/*
 * Copyright © 2018 Shaburov Oleg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.touchbit.buggy.core.testng;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.testng.*;
import org.touchbit.buggy.core.config.BuggyConfig;
import org.touchbit.buggy.core.exceptions.CorruptedTestException;
import org.touchbit.buggy.core.log.BuggyLoggers;
import org.touchbit.buggy.core.log.ConfigurationLogger;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.utils.IOHelper;
import org.touchbit.buggy.core.utils.JUtils;
import org.touchbit.buggy.core.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static org.testng.ITestResult.SKIP;
import static org.testng.ITestResult.SUCCESS;
import static org.testng.ITestResult.*;
import static org.touchbit.buggy.core.model.Status.*;

/**
 * Listener for processing executable tests.
 * <p>
 * Created by Shaburov Oleg on 31.07.2017.
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "squid:S2629"})
public class BuggyExecutionListener extends BaseBuggyExecutionListener
        implements IExecutionListener, IInvokedMethodListener, ISuiteListener, ITestListener, IClassListener {

    private static final ThreadLocal<List<String>> STEPS = new ThreadLocal<>();

    public BuggyExecutionListener() {
    }

    public BuggyExecutionListener(Logger testLogger, Logger frameworkLogger, Logger consoleLogger) {
        testLog = testLogger;
        frameworkLog = frameworkLogger;
        consoleLog = consoleLogger;
    }

    /**
     * Method for the separation of steps in the test log.
     */
    public static void step(@NotNull final Logger logger, @NotNull final String msg, @NotNull final Object... args) {
        int stepNum = getSteps().size() + 1;
        String msgBody = msg;
        for (Object s : args) {
            msgBody = msgBody.replaceFirst("\\{}", String.valueOf(s));
        }
        String stepInfo = "Step " + stepNum + ". " + msgBody;
        logger.info(" ------------> {}", stepInfo);
        getSteps().add(stepInfo);
    }

    public static List<String> getSteps() {
        if (STEPS.get() == null) {
            STEPS.set(new ArrayList<>());
        }
        return STEPS.get();
    }

    public static void setSteps(List<String> stepList) {
        STEPS.set(stepList);
    }

    @Override
    public boolean isEnable() {
        return true;
    }

    @Override
    public void onExecutionStart() {
        super.onExecutionStart();
        consoleLog.info("");
    }

    @Override
    public void onExecutionFinish() {
        super.onExecutionFinish();
        printTestStatistic();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        onTestFinish(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        onTestFinish(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        onTestFinish(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        onTestFinish(result);
    }

    public void onTestFinish(ITestResult result) {
        Method method = getRealMethod(result);
        Throwable t = result.getThrowable();
        if ((t != null && t.getClass().getName().contains("ExpectedImplementationException"))) {
            return;
        }
        if (result.getStatus() == ITestResult.SKIP) {
            testLog.warn("The test {} is skipped.", method.getName());
            return;
        }
        if (!getSteps().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            getSteps().forEach(step -> sb.append(step).append("\n"));
            testLog.info("Steps:\n{}", sb);
        } else {
//            Buggy.incrementBuggyWarns();
            frameworkLog.warn("There are no playback steps in the test method {}", method.getName());
        }
        testLog.info("Date: {}", new Date());
    }

    @Override
    public void onAfterClass(ITestClass iTestClass) {
        if (getSuite(iTestClass) == null) {
//            Buggy.incrementBuggyErrors();
            frameworkLog.error("There is no @Suite annotation for the test class: {}", iTestClass.getName());
        }
    }

    @Override
    public void onStart(ISuite suite) {
        disableTestsByType(suite);
        if (!BuggyConfig.isForce()) {
            disableTestsByStatus(suite);
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        suite.getAllInvokedMethods()
                .forEach(m -> {
                    try {
                        copyTestMethodLogFile(m);
                    } catch (Exception e) {
//                        Buggy.incrementBuggyErrors();
                        frameworkLog.error(e.getMessage(), e);
                    }
                });
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        STEPS.set(new ArrayList<>());
        String methodName = getMethodName(method);
        BuggyLoggers.setSiftingLogFilePath(getInvokedMethodLogFileName(method));
        if (method.isTestMethod()) {
            testLog.info("Test method is running:\n{} - {}", methodName, getDescription(method));
        } else {
            testLog.info("Configuration method is running:\n{} - {}.", methodName, getDescription(method));
        }
        if (testLog.isDebugEnabled()) {
            StringJoiner sj = new StringJoiner("\n", "\n", "\n");
            for (Annotation annotation : getRealMethod(method).getAnnotations()) {
                sj.add(annotation.annotationType().getTypeName());
            }
            testLog.debug("Declared method annotations:{}", sj);
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        Throwable throwable = testResult.getThrowable();
        String methodName = getMethodName(method);
        if (throwable != null) {
            List<String> stepList = getSteps();
            if (!stepList.isEmpty()) {
                int lastIndex = stepList.size() - 1;
                stepList.set(lastIndex, stepList.get(lastIndex) + " - ERROR");
                setSteps(stepList);
            }
            testLog.error("Execution of {} resulted in an error.", methodName, throwable);
        }
        if (method.isTestMethod()) {
            if (testResult.getStatus() != SKIP) {
                testCount.incrementAndGet();
            }
            Details details = getDetails(method);
            if (details != null) {
                processTestMethodResult(method, testResult, details);
            } else {
//                Buggy.incrementBuggyWarns();
                frameworkLog.warn("The test method {} does not contain the @Details annotation", methodName);
            }
        } else {
            processConfigurationMethodResult(method, testResult);
        }

    }

    public void processTestMethodResult(IInvokedMethod m, ITestResult testResult, Details details) {
        ITestNGMethod method = m.getTestMethod();
        switch (testResult.getStatus()) {
            case SUCCESS:
                switch (details.status()) {
                    case BLOCKED:
                    case EXP_FIX:
                        resultLog(method, Status.FIXED, buildDetailsMessage(details));
                        break;
                    case EXP_IMPL:
                        resultLog(method, Status.IMPLEMENTED, buildDetailsMessage(details));
                        break;
                    case CORRUPTED:
                        resultLog(method, Status.CORRUPTED, buildDetailsMessage(details));
                        testResult.setThrowable(new CorruptedTestException());
                        break;
                    default:
                        resultLog(method, Status.SUCCESS, buildDetailsMessage(details));
                }
                break;
            case SUCCESS_PERCENTAGE_FAILURE:
            case FAILURE:
                switch (details.status()) {
                    case CORRUPTED:
                        resultLog(method, Status.CORRUPTED, buildDetailsMessage(details));
                        testResult.setThrowable(new CorruptedTestException());
                        break;
                    case EXP_FIX:
                        resultLog(method, Status.EXP_FIX, buildDetailsMessage(details));
                        break;
                    case EXP_IMPL:
                        resultLog(method, Status.EXP_IMPL, buildDetailsMessage(details));
                        break;
                    case BLOCKED:
                        resultLog(method, Status.BLOCKED, buildDetailsMessage(details));
                        break;
                    default:
                        resultLog(method, Status.FAILED, buildDetailsMessage(details));
                }
                break;
            case SKIP:
                resultLog(method, Status.SKIP, buildDetailsMessage(details));
                break;
            default:
                frameworkLog.error("Received unprocessed status: {}", testResult.getStatus());
//                Buggy.incrementBuggyErrors();
        }
    }

    public void processConfigurationMethodResult(IInvokedMethod method, ITestResult testResult) {
        String methodName = getMethodName(method);
        String description = getDescription(method);
        switch (testResult.getStatus()) {
            case SUCCESS:
                testLog.info("Invoke configuration method [{}] completed successfully", methodName);
                break;
            case SKIP:
                testLog.warn("Invoke configuration method [{}] skipped. " +
                        "List of dependent tests that were missed: {}", methodName, description);
                break;
            case SUCCESS_PERCENTAGE_FAILURE:
            case FAILURE:
                testLog.error("Invoke configuration method [{}] completed with error \n{}",
                        methodName, testResult.getThrowable().getMessage());
                break;
            default:
//                Buggy.incrementBuggyErrors();
                frameworkLog.error("Received unresolved status of configuration method [{}]. Status: {}",
                        methodName, testResult.getStatus());
        }
    }

    public void disableTestsByStatus(ISuite suite) {
        suite.getAllMethods().forEach(method -> {
            Details details = getDetails(method);
            if (details != null) {
                if (method.getInvocationCount() > 0) {
                    switch (details.status()) {
                        case EXP_FIX:
                        case EXP_IMPL:
                        case BLOCKED:
                        case CORRUPTED:
                            method.setInvocationCount(0);
                            resultLog(method, details.status(), buildDetailsMessage(details, "forced test run disabled"));
                            break;
                        default:
                            // do nothing
                    }
                }
            } else {
//                Buggy.incrementBuggyWarns();
                frameworkLog.warn("The test method {} does not contain the @Details annotation",
                        method.getMethodName());
            }
        });
    }

    public void disableTestsByType(final ISuite suite) {
        final List<ITestNGMethod> methods = suite.getAllMethods();
        methods.forEach(method -> {
            Details details = getDetails(method);
            List<Type> configType = BuggyConfig.getTypes();
            if (details != null) {
                List<Type> methodTypes = new ArrayList<>(Arrays.asList(details.type()));
                boolean matched = methodTypes.stream().anyMatch(configType::contains);
                if (!configType.contains(Type.ALL) && !matched) {
                    method.setInvocationCount(0);
                    resultLog(method, Status.SKIP, buildDetailsMessage(details, details.type(), "test types"));
                }
            }
        });
    }

    public void copyTestMethodLogFile(IInvokedMethod method) throws IOException {
        Details details = getDetails(method);
        int iTestResultStatus = method.getTestResult().getStatus();
        File sourceFile = new File(getInvokedMethodLogFileName(method));
        File targetFile = null;
        if (details == null) {
            if (iTestResultStatus == FAILURE || iTestResultStatus == SUCCESS_PERCENTAGE_FAILURE) {
                targetFile = new File("new", getInvokedMethodLogFileName(method));
                copyFile(sourceFile, targetFile);
            }
            return;
        }
        Status status = details.status();
        switch (iTestResultStatus) {
            case ITestResult.SUCCESS:
                if (status.equals(EXP_FIX) || status.equals(BLOCKED)) {
                    targetFile = new File("fixed", getInvokedMethodLogFileName(method));
                }
                if (status.equals(EXP_IMPL)) {
                    targetFile = new File("implemented", getInvokedMethodLogFileName(method));
                }
                break;
            case ITestResult.FAILURE:
            case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
                switch (status) {
                    case EXP_FIX:
                        targetFile = new File("exp_fix", getInvokedMethodLogFileName(method));
                        break;
                    case EXP_IMPL:
                        targetFile = new File("exp_impl", getInvokedMethodLogFileName(method));
                        break;
                    case BLOCKED:
                        targetFile = new File("blocked", getInvokedMethodLogFileName(method));
                        break;
                    case CORRUPTED:
                        targetFile = new File("corrupted", getInvokedMethodLogFileName(method));
                        break;
                    default:
                        targetFile = new File("new", getInvokedMethodLogFileName(method));
                }
            default:
                // ignore unhandled statuses
        }
        if (targetFile != null) {
            copyFile(sourceFile, targetFile);
        }
    }

    public void resultLog(ITestNGMethod method, Status status, String details) {
        String methodName = method.getMethodName();
        Suite suite = getSuite(method);
        String statusName = status.name();
        StringJoiner resultMsg = new StringJoiner(" ");
        if (BuggyConfig.isPrintSuite()) {
            StringJoiner sj = new StringJoiner(" ", " [", "]");
            sj.add(JUtils.getGoal(Suite::component, suite).getName().trim());
            sj.add(JUtils.getGoal(Suite::service, suite).getName().trim());
            sj.add(JUtils.getGoal(Suite::interfaze, suite).getName().trim());
            sj.add(suite.purpose().trim());
            resultMsg.add(sj.toString().trim());
        }
        testLog.info("{} - {} {}", methodName, statusName, method.getDescription());
        String detailsString = details.trim();
        if ((BuggyConfig.isPrintCause() || method.getInvocationCount() < 1) && !detailsString.isEmpty()) {
            resultMsg.add(detailsString);
        }
        String logPathString = getLogFilePath(method).trim();
        if (BuggyConfig.isPrintLog() && method.getInvocationCount() > 0 && !logPathString.isEmpty() &&
                (!BuggyConfig.isPrintLogFileOnlyFail() || status != Status.SUCCESS)) {
            resultMsg.add("\n  └");
            resultMsg.add(logPathString);
        }
        printASCIIStatus(status, StringUtils.dotFiller(methodName, 47, statusName) +
                (resultMsg.length() > 0 ? " " + resultMsg.toString().trim() : ""));
        if (method.isTest()) {
            if (method.getInvocationCount() > 0) {
                increment(status);
            } else {
                increment(Status.SKIP);
            }
        }
    }

    protected void printASCIIStatus(Status status, String msg) {
        switch (status) {
            case FAILED:
            case CORRUPTED:
                consoleLog.error(msg);
                break;
            case EXP_IMPL:
            case EXP_FIX:
            case BLOCKED:
            case SKIP:
                consoleLog.warn(msg);
                break;
            case IMPLEMENTED:
            case FIXED:
                consoleLog.debug(msg);
                break;
            default:
                consoleLog.info(msg);
        }
    }

    protected void increment(Status status) {
        switch (status) {
            case FAILED:
                newError.incrementAndGet();
                break;
            case CORRUPTED:
                corruptedError.incrementAndGet();
                break;
            case EXP_IMPL:
                expImplError.incrementAndGet();
                break;
            case EXP_FIX:
                expFixError.incrementAndGet();
                break;
            case BLOCKED:
                blockedError.incrementAndGet();
                break;
            case IMPLEMENTED:
                implemented.incrementAndGet();
                break;
            case FIXED:
                fixed.incrementAndGet();
                break;
            case SKIP:
                skippedTests.incrementAndGet();
                break;
            default:
                // do nothing
        }
    }

    protected void printTestStatistic() {
        ConfigurationLogger.blockDelimiter();
//        if (Buggy.getBuggyErrors() > 0) {
//            consoleLog.error(StringUtils.dotFiller("Framework errors", 47, Buggy.getBuggyErrors()));
//        }
//        if (Buggy.getBuggyWarns() > 0) {
//            consoleLog.warn(StringUtils.dotFiller("Framework warns", 47, Buggy.getBuggyWarns()));
//        }
//        if (Buggy.getBuggyErrors() > 0 || Buggy.getBuggyWarns() > 0) {
//            ConfigurationLogger.blockDelimiter();
//        }
        int fixedCount = fixed.get() + implemented.get();
        int errorCount = expFixError.get() + expImplError.get() + newError.get() +
                corruptedError.get() + blockedError.get();
        consoleLog.info(StringUtils.dotFiller("Total tests run", 47, testCount.get()));
        consoleLog.info(StringUtils.dotFiller("Successful tests", 47, testCount.get() - errorCount));
        checkWarnAndPrint("Skipped tests", skippedTests.get());
        if (errorCount > 0) {
            checkWarnAndPrint("Failed tests", errorCount);
            checkErrorAndPrint("New Errors", newError.get());
            ConfigurationLogger.blockDelimiter();
            checkWarnAndPrint("Waiting to fix a defect", expFixError.get());
            checkWarnAndPrint("Waiting for implementation", expImplError.get());
            checkWarnAndPrint("Blocked tests", blockedError.get());
            checkErrorAndPrint("Corrupted tests", corruptedError.get());
        } else {
            String result = StringUtils.dotFiller("Errors", 47, errorCount);
            consoleLog.info(result);
        }
        if (fixedCount > 0) {
            ConfigurationLogger.blockDelimiter();
            checkDebugAndPrint("Fixed cases", fixed.get());
            checkDebugAndPrint("Implemented cases", implemented.get());
        }
        ConfigurationLogger.blockDelimiter();
        consoleLog.info(StringUtils.dotFiller("Execution time", 47, org.apache.commons.lang3.time.DurationFormatUtils
                .formatDuration(finishTime - startTime, "HH:mm:ss,SSS")));
        ConfigurationLogger.blockDelimiter();
    }

    protected void checkDebugAndPrint(String msg, int count) {
        String result = StringUtils.dotFiller(msg, 47, count);
        if (count > 0) {
            consoleLog.debug(result);
        } else {
            consoleLog.info(result);
        }
    }

    protected void checkErrorAndPrint(String msg, int count) {
        String result = StringUtils.dotFiller(msg, 47, count);
        if (count > 0) {
            consoleLog.error(result);
        } else {
            consoleLog.info(result);
        }
    }

    protected void checkWarnAndPrint(String msg, int count) {
        String result = StringUtils.dotFiller(msg, 47, count);
        if (count > 0) {
            consoleLog.warn(result);
        } else {
            consoleLog.info(result);
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected void checkTraceAndPrint(String msg, int count) {
        String result = StringUtils.dotFiller(msg, 47, count);
        if (count > 0) {
            consoleLog.trace(result);
        } else {
            consoleLog.info(result);
        }
    }

    protected void copyFile(File sourceFile, File destFile) throws IOException {
        IOHelper.copyFile(sourceFile, destFile);
    }

    @Override
    public void onStart(ITestContext context) {
        // do nothing
    }

    @Override
    public void onFinish(ITestContext context) {
        // do nothing
    }

    @Override
    public void onBeforeClass(ITestClass testClass) {
        // do nothing
    }

    @Override
    public void onTestStart(ITestResult result) {
        // do nothing
    }

}
