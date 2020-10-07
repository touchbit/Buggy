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

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.testng.*;
import org.touchbit.buggy.core.config.BuggyConfig;
import org.touchbit.buggy.core.logback.ConsoleLogger;
import org.touchbit.buggy.core.logback.FrameworkLogger;
import org.touchbit.buggy.core.logback.SiftingTestLogger;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.IStatus;
import org.touchbit.buggy.core.model.Suite;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Oleg Shaburov on 16.05.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseBuggyExecutionListener implements BuggyListener, IExecutionListener {

    protected static AtomicInteger testCount = new AtomicInteger(0);
    protected static AtomicInteger skippedTests = new AtomicInteger(0);
    protected static AtomicInteger corruptedError = new AtomicInteger(0);
    protected static AtomicInteger expFixError = new AtomicInteger(0);
    protected static AtomicInteger expImplError = new AtomicInteger(0);
    protected static AtomicInteger blockedError = new AtomicInteger(0);
    protected static AtomicInteger newError = new AtomicInteger(0);
    protected static AtomicInteger fixed = new AtomicInteger(0);
    protected static AtomicInteger implemented = new AtomicInteger(0);

    protected long startTime;
    protected long finishTime;
    protected Logger testLog;
    protected Logger frameworkLog;
    protected Logger consoleLog;

    @Override
    public void onExecutionStart() {
        startTime = new Date().getTime();
        if (testLog == null) {
            testLog = new SiftingTestLogger();
        }
        if (frameworkLog == null) {
            frameworkLog = new FrameworkLogger();
        }
        if (consoleLog == null) {
            consoleLog = new ConsoleLogger();
        }
    }

    @Override
    public void onExecutionFinish() {
        finishTime = new Date().getTime();
    }

    protected @Nullable Details getDetails(IInvokedMethod method) {
        return getDetails(method.getTestMethod());
    }

    protected @Nullable Details getDetails(ITestNGMethod method) {
        return getDetails(method.getConstructorOrMethod().getMethod());
    }

    protected @Nullable Details getDetails(Method method) {
        return method.getAnnotation(Details.class);
    }

    protected Suite getSuite(ITestClass iTestClass) {
        return iTestClass.getRealClass().getAnnotation(Suite.class);
    }

    protected Suite getSuite(ITestNGMethod method) {
        return (Suite) method.getRealClass().getAnnotation(Suite.class);
    }

    protected String getLogFilePath(ITestNGMethod method, IStatus status) {
        // Do not change the check. Feature parsing values by jCommander library.
        if (!"null".equalsIgnoreCase(String.valueOf(BuggyConfig.getArtifactsUrl()))) {
            return BuggyConfig.getArtifactsUrl().endsWith("/") ?
                    BuggyConfig.getArtifactsUrl() :
                    BuggyConfig.getArtifactsUrl() + "/";
        } else {
            String fileName = getInvokedMethodLogFileName(method);
            SiftingTestLogger.setTestStatus(fileName, status);
            File file = SiftingTestLogger.getSiftingLogFile(fileName);
            if (file != null) {
                return "file://" + file.getAbsolutePath();
            } else {
                return "Log file not found: " + fileName;
            }
        }
    }

    protected String getInvokedMethodLogFileName(IInvokedMethod method) {
        return getInvokedMethodLogFileName(method.getTestMethod());
    }

    protected String getInvokedMethodLogFileName(ITestNGMethod iTestNGMethod) {
        Method method = iTestNGMethod.getConstructorOrMethod().getMethod();
        return method.getName() + ".log";
    }

    protected Method getRealMethod(ITestResult result) {
        return getRealMethod(result.getMethod());
    }

    protected Method getRealMethod(IInvokedMethod method) {
        return getRealMethod(method.getTestMethod());
    }

    protected Method getRealMethod(ITestNGMethod method) {
        return method.getConstructorOrMethod().getMethod();
    }

    protected String getDescription(IInvokedMethod method) {
        return method.getTestMethod().getDescription();
    }

    protected String getClassSimpleName(IInvokedMethod method) {
        return method.getTestMethod().getRealClass().getSimpleName();
    }

    protected String getMethodName(IInvokedMethod method) {
        return method.getTestMethod().getMethodName();
    }

    protected boolean isIssuesPresent(Details details) {
        for (String s : details.issue()) {
            if (!s.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    protected String getIssues(Details details) {
        String[] issues = details.issue();
        if (issues.length == 0) {
            return "";
        }
        return Arrays.toString(details.issue());
    }

    protected String buildDetailsMessage(Details details, Object... appends) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        for (Object o : appends) {
            stringJoiner.add(String.valueOf(o));
        }
        String ref = isIssuesPresent(details) ? "" + getIssues(details).trim() : "";
        String appendsResult = stringJoiner.toString().trim();
        if (ref.length() != 0) {
            return ref + (appendsResult.length() > 0 ? " " + appendsResult.trim() : "");
        }
        return stringJoiner.length() != 0 ? stringJoiner.toString().trim() : "";
    }

    public int getTestCount() {
        return testCount.get();
    }

    public int getSkippedTests() {
        return skippedTests.get();
    }

    public int getCorruptedError() {
        return corruptedError.get();
    }

    public int getExpFixError() {
        return expFixError.get();
    }

    public int getExpImplError() {
        return expImplError.get();
    }

    public int getBlockedError() {
        return blockedError.get();
    }

    public int getNewError() {
        return newError.get();
    }

    public int getFixed() {
        return fixed.get();
    }

    public int getImplemented() {
        return implemented.get();
    }

}
