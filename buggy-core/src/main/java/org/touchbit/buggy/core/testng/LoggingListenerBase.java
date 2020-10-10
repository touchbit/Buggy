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

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.touchbit.buggy.core.config.BuggyConfigurationYML;
import org.touchbit.buggy.core.logback.ConfLogger;
import org.touchbit.buggy.core.logback.ConsoleLogger;
import org.touchbit.buggy.core.logback.FrameworkLogger;
import org.touchbit.buggy.core.logback.SiftingTestLogger;
import org.touchbit.buggy.core.model.*;
import org.touchbit.buggy.core.utils.JUtils;
import org.touchbit.buggy.core.utils.TestNGHelper;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import static org.touchbit.buggy.core.utils.ANSI.RED;

/**
 * Listener for processing executable tests.
 * <p>
 * Created by Shaburov Oleg on 31.07.2017.
 */
public abstract class LoggingListenerBase implements BuggyListener, IInvokedMethodListener {

    private static final FrameworkLogger FRAMEWORK = new FrameworkLogger();
    private static final ConsoleLogger CONSOLE = new ConsoleLogger();
    private static final SiftingTestLogger TEST = new SiftingTestLogger();

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        SiftingTestLogger.setTestLogFileName(method);
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        TEST.info("Date: {}", new Date());
        ResultStatus resultStatus = getResultStatus(method);
        SiftingTestLogger.setTestResultStatus(resultStatus);
        Boolean printLog = BuggyConfigurationYML.isPrintLog();
        Boolean printSuite = BuggyConfigurationYML.isPrintSuite();
        Boolean printCause = BuggyConfigurationYML.isPrintCause();
        String methodName = TestNGHelper.getMethodName(method);
        Throwable throwable = testResult.getThrowable();
        if (hasDetails(method) && hasSuite(method) && method.isTestMethod()) {
            Details details = getDetails(method);
            Suite suite = getSuite(method);
            String component = JUtils.getGoal(Suite::component, suite).getName().trim();
            String service = JUtils.getGoal(Suite::service, suite).getName().trim();
            String interfaze = JUtils.getGoal(Suite::interfaze, suite).getName().trim();
            //noinspection ConstantConditions
            String purpose = suite.purpose().trim();
            //noinspection ConstantConditions
            Status status = details.status();
            Type[] types = details.types();
            String[] ids = details.caseIDs();
            String[] issues = details.issues();
            String[] bugs = details.bugs();
            String indent = " ";

            String dotPlaceholder = ConfLogger.getDotPlaceholder(methodName, resultStatus);
            StringBuilder message = new StringBuilder();
            message.append(dotPlaceholder);
            if (printSuite) {
                message.append(" [")
                        .append(component).append(" ")
                        .append(service).append(" ")
                        .append(interfaze);
                if (!purpose.isEmpty()) {
                    message.append(" ").append(purpose);
                }
                message.append("]");
            }
            if (hasDescription(method)) {
//                message.append("\n").append(" § ").append(description);
                message.append("\n  Case:  ").append(getDescription(method));
                indent += indent;
            }
            if (printLog) {
                message.append("\n  Log:   ").append(getLogFilePath());
                indent += indent;
            }
            if (printCause && bugs.length > 0) {
                if (BuggyConfigurationYML.getIssuesUrl() != null && !BuggyConfigurationYML.getIssuesUrl().isEmpty()) {
                    for (String bug : bugs) {
//                        message.append("\n  └").append(BOLD.wrap(RED.wrap(" ❗ ")))
                        message.append("\n  Bug:   ").append(BuggyConfigurationYML.getIssuesUrl()).append(bug);
                    }
                } else {
                    message.append(" ").append(Arrays.toString(bugs));
                }
            }
            if (true) {
                if (BuggyConfigurationYML.getIssuesUrl() != null && !BuggyConfigurationYML.getIssuesUrl().isEmpty()) {
                    for (String bug : bugs) {
//                        message.append("\n  └").append(BOLD.wrap(GREEN.wrap(" ✓ ")))
                        message.append("\n  Issue: ").append(BuggyConfigurationYML.getIssuesUrl()).append(bug);
                    }
                } else {
                    message.append(" ").append(Arrays.toString(bugs));
                }
            }

            if (printCause && throwable != null) {
//                message.append("\n  └ ").append(BOLD.wrap(RED.wrap("↯ ")))
                message.append("\n  ")
                        .append(RED.wrap(throwable.getClass().getSimpleName()))
                        .append(": ")
                        .append(throwable.getMessage());
            }
            ConfLogger.info(message.append("\n").toString());
//            processTestMethodResult(method, testResult, details);
        } else {
//            processConfigurationMethodResult(method, testResult);
        }
    }
//
//    public void resultLog(ITestNGMethod method, Status status, String details) {
//        String methodName = method.getMethodName();
//        Suite suite = TestNGHelper.getSuite(method);
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
//        TEST.info("{} - {} {}", methodName, statusName, method.getDescription());
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
//                FRAMEWORK.error("Received unprocessed status: {}", testResult.getStatus());
////                Buggy.incrementBuggyErrors();
//        }
//    }
//
//    public void processConfigurationMethodResult(IInvokedMethod method, ITestResult testResult) {
//        String methodName = TestNGHelper.getMethodName(method);
//        String description = getDescription(method);
//        switch (testResult.getStatus()) {
//            case SUCCESS:
//                TEST.info("Invoke configuration method [{}] completed successfully", methodName);
//                break;
//            case SKIP:
//                TEST.warn("Invoke configuration method [{}] skipped. " +
//                        "List of dependent tests that were missed: {}", methodName, description);
//                break;
//            case SUCCESS_PERCENTAGE_FAILURE:
//            case FAILURE:
//                TEST.error("Invoke configuration method [{}] completed with error \n{}",
//                        methodName, testResult.getThrowable().getMessage());
//                break;
//            default:
////                Buggy.incrementBuggyErrors();
//                FRAMEWORK.error("Received unresolved status of configuration method [{}]. Status: {}",
//                        methodName, testResult.getStatus());
//        }
//    }
//
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

    protected String getLogFilePath() {
        // Do not change the check. Feature parsing values by jCommander library.
        if (!"null".equalsIgnoreCase(String.valueOf(BuggyConfigurationYML.getArtifactsUrl()))) {
            return BuggyConfigurationYML.getArtifactsUrl().endsWith("/") ?
                    BuggyConfigurationYML.getArtifactsUrl() :
                    BuggyConfigurationYML.getArtifactsUrl() + "/";
        } else {
            File file = SiftingTestLogger.getSiftingLogFile();
            if (file != null) {
                return "file://" + file.getAbsolutePath();
            } else {
                return "Log file not found";
            }
        }
    }

    protected String getLogFileName(IInvokedMethod method) {
        return getLogFileName(method.getTestMethod());
    }

    protected String getLogFileName(ITestNGMethod iTestNGMethod) {
        Method method = iTestNGMethod.getConstructorOrMethod().getMethod();
        return method.getName() + ".log";
    }

}
