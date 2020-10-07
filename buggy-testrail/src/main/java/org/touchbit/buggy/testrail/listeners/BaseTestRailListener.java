package org.touchbit.buggy.testrail.listeners;

import org.testng.IExecutionListener;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.touchbit.buggy.core.config.BuggyConfig;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.testng.BaseBuggyExecutionListener;
import org.touchbit.buggy.testrail.BaseTestRailConfig;
import org.touchbit.buggy.testrail.RunsResultsStorage;
import org.touchbit.buggy.testrail.StatusMapper;
import org.touchbit.testrail4j.core.BasicAuth;
import org.touchbit.testrail4j.core.query.filter.GetCasesFilter;
import org.touchbit.testrail4j.jackson2.feign.client.TestRailClient;
import org.touchbit.testrail4j.jackson2.feign.client.TestRailClientBuilder;
import org.touchbit.testrail4j.jackson2.model.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Listener of the test methods to be followed for the subsequent translation of the results into the TestRail.
 * <p>
 * Created by Oleg Shaburov on 11.11.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BaseTestRailListener<S> extends BaseBuggyExecutionListener
        implements IExecutionListener, IInvokedMethodListener {

    private static RunsResultsStorage runsResults = new RunsResultsStorage();
    private TestRailClient client;
    private StatusMapper<S> statusMapper;

    public BaseTestRailListener(StatusMapper<S> statusMapper) {
        this.statusMapper = statusMapper;
        BasicAuth interceptor = new BasicAuth(BaseTestRailConfig.getLogin(), BaseTestRailConfig.getPass());
        client = TestRailClientBuilder.build(interceptor, BaseTestRailConfig.getTestRailHost());
    }

    protected void setTestRailClient(TestRailClient client) {
        this.client = client;
    }

    @Override
    public void onExecutionFinish() {
        if (BaseTestRailConfig.isTestRailEnable()) {
            long time = new Date().getTime();
            // We get a list of run_id for which there are test results
            for (Long rId : runsResults.getRunIDs()) {
                try {
                    List<Long> caseIDsCheckList = new ArrayList<>(runsResults.getCaseIDsForRun(rId));
                    // Getting the list of cases for rId
                    List<Long> testRailCaseIds = getTestRailCasesByRunId(rId);
                    frameworkLog.info("List of case identifiers for run_id={} received.", rId);
                    ArrayList<TRResult> resultsForRun = new ArrayList<>();
                    // Be safe from remote test cases so as not to get the error when adding results.
                    runsResults.getResultsForRun(rId)
                            .stream()
                            .filter(result -> testRailCaseIds.contains(result.getCaseId()))
                            .forEach(r -> {
                                resultsForRun.add(r);
                                // Deletion occurs by reference to the Integer object.
                                caseIDsCheckList.remove(r.getCaseId());
                            });
                    frameworkLog.info("List of cases for run_id = {} filtered.", rId);
                    if (!resultsForRun.isEmpty()) {
                        frameworkLog.debug("List of cases for run_id={}:\n{}\n", rId, resultsForRun);
                        addResultsForCasesByRunId(rId, resultsForRun);
                    }
                    frameworkLog.info("Test results for run_id={} transferred.", rId);
                    if (!caseIDsCheckList.isEmpty()) {
                        caseIDsCheckList.forEach(cId -> {
//                            Buggy.incrementBuggyErrors();
                            frameworkLog.error("In TestRail is absent or in the " +
                                    "test the case with id={} is incorrectly specified", cId);
                        });
                    }
                } catch (Exception e) {
//                    Buggy.incrementBuggyErrors();
                    frameworkLog.error("Error getting data from TestRail.", e);
                }
            }
            frameworkLog.info("Test results are translated into TestRail for {} ms.", new Date().getTime() - time);
        }
    }

    protected void addResult(ITestResult result, Details details, S status, String comment) {
        String msg = comment + addAttachments(result);
        String strRunID = "0"; // TODO
//        String strRunID = String.valueOf(result.getAttribute(RUN_ID.toString()));
        if ("null".equals(String.valueOf(strRunID))) {
//            strRunID = String.valueOf(System.getProperty(RUN_ID.toString()));
        }
        try {
            Long runID = Long.parseLong(strRunID);
            for (String caseId : details.caseIDs()) {
                if (statusMapper.getId(status) > 0) {
                    StringJoiner defects = new StringJoiner(", ");
                    for (String bug : details.bugs()) {
                        defects.add(bug);
                    }
                    TRResult trResult = new TRResult()
                            .withCaseId(Long.valueOf(caseId))
                            .withStatusId(statusMapper.getId(status))
                            .withComment(msg)
                            .withDefects(defects.toString());
                    runsResults.add(runID, trResult);
                } else {
                    frameworkLog.warn("Result status {} is ignored. ID={}", status, statusMapper.getId(status));
                }
            }
        } catch (Exception ignore) {
//            BuCo.incrementBuggyErrors();
//            frameworkLog.error("An incorrect value {} = {} was received", RUN_ID, strRunID);
        }
    }

    protected String addAttachments(ITestResult testResult) {
        Method method = testResult.getMethod().getConstructorOrMethod().getMethod();
        Details trCase = method.getAnnotation(Details.class);

        String caseIds = trCase != null ? Arrays.toString(trCase.caseIDs()) + "_" : "";
        String prefix = caseIds + method.getName();
        return "\n" + attachLogfile(prefix) + "\n" + attachScreenshots(prefix);
    }

    protected String attachLogfile(String prefix) {
        String logfile = BuggyConfig.getArtifactsUrl() + "/tests/" + prefix + ".log";
        return attachFile(logfile);
    }

    protected String attachScreenshots(String prefix) {
        StringBuilder links = new StringBuilder();
        File screenshotsDir = new File("target/screenshots");
        File[] files = screenshotsDir.listFiles((dir, name) -> name.startsWith(prefix));
        if (files != null) {
            for (File f : files) {
                links.append("!");
                links.append(attachFile(f.getPath()));
            }
        }
        return links.toString();
    }

    protected String attachFile(String filename) {
        File sourceFile = new File(filename);
        return "[" + sourceFile.getName() + "](" +
                BuggyConfig.getArtifactsUrl() + "/tests/" + sourceFile.getName() + ")\n";
    }

    protected List<Long> getTestRailCasesByRunId(Long rId) {
        TRRun run = client.getRun(rId);
        GetCasesFilter getCasesQueryMap = new GetCasesFilter();
        getCasesQueryMap.setSuiteId(run.getSuiteId());
        return client.getCases(run.getProjectId(), getCasesQueryMap).stream()
                .map(TRCase::getId)
                .collect(Collectors.toList());
    }

    public void addResultsForCasesByRunId(Long rId, List<TRResult> resultList) {
        TRResults results = new TRResults();
        results.setResults(resultList);
        client.addResultsForCases(results, rId);
    }

    public List<Long> getSuitesIds(Long pId) {
        return client.getSuites(pId).stream()
                .map(TRSuite::getId)
                .collect(Collectors.toList());
    }

    public List<Long> getProjectsIds() {
        return client.getProjects().stream()
                .map(TRProject::getId)
                .collect(Collectors.toList());
    }

}