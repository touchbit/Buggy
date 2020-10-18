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
import org.touchbit.buggy.core.config.BuggyConfigurationYML;
import org.touchbit.buggy.core.model.Buggy;
import org.touchbit.buggy.core.model.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Listener for processing executable tests.
 * <p>
 * Created by Shaburov Oleg on 31.07.2017.
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "squid:S2629"})
public abstract class BuggyExecutionListener extends BaseBuggyExecutionListener
        implements IExecutionListener, IInvokedMethodListener, ISuiteListener, ITestListener, IClassListener {

    public BuggyExecutionListener() {
    }

    public BuggyExecutionListener(Logger testLogger, Logger frameworkLogger, Logger consoleLogger) {
//        testLog = testLogger;
//        frameworkLog = frameworkLogger;
//        consoleLog = consoleLogger;
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
//        printTestStatistic();
    }
//
//    @Override
//    public void onTestSuccess(ITestResult result) {
//        onTestFinish(result);
//    }
//
//    @Override
//    public void onTestFailure(ITestResult result) {
//        onTestFinish(result);
//    }
//
//    @Override
//    public void onTestSkipped(ITestResult result) {
//        onTestFinish(result);
//    }
//
//    @Override
//    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
//        onTestFinish(result);
//    }
//
//    public void onTestFinish(ITestResult result) {
//        Method method = getRealMethod(result);
//        Throwable t = result.getThrowable();
//        if ((t != null && t.getClass().getName().contains("ExpectedImplementationException"))) {
//            return;
//        }
//        if (result.getStatus() == ITestResult.SKIP) {
//            testLog.warn("The test {} is skipped.", method.getName());
//            return;
//        }
//        if (!getSteps().isEmpty()) {
//            StringBuilder sb = new StringBuilder();
//            getSteps().forEach(step -> sb.append(step).append("\n"));
//            testLog.info("Steps:\n{}", sb);
//        } else {
////            Buggy.incrementBuggyWarns();
//            frameworkLog.warn("There are no playback steps in the test method {}", method.getName());
//        }
//        testLog.info("Date: {}", new Date());
//    }
//
//    @Override
//    public void onAfterClass(ITestClass iTestClass) {
//        if (getSuite(iTestClass) == null) {
////            Buggy.incrementBuggyErrors();
//            frameworkLog.error("There is no @Suite annotation for the test class: {}", iTestClass.getName());
//        }
//    }
//
    @Override
    public void onStart(ISuite suite) {
        disableTestsByType(suite);
        if (!BuggyConfigurationYML.isForceRun()) {
            disableTestsByStatus(suite);
        }
    }
//
//    public void processTestMethodResult(IInvokedMethod m, ITestResult testResult, Details details) {
//        ITestNGMethod method = m.getTestMethod();
//        switch (testResult.getStatus()) {
//            case SUCCESS:
//                switch (details.status()) {
//                    case BLOCKED:
//                    case EXP_FIX:
//                        resultLog(method, Status.FIXED, buildDetailsMessage(details));
//                        break;
//                    case EXP_IMPL:
//                        resultLog(method, Status.IMPLEMENTED, buildDetailsMessage(details));
//                        break;
//                    case CORRUPTED:
//                        resultLog(method, Status.CORRUPTED, buildDetailsMessage(details));
//                        testResult.setThrowable(new CorruptedTestException());
//                        break;
//                    default:
//                        resultLog(method, Status.SUCCESS, buildDetailsMessage(details));
//                }
//                break;
//            case SUCCESS_PERCENTAGE_FAILURE:
//            case FAILURE:
//                switch (details.status()) {
//                    case CORRUPTED:
//                        resultLog(method, Status.CORRUPTED, buildDetailsMessage(details));
//                        testResult.setThrowable(new CorruptedTestException());
//                        break;
//                    case EXP_FIX:
//                        resultLog(method, Status.EXP_FIX, buildDetailsMessage(details));
//                        break;
//                    case EXP_IMPL:
//                        resultLog(method, Status.EXP_IMPL, buildDetailsMessage(details));
//                        break;
//                    case BLOCKED:
//                        resultLog(method, Status.BLOCKED, buildDetailsMessage(details));
//                        break;
//                    default:
//                        resultLog(method, Status.FAILED, buildDetailsMessage(details));
//                }
//                break;
//            case SKIP:
//                resultLog(method, Status.SKIP, buildDetailsMessage(details));
//                break;
//            default:
//                frameworkLog.error("Received unprocessed status: {}", testResult.getStatus());
////                Buggy.incrementBuggyErrors();
//        }
//    }
//
//    public void processConfigurationMethodResult(IInvokedMethod method, ITestResult testResult) {
//        String methodName = getMethodName(method);
//        String description = getDescription(method);
//        switch (testResult.getStatus()) {
//            case SUCCESS:
//                testLog.info("Invoke configuration method [{}] completed successfully", methodName);
//                break;
//            case SKIP:
//                testLog.warn("Invoke configuration method [{}] skipped. " +
//                        "List of dependent tests that were missed: {}", methodName, description);
//                break;
//            case SUCCESS_PERCENTAGE_FAILURE:
//            case FAILURE:
//                testLog.error("Invoke configuration method [{}] completed with error \n{}",
//                        methodName, testResult.getThrowable().getMessage());
//                break;
//            default:
////                Buggy.incrementBuggyErrors();
//                frameworkLog.error("Received unresolved status of configuration method [{}]. Status: {}",
//                        methodName, testResult.getStatus());
//        }
//    }
//
    public void disableTestsByStatus(ISuite suite) {
        suite.getAllMethods().forEach(method -> {
            Buggy details = getBuggyAnnotation(method);
            if (details != null) {
                if (method.getInvocationCount() > 0) {
                    switch (details.status()) {
                        case EXP_FIX:
                        case EXP_IMPL:
                        case BLOCKED:
                        case CORRUPTED:
                            System.out.println(" >>>> disableTestsByStatus : " + method.getMethodName() + ": " + details.status());
                            method.setInvocationCount(0);
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
            Buggy details = getBuggyAnnotation(method);
            Type[] types = BuggyConfigurationYML.getTypes();
            Set<Type> configType = new HashSet<>(Arrays.asList(types));
            if (details != null) {
                List<Type> methodTypes = new ArrayList<>(Arrays.asList(details.types()));
                boolean matched = methodTypes.stream().anyMatch(configType::contains);
                if (!configType.contains(Type.ALL) && !matched) {
                    System.out.println(" >>>> disableTestsByStatus : " + method.getMethodName() + ": " + methodTypes);
                    method.setInvocationCount(0);
                }
            }
        });
    }
//
//    public void resultLog(ITestNGMethod method, Status status, String details) {
//        String methodName = method.getMethodName();
//        Suite suite = getSuite(method);
//        String statusName = status.name();
//        StringJoiner resultMsg = new StringJoiner(" ");
//        if (BuggyConfig.isPrintSuite()) {
//            StringJoiner sj = new StringJoiner(" ", " [", "]");
//            sj.add(JUtils.getGoal(Suite::component, suite).getName().trim());
//            sj.add(JUtils.getGoal(Suite::service, suite).getName().trim());
//            sj.add(JUtils.getGoal(Suite::interfaze, suite).getName().trim());
//            sj.add(suite.purpose().trim());
//            resultMsg.add(sj.toString().trim());
//        }
//        testLog.info("{} - {} {}", methodName, statusName, method.getDescription());
//        String detailsString = details.trim();
//        if ((BuggyConfig.isPrintCause() || method.getInvocationCount() < 1) && !detailsString.isEmpty()) {
//            resultMsg.add(detailsString);
//        }
//        String logPathString = getLogFilePath(method, status).trim();
//        if (BuggyConfig.isPrintLog() && method.getInvocationCount() > 0 && !logPathString.isEmpty() &&
//                (!BuggyConfig.isPrintLogFileOnlyFail() || status != Status.SUCCESS)) {
//            resultMsg.add("\n  └");
//            resultMsg.add(logPathString);
//        }
//        printASCIIStatus(status, StringUtils.dotFiller(methodName, 47, statusName) +
//                (resultMsg.length() > 0 ? " " + resultMsg.toString().trim() : ""));
//    }
//
//    protected void printASCIIStatus(Status status, String msg) {
//        switch (status) {
//            case FAILED:
//            case CORRUPTED:
//                consoleLog.error(msg);
//                break;
//            case EXP_IMPL:
//            case EXP_FIX:
//            case BLOCKED:
//            case SKIP:
//                consoleLog.warn(msg);
//                break;
//            case IMPLEMENTED:
//            case FIXED:
//                consoleLog.debug(msg);
//                break;
//            default:
//                consoleLog.info(msg);
//        }
//    }
//
//
//
//    protected void copyFile(File sourceFile, File destFile) throws IOException {
//        IOHelper.copyFile(sourceFile, destFile);
//    }

}
