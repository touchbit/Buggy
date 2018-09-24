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

package org.touchbit.buggy.core.testng.listeners;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.testng.*;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.config.PrimaryConfig;
import org.touchbit.buggy.core.exceptions.CorruptedTestException;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.utils.BuggyUtils;
import org.touchbit.buggy.core.utils.StringUtils;
import org.touchbit.buggy.core.utils.log.BuggyLog;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static org.testng.ITestResult.*;
import static org.touchbit.buggy.core.model.Status.EXP_FIX;
import static org.touchbit.buggy.core.model.Status.EXP_IMPL;
import static org.touchbit.buggy.core.utils.BuggyUtils.CONSOLE_DELIMITER;
import static org.touchbit.buggy.core.utils.IOHelper.copyFile;
import static org.touchbit.buggy.core.utils.StringUtils.*;

/**
 * Listener for processing executable tests.
 *
 * Created by Shaburov Oleg on 31.07.2017.
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "WeakerAccess", "squid:S2629"})
public class BuggyExecutionListener extends BaseBuggyExecutionListener
        implements IExecutionListener, IInvokedMethodListener, ISuiteListener, ITestListener, IClassListener {

    private static ThreadLocal<Integer> stepNum = new ThreadLocal<>();
    private static ThreadLocal<List<String>> steps = new ThreadLocal<>();

    public BuggyExecutionListener() { }

    public BuggyExecutionListener(Logger testLogger, Logger frameworkLogger, Logger consoleLogger) {
        testLog = testLogger;
        frameworkLog = frameworkLogger;
        consoleLog = consoleLogger;
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
        finalization();
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
            Buggy.incrementBuggyWarns();
            frameworkLog.warn("There are no playback steps in the test method {}", method.getName());
        }
        testLog.info("Date: {}", new Date());
    }

    @Override
    public void onAfterClass(ITestClass iTestClass) {
        if (getSuite(iTestClass) == null) {
            Buggy.incrementBuggyErrors();
            frameworkLog.error("There is no @Suite annotation for the test class: {}", iTestClass.getName());
        }
    }

    @Override
    public void onStart(ISuite suite) {
        disableTestsByType(suite);
        // If you do not expect to run all tests (including errors) -> disable the launch of the falling tests.
        if (!Buggy.getPrimaryConfig().isForceRun()) {
            disableTestsByStatus(suite);
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        // This method is invoked after the SuiteRunner has run all the getTestLog suites.
        copyExpectedTestLogFiles(suite);
        copyFixedTestLogFiles(suite);
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        initSteps();
        String methodName = getMethodName(method);
        if (testResult.getStatus() == ITestResult.STARTED) {
            if (method.isTestMethod()) {
                BuggyLog.setTestName(getTestMethodLogFileName(method));
                testLog.info("The test method is running:\n{} - {}", methodName, getDescription(method));
            } else {
                BuggyLog.setTestName(getTestMethodLogFileName(method));
                testLog.info("The configuration method is running:\n{} - {}.", methodName, getDescription(method));
                if (testLog.isDebugEnabled()) {
                    StringJoiner sj = new StringJoiner("\n", "\n", "\n");
                    for (Annotation annotation : getRealMethod(method).getAnnotations()) {
                        sj.add(annotation.toString());
                    }
                    testLog.debug("Declared method annotations:{}", sj);
                }
            }
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        Throwable throwable = testResult.getThrowable();
        String methodName = getMethodName(method);
        if (throwable != null && method.isTestMethod()) {
            List<String> stepList = getSteps();
            if (stepList != null && !stepList.isEmpty()) {
                int lastIndex = stepList.size() - 1;
                stepList.set(lastIndex, stepList.get(lastIndex) + " - ERROR");
                setSteps(stepList);
            }
            testLog.error("Execution of {} resulted in an error.", methodName, throwable);
        }
        if (method.isTestMethod()) {
            testCount.incrementAndGet();
            Details details = getDetails(method);
            if (details != null) {
                processTestMethodResult(method, testResult, details);
            } else {
                Buggy.incrementBuggyWarns();
                frameworkLog.warn("The test method {} does not contain the @Details annotation", methodName);
            }
        } else {
            processConfigurationMethodResult(method, testResult);
        }

    }

    /**
     * Method for the separation of steps in the test log.
     */
    public static void step(@NotNull final Logger logger, @NotNull final String msg, @NotNull final Object... args) {
        if (stepNum.get() == null) {
            initSteps();
        }
        int step = stepNum.get() + 1;
        String msgBody = msg;
        for (Object s : args) {
            msgBody = msgBody.replaceFirst("\\{}", String.valueOf(s));
        }
        String stepText = "Step " + step + ". " + msgBody;
        logger.info(" -----------\u2B9E {}", stepText);
        steps.get().add(stepText);
        stepNum.set(step);
    }

    public static void initSteps() {
        stepNum.set(0);
        steps.set(new ArrayList<>());
    }

    public static List<String> getSteps() {
        return steps.get();
    }

    public static void setSteps(List<String> stepList) {
        if (stepList == null) {
            steps.set(new ArrayList<>());
        } else {
            steps.set(stepList);
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
                        resultLog(method, EXP_FIX, buildDetailsMessage(details));
                        break;
                    case EXP_IMPL:
                        resultLog(method, EXP_IMPL, buildDetailsMessage(details));
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
                Buggy.incrementBuggyErrors();
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
                Buggy.incrementBuggyErrors();
                frameworkLog.error("Received unresolved status of configuration method [{}]. Status: {}",
                        methodName, testResult.getStatus());
        }
    }

    public void disableTestsByStatus(ISuite suite) {
        List<ITestNGMethod> methods = suite.getAllMethods();
        methods.forEach(method -> {
            Details details = getDetails(method);
            if (method.isTest()) {
                if (details != null && method.getInvocationCount() > 0) {
                    switch(details.status()) {
                        case EXP_FIX:
                        case EXP_IMPL:
                        case BLOCKED:
                        case CORRUPTED:
                            method.setInvocationCount(0);
                            resultLog(method, details.status(), buildDetailsMessage(details, "forced run disabled"));
                            break;
                        default:
                            // do nothing
                    }
                } else {
                    Buggy.incrementBuggyWarns();
                    frameworkLog.warn("The test method {} does not contain the @Details annotation",
                            method.getMethodName());
                }
            }
        });
    }

    public void disableTestsByType(final ISuite suite) {
        final List<ITestNGMethod> methods = suite.getAllMethods();
        methods.forEach(method -> {
            Details details = getDetails(method);
            Type configType = Buggy.getPrimaryConfig().getType();
            if (details != null && !configType.isIncludeOrEquals(details.type())) {
                method.setInvocationCount(0);
                resultLog(method, Status.SKIP, buildDetailsMessage(details, details.type(), "test type"));
            }
        });
    }

    public void copyFixedTestLogFiles(ISuite suite) {
        PrimaryConfig c = Buggy.getPrimaryConfig();
        suite.getAllInvokedMethods().stream()
                .filter(m -> m.getTestResult().getStatus() == SUCCESS)
                .forEach(m -> {
                    try {
                        Details details = getDetails(m);
                        if (m.isTestMethod() && details != null) {
                            if (details.status().equals(EXP_FIX)) {
                                copyFile(new File(c.getTestLogDir(), getTestMethodLogFileName(m)),
                                        new File(c.getFixedLogDir(), getTestMethodLogFileName(m)));
                            } else if (details.status().equals(EXP_IMPL)) {
                                copyFile(new File(c.getTestLogDir(), getTestMethodLogFileName(m)),
                                        new File(c.getImplementedLogDir(), getTestMethodLogFileName(m)));
                            }
                        }
                    } catch (IOException e) {
                        Buggy.incrementBuggyErrors();
                        frameworkLog.error(e.getMessage(), e);
                    }
                });
    }

    public void copyExpectedTestLogFiles(ISuite suite) {
        suite.getAllInvokedMethods().stream()
                .filter(m -> m.getTestResult().getStatus() == FAILURE ||
                        m.getTestResult().getStatus() == SUCCESS_PERCENTAGE_FAILURE)
                .forEach(m -> {
                    try {
                        if (m.isTestMethod()) {
                            copyTestMethodLogFle(m);
                        } else {
                            copyConfigurationMethodLogFle(m);
                        }
                    } catch (IOException e) {
                        Buggy.incrementBuggyErrors();
                        frameworkLog.error(e.getMessage(), e);
                    }
                });
    }

    public void copyTestMethodLogFle(IInvokedMethod method) throws IOException {
        PrimaryConfig c = Buggy.getPrimaryConfig();
        Details details = getDetails(method);
        if (details != null) {
            switch (details.status()) {
                case CORRUPTED:
                    copyFile(new File(c.getTestLogDir(), getTestMethodLogFileName(method)),
                            new File(c.getCorruptedErrorLogDir(), getTestMethodLogFileName(method)));
                    break;
                case EXP_FIX:
                    copyFile(new File(c.getTestLogDir(), getTestMethodLogFileName(method)),
                            new File(c.getExpFixErrorLogDir(), getTestMethodLogFileName(method)));
                    break;
                case EXP_IMPL:
                    copyFile(new File(c.getTestLogDir(), getTestMethodLogFileName(method)),
                            new File(c.getExpImplErrorLogDir(), getTestMethodLogFileName(method)));
                    break;
                case BLOCKED:
                    copyFile(new File(c.getTestLogDir(), getTestMethodLogFileName(method)),
                            new File(c.getBlockedErrorLogDir(), getTestMethodLogFileName(method)));
                    break;
                default:
                    if (method.getTestResult().getStatus() == FAILURE ||
                            method.getTestResult().getStatus() == SUCCESS_PERCENTAGE_FAILURE) {
                        copyFile(new File(c.getTestLogDir(), getTestMethodLogFileName(method)),
                                new File(c.getNewErrorLogDir(), getTestMethodLogFileName(method)));
                    }
            }
        }
    }

    public void copyConfigurationMethodLogFle(IInvokedMethod method) throws IOException {
        PrimaryConfig c = Buggy.getPrimaryConfig();
        String fileName = "conf_" + getClassSimpleName(method) + "." + getMethodName(method) + "().log";
        copyFile(new File(c.getTestLogDir(), fileName), new File(c.getNewErrorLogDir(), fileName));
    }

    public void resultLog(ITestNGMethod method, Status status, String msg) {
        String methodName = method.getMethodName();
        Suite suite = getSuite(method);
        String statusName = status.name();
        if (Buggy.getPrimaryConfig().isPrintSuite()) {
            StringJoiner sj = new StringJoiner(" ", " \u2B9E [", "] ");
            sj.add(BuggyUtils.getService(suite).getName().trim());
            sj.add(BuggyUtils.getInterface(suite).getName().trim());
            sj.add(suite.task().trim());
            msg = sj + msg;
        }
        testLog.info("{} - {} {}", methodName, statusName, method.getDescription());
        if (Buggy.getPrimaryConfig().isPrintLogFile() && method.getInvocationCount() > 0) {
            msg = msg + getURLEncodedLogFileName(method);
        }
        if (Buggy.getPrimaryConfig().isPrintCause()) {
            printASCIIStatus(status, StringUtils.dotFiller(methodName, 47, statusName) + msg);
        } else {
            printASCIIStatus(status, StringUtils.dotFiller(methodName, 47, statusName));
        }
        if (method.getCurrentInvocationCount() > 0) {
            increment(status);
        }
    }

    public void printASCIIStatus(Status status, String msg) {
        switch (status) {
            case FAILED:
                consoleLog.error(msg);
                break;
            case CORRUPTED:
                consoleLog.error(msg);
                break;
            case EXP_IMPL:
                consoleLog.warn(msg);
                break;
            case EXP_FIX:
                consoleLog.warn(msg);
                break;
            case BLOCKED:
                consoleLog.warn(msg);
                break;
            case SKIP:
                consoleLog.warn(msg);
                break;
            case SUCCESS:
                consoleLog.info(msg);
                break;
            case IMPLEMENTED:
                consoleLog.debug(msg);
                break;
            case FIXED:
                consoleLog.debug(msg);
                break;
            default:
                // do nothing
        }
    }

    private void increment(Status status) {
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
            default:
                // do nothing
        }
    }

    private void finalization() {
        println(CONSOLE_DELIMITER);
        if (Buggy.getBuggyErrors() > 0) {
            consoleLog.error(StringUtils.dotFiller("Framework errors", 47, Buggy.getBuggyErrors()));
        }
        if (Buggy.getBuggyWarns() > 0) {
            consoleLog.warn(StringUtils.dotFiller("Framework warns", 47, Buggy.getBuggyWarns()));
        }
        if (Buggy.getBuggyErrors() > 0 || Buggy.getBuggyWarns() > 0) {
            println(CONSOLE_DELIMITER);
        }
        int fixedCount = fixed.get() + implemented.get();
        int errorCount = expFixError.get() + expImplError.get() + newError.get() +
                corruptedError.get() + blockedError.get();
        consoleLog.info(StringUtils.dotFiller("Total tests run", 47, testCount.get()));
        consoleLog.info(StringUtils.dotFiller("Successful tests", 47, testCount.get() - errorCount));
        if (errorCount > 0 ) {
            checkWarnAndPrint("Failed tests", errorCount);
            checkErrorAndPrint("New Errors", newError.get());
            println(CONSOLE_DELIMITER);
            checkWarnAndPrint("Waiting to fix a defect", expFixError.get());
            checkWarnAndPrint("Waiting for implementation", expImplError.get());
            checkWarnAndPrint("Blocked tests", blockedError.get());
            checkErrorAndPrint("Corrupted tests", corruptedError.get());
        } else {
            String result = StringUtils.dotFiller("Errors", 47, errorCount);
            consoleLog.info(result);
        }
        if (fixedCount > 0) {
            println(CONSOLE_DELIMITER);
            checkDebugAndPrint("Fixed cases", fixed.get());
            checkDebugAndPrint("Implemented cases", implemented.get());
        }
        println(CONSOLE_DELIMITER);
        consoleLog.info(StringUtils.dotFiller("Execution time", 47, org.apache.commons.lang3.time.DurationFormatUtils
                .formatDuration(finishTime - startTime, "HH:mm:ss,SSS")));
        println(CONSOLE_DELIMITER);
    }

    private void checkDebugAndPrint(String msg, int count) {
        String result = StringUtils.dotFiller(msg, 47, count);
        if (count > 0) {
            consoleLog.debug(result);
        } else {
            consoleLog.info(result);
        }
    }

    private void checkErrorAndPrint(String msg, int count) {
        String result = StringUtils.dotFiller(msg, 47, count);
        if (count > 0) {
            consoleLog.error(result);
        } else {
            consoleLog.info(result);
        }
    }

    private void checkWarnAndPrint(String msg, int count) {
        String result = StringUtils.dotFiller(msg, 47, count);
        if (count > 0) {
            consoleLog.warn(result);
        } else {
            consoleLog.info(result);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void checkTraceAndPrint(String msg, int count) {
        String result = StringUtils.dotFiller(msg, 47, count);
        if (count > 0) {
            consoleLog.trace(result);
        } else {
            consoleLog.info(result);
        }
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
