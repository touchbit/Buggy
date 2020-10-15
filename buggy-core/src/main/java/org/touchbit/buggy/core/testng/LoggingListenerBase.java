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

import org.testng.*;
import org.touchbit.buggy.core.config.BuggyConfigurationYML;
import org.touchbit.buggy.core.config.OutputRule;
import org.touchbit.buggy.core.logback.ConfLogger;
import org.touchbit.buggy.core.logback.SiftingTestLogger;
import org.touchbit.buggy.core.model.*;
import org.touchbit.buggy.core.utils.ANSI;
import org.touchbit.buggy.core.utils.JUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

import static org.touchbit.buggy.core.model.ResultStatus.SKIP;
import static org.touchbit.buggy.core.model.ResultStatus.SUCCESS;
import static org.touchbit.buggy.core.utils.ANSI.*;

/**
 * Listener for processing executable tests.
 * <p>
 * Created by Shaburov Oleg on 31.07.2017.
 */
public abstract class LoggingListenerBase implements BuggyListener, IInvokedMethodListener, ISuiteListener {

    protected static final SiftingTestLogger TEST = new SiftingTestLogger();

    protected static final Set<ITestNGMethod> DISABLED_METHODS = new HashSet<>();

    @Override
    public void onStart(ISuite suite) {
        suite.getAllMethods().forEach(this::skippedTestsByType);
        suite.getAllMethods().forEach(this::skippedByTestStatus);
        suite.getAllMethods().forEach(this::skippedByTestAnnotation);
    }

    protected void skippedTestsByType(ITestNGMethod method) {
        if (isSkipByType(method) && !DISABLED_METHODS.contains(method)) {
            DISABLED_METHODS.add(method);
            String placeholder = ConfLogger.getDotPlaceholder(method.getMethodName(), SKIP);
            String msg = YELLOW.wrap(placeholder);
            Buggy buggy = getBuggyAnnotation(method);
            msg += " by test types: " + Arrays.toString(Objects.requireNonNull(buggy).types());
            ConfLogger.info(msg);
        }
    }

    protected void skippedByTestStatus(ITestNGMethod method) {
        if (isSkipByTestStatus(method) && !DISABLED_METHODS.contains(method)) {
            DISABLED_METHODS.add(method);
            String placeholder = ConfLogger.getDotPlaceholder(method.getMethodName(), SKIP);
            String msg = YELLOW.wrap(placeholder);
            Buggy buggy = getBuggyAnnotation(method);
            msg += " by status: " + Objects.requireNonNull(buggy).status();
            ConfLogger.info(msg);
        }
    }

    protected void skippedByTestAnnotation(ITestNGMethod method) {
        assertNotNull(method);
        if (!method.getEnabled()) {
            String placeholder = ConfLogger.getDotPlaceholder(method.getMethodName(), SKIP);
            String msg = YELLOW.wrap(placeholder);
            msg += " by @Test annotation.";
            ConfLogger.info(msg);
        }
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        SiftingTestLogger.setTestLogFileName(method);
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        TEST.steps();
        ResultStatus resultStatus = getResultStatus(method);
        SiftingTestLogger.setTestResultStatus(resultStatus);
        String methodName = getMethodName(method);
        Throwable throwable = testResult.getThrowable();
        if (throwable != null) {
            TEST.error("{}: {}", throwable.getClass().getSimpleName(), throwable.getMessage());
        }
        if (hasBuggyAnnotation(method) && hasSuiteAnnotation(method) && method.isTestMethod()) {
            Buggy buggy = getBuggyAnnotation(method);
            Suite suite = getSuiteAnnotation(method);
            String component = JUtils.getGoal(Suite::component, suite).getName().trim();
            String service = JUtils.getGoal(Suite::service, suite).getName().trim();
            String interfaze = JUtils.getGoal(Suite::interfaze, suite).getName().trim();
            //noinspection ConstantConditions
            String purpose = suite.purpose().trim();
            //noinspection ConstantConditions
            Status status = buggy.status();
            Type[] types = buggy.types();
            String[] ids = buggy.caseIDs();
            String[] issues = buggy.issues();
            String[] bugs = buggy.bugs();
            String indent = " ";
            String dotPlaceholder = ConfLogger.getDotPlaceholder(methodName, resultStatus, getANSI(resultStatus));
            if (BuggyConfigurationYML.getOutputRule().equals(OutputRule.OFF)) {
                ConfLogger.info(dotPlaceholder);
                return;
            }
            if (BuggyConfigurationYML.getOutputRule().equals(OutputRule.UNSUCCESSFUL) && resultStatus == SUCCESS) {
                ConfLogger.info(dotPlaceholder);
                return;
            }
            StringBuilder message = new StringBuilder();
            message.append(dotPlaceholder);
            if (BuggyConfigurationYML.getTestSuiteInfo()) {
                message.append(" [")
                        .append(component).append(" ")
                        .append(service).append(" ")
                        .append(interfaze);
                if (!purpose.isEmpty()) {
                    message.append(" ").append(purpose);
                }
                message.append("]");
            }
            if (BuggyConfigurationYML.getTestCaseTitle() && hasDescription(method)) {
//                message.append("\n").append(" § ").append(description);
                message.append("\n  Case:  ").append(getDescription(method));
                indent += indent;
            }
            if (BuggyConfigurationYML.getTestLogFilePath()) {
                message.append("\n  Log:   ").append(getLogFilePath());
                indent += indent;
            }
            if (BuggyConfigurationYML.getTestBugsInfo() && bugs.length > 0) {
                if (BuggyConfigurationYML.getIssuesUrl() != null && !BuggyConfigurationYML.getIssuesUrl().isEmpty()) {
                    for (String bug : bugs) {
//                        message.append("\n  └").append(BOLD.wrap(RED.wrap(" ❗ ")))
                        message.append("\n  Bug:   ").append(BuggyConfigurationYML.getIssuesUrl()).append(bug);
                    }
                } else {
                    message.append(" ").append(Arrays.toString(bugs));
                }
            }
            if (BuggyConfigurationYML.getTestIssuesInfo()) {
                if (BuggyConfigurationYML.getIssuesUrl() != null && !BuggyConfigurationYML.getIssuesUrl().isEmpty()) {
                    for (String bug : bugs) {
//                        message.append("\n  └").append(BOLD.wrap(GREEN.wrap(" ✓ ")))
                        message.append("\n  Issue: ").append(BuggyConfigurationYML.getIssuesUrl()).append(bug);
                    }
                } else {
                    message.append(" ").append(Arrays.toString(bugs));
                }
            }

            if (BuggyConfigurationYML.getTestErrorInfo() && throwable != null) {
//                message.append("\n  └ ").append(BOLD.wrap(RED.wrap("↯ ")))
                message.append("\n  ")
                        .append(RED.wrap(throwable.getClass().getSimpleName()))
                        .append(": ")
                        .append(throwable.getMessage());
            }
            ConfLogger.info(message.append("\n").toString());
        }
        TEST.info("Date: {}", new Date());
    }

    protected ANSI getANSI(ResultStatus status) {
        assertNotNull(status);
        switch (status) {
            case SUCCESS:
                return NONE;
            case FIXED:
            case IMPLEMENTED:
                return GREEN;
            case FAILED:
            case CORRUPTED:
                return RED;
            case SKIP:
                return YELLOW;
            case BLOCKED:
            case EXP_FIX:
            case EXP_IMPL:
            default:
                return PURPLE;
        }
    }

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

}
