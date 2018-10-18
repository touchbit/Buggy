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

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.testng.*;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.config.PrimaryConfig;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.utils.StringUtils;
import org.touchbit.buggy.core.utils.log.BuggyLog;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
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
    private String arrow = " \u2B9E ";

    protected Logger testLog;
    protected Logger frameworkLog;
    protected Logger consoleLog;

    @Override
    public void onExecutionStart() {
        startTime = new Date().getTime();
        if (testLog == null) {
            testLog = BuggyLog.test();
        }
        if (frameworkLog == null) {
            frameworkLog = BuggyLog.framework();
        }
        if (consoleLog == null) {
            consoleLog = BuggyLog.console();
        }
    }

    @Override
    public void onExecutionFinish() {
        finishTime = new Date().getTime();
    }

    public void setArrow(String a) {
        arrow = a;
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

    protected String getURLEncodedLogFilePath(ITestNGMethod method) {
        PrimaryConfig c = Buggy.getPrimaryConfig();
        String urlEncoded = StringUtils.encode(getInvokedMethodLogFileName(method));
        // Do not change the check. Feature parsing values by jCommander library.
        if (!"null".equalsIgnoreCase(String.valueOf(c.getArtifactsUrl()))) {
            String parentDir = c.getTestLogDir().getParentFile().getName();
            String logDir = new File(parentDir, c.getTestLogDir().getName()).getPath();
            String url = c.getArtifactsUrl().endsWith("/") ? c.getArtifactsUrl() : c.getArtifactsUrl() + "/";
            return arrow + url + new File(logDir, urlEncoded).getPath();
        } else {
            return arrow + "file://" + new File(c.getTestLogDir(), urlEncoded);
        }
    }

    protected String getInvokedMethodLogFileName(IInvokedMethod method) {
        return getInvokedMethodLogFileName(method.getTestMethod());
    }

    protected String getInvokedMethodLogFileName(ITestNGMethod iTestNGMethod) {
        Method method = iTestNGMethod.getConstructorOrMethod().getMethod();
        String caseIds = "";
        String methodName = method.getName();
        if (!iTestNGMethod.isTest()) {
            return methodName + ".log";
        }
        Details details = getDetails(method);
        if (details != null) {
            List<Integer> ids = new ArrayList<>();
            for (int id : details.id()) {
                if (id > 0) {
                    ids.add(id);
                }
            }
            if (!ids.isEmpty()) {
                caseIds = Arrays.toString(details.id()) + "_";
            }
        }
        return caseIds + methodName + ".log";
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
        return Arrays.toString(details.issue());
    }

    protected String buildDetailsMessage(Details details, Object... appends) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        for (Object o : appends) {
            stringJoiner.add(String.valueOf(o));
        }
        String ref = isIssuesPresent(details) ? arrow + getIssues(details).trim() : "";
        String appendsResult = stringJoiner.toString().trim();
        if (ref.length() != 0) {
            return ref + (appendsResult.length() > 0 ? " " + appendsResult : "");
        }
        return stringJoiner.length() != 0 ? arrow + stringJoiner : "";
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
