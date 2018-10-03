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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.testng.*;
import org.testng.internal.ConstructorOrMethod;
import org.touchbit.buggy.core.BaseUnitTest;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.exceptions.ExpectedImplementationException;
import org.touchbit.buggy.core.indirect.TestInterface;
import org.touchbit.buggy.core.indirect.TestService;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.model.Type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.slf4j.helpers.NOPLogger.NOP_LOGGER;
import static org.touchbit.buggy.core.model.Status.*;
import static org.touchbit.buggy.core.model.Type.*;

/**
 * Created by Oleg Shaburov on 21.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("BuggyExecutionListener class tests")
class BuggyExecutionListenerTests extends BaseUnitTest {

    private static final Logger LOG = NOP_LOGGER;

    @Test
    @DisplayName("Check BuggyExecutionListener()")
    void unitTest_20180921144349() {
        BuggyExecutionListener listener = new BuggyExecutionListener();
        listener.onExecutionStart();
        listener.onExecutionFinish();
        assertThat(listener.isEnable(), is(true));
    }

    @Test
    @DisplayName("Check BuggyExecutionListener(null, null, null)")
    void unitTest_20180921144529() {
        BuggyExecutionListener listener = new BuggyExecutionListener(null, null, null);
        listener.onExecutionStart();
        listener.onExecutionFinish();
        assertThat(listener.isEnable(), is(true));
    }

    @Test
    @DisplayName("Check BuggyExecutionListener(LOG, LOG, LOG)")
    void unitTest_20180921144624() {
        BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
        listener.onExecutionStart();
        listener.onExecutionFinish();
        assertThat(listener.isEnable(), is(true));
    }

    @Test
    @DisplayName("Check 'do nothing' methods")
    void unitTest_20180921145653() {
        BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
        ITestContext iTestContext = mock(ITestContext.class);
        ITestClass iTestClass = mock(ITestClass.class);
        ITestResult iTestResult = mock(ITestResult.class);
        listener.onStart(iTestContext);
        listener.onFinish(iTestContext);
        listener.onBeforeClass(iTestClass);
        listener.onTestStart(iTestResult);
    }

    @Test
    @DisplayName("Check onTestSuccess(ITestResult result)")
    void unitTest_20180921150511() {
        BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
        ITestResult iTestResult = getMockITestResult(1);
        listener.onTestSuccess(iTestResult);
    }

    @Test
    @DisplayName("Check onTestFailure(ITestResult result)")
    void unitTest_20180921150916() {
        BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
        ITestResult iTestResult = getMockITestResult(2);
        listener.onTestFailure(iTestResult);
    }

    @Test
    @DisplayName("Check onTestSkipped(ITestResult result)")
    void unitTest_20180921151028() {
        BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
        ITestResult iTestResult = getMockITestResult(3);
        listener.onTestSkipped(iTestResult);
    }

    @Test
    @DisplayName("Check onTestFailedButWithinSuccessPercentage(ITestResult result)")
    void unitTest_20180921151130() {
        BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
        ITestResult iTestResult = getMockITestResult(4);
        listener.onTestFailedButWithinSuccessPercentage(iTestResult);
    }

    @Test
    @DisplayName("Check onTestFinish with ExpectedImplementationException")
    void unitTest_20180921182848() {
        BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
        ITestResult iTestResult = getMockITestResult(4);
        when(iTestResult.getThrowable()).thenReturn(new ExpectedImplementationException(""));
        listener.onTestFinish(iTestResult);
    }

    @Test
    @DisplayName("Check onTestFinish with Exception")
    void unitTest_20180921183436() {
        BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
        ITestResult iTestResult = getMockITestResult(4);
        when(iTestResult.getThrowable()).thenReturn(new Exception(""));
        listener.onTestFinish(iTestResult);
    }

    @Test
    @DisplayName("Check onTestFinish with null steps")
    void unitTest_20180921183125() {
        BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
        ITestResult iTestResult = getMockITestResult(1);
        BuggyExecutionListener.setSteps(null);
        listener.onTestFinish(iTestResult);
    }

    @Test
    @DisplayName("Check onTestFinish with empty steps")
    void unitTest_20180921183228() {
        BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
        ITestResult iTestResult = getMockITestResult(1);
        BuggyExecutionListener.setSteps(new ArrayList<String>() {{ add("steppp"); }});
        listener.onTestFinish(iTestResult);
    }

    @Test
    @DisplayName("Check onAfterClass(ITestClass iTestClass) with Suite")
    void unitTest_20180921183611() {
        BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
        ITestClass iTestClass = getMockITestClass(MockITestClass.class);
        BuggyExecutionListener.setSteps(new ArrayList<>());
        listener.onAfterClass(iTestClass);
    }

    @Test
    @DisplayName("Check onAfterClass(ITestClass iTestClass) without Suite")
    void unitTest_20180921184448() {
        BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
        ITestClass iTestClass = getMockITestClass(Object.class);
        BuggyExecutionListener.setSteps(new ArrayList<>());
        listener.onAfterClass(iTestClass);
    }

    @Test
    @DisplayName("Check onStart() if forceRun disabled")
    void unitTest_20180922083207() {
        Type configType = Buggy.getPrimaryConfig().getType();
        boolean isForceRun = Buggy.getPrimaryConfig().isForceRun();
        try {
            Buggy.getPrimaryConfig().setType(MODULE);
            Buggy.getPrimaryConfig().setForceRun(false);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(EXP_FIX, SMOKE));
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.onStart(suite);
            assertThat(listener.method, not(nullValue()));
            assertThat(listener.status, is(EXP_FIX));
            assertThat(listener.msg, is(" \u2B9E forced run disabled"));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        } finally {
            Buggy.getPrimaryConfig().setType(configType);
            Buggy.getPrimaryConfig().setForceRun(isForceRun);
        }
    }

    @Test
    @DisplayName("Check onStart() if forceRun enabled")
    void unitTest_20180922085022() {
        Type configType = Buggy.getPrimaryConfig().getType();
        boolean isForceRun = Buggy.getPrimaryConfig().isForceRun();
        try {
            Buggy.getPrimaryConfig().setType(MODULE);
            Buggy.getPrimaryConfig().setForceRun(true);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(EXP_FIX, SMOKE));
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.onStart(suite);
            assertThat(listener.method, is(nullValue()));
            assertThat(listener.status, is(nullValue()));
            assertThat(listener.msg, is(nullValue()));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        } finally {
            Buggy.getPrimaryConfig().setType(configType);
            Buggy.getPrimaryConfig().setForceRun(isForceRun);
        }
    }

    @Test
    @DisplayName("Check onFinish()")
    void unitTest_20180922085205() {
        IInvokedMethod method = getMockIInvokedMethod(true);
        List<IInvokedMethod> allMethods = new ArrayList<>();
        allMethods.add(method);
        ISuite suite = mock(ISuite.class);
        when(suite.getAllInvokedMethods()).thenReturn(allMethods);
        UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(EXP_FIX, SMOKE));
        listener.onFinish(suite);
    }

    @Nested()
    @DisplayName("beforeInvocation() tests")
    class BeforeInvocationTests extends BaseUnitTest {

        @Test
        @DisplayName("Check beforeInvocation() with STARTED status")
        void unitTest_20180921191220() {
            BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            ITestResult iTestResult = getMockITestResult(16);
            listener.beforeInvocation(iInvokedMethod, iTestResult);
        }

        @Test
        @DisplayName("Check Check beforeInvocation() with CREATED status")
        void unitTest_20180921191657() {
            BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            ITestResult iTestResult = getMockITestResult(-1);
            listener.beforeInvocation(iInvokedMethod, iTestResult);
        }

        @Test
        @DisplayName("Check beforeInvocation() is test method")
        void unitTest_20180921191731() {
            BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            ITestResult iTestResult = getMockITestResult(16);
            when(iInvokedMethod.isTestMethod()).thenReturn(true);
            listener.beforeInvocation(iInvokedMethod, iTestResult);
        }

        @Test
        @DisplayName("Check beforeInvocation() is no test method")
        void unitTest_20180921191829() {
            BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            ITestResult iTestResult = getMockITestResult(16);
            when(iInvokedMethod.isTestMethod()).thenReturn(false);
            listener.beforeInvocation(iInvokedMethod, iTestResult);
        }

        @Test
        @DisplayName("Check debug info")
        void unitTest_20180922090145() {
            Logger log = mock(Logger.class);
            when(log.isDebugEnabled()).thenReturn(true);
            BuggyExecutionListener listener = new BuggyExecutionListener(log, log, log);
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            ITestResult iTestResult = getMockITestResult(16);
            when(iInvokedMethod.isTestMethod()).thenReturn(false);
            listener.beforeInvocation(iInvokedMethod, iTestResult);
        }

    }

    @Nested()
    @DisplayName("afterInvocation() tests")
    class AfterInvocationTests extends BaseUnitTest {

        @Test
        @DisplayName("Check afterInvocation with exception")
        void unitTest_20180921220759() {
            BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            when(iInvokedMethod.isTestMethod()).thenReturn(true);
            ITestResult iTestResult = getMockITestResult(1);
            when(iTestResult.getThrowable()).thenReturn(new Exception());
            BuggyExecutionListener.step(LOG, "with exception");
            listener.afterInvocation(iInvokedMethod, iTestResult);
            assertThat(BuggyExecutionListener.getSteps(), contains("Step 1. with exception - ERROR"));
        }

        @Test
        @DisplayName("Check afterInvocation without exception")
        void unitTest_20180921221233() {
            BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            when(iInvokedMethod.isTestMethod()).thenReturn(true);
            ITestResult iTestResult = getMockITestResult(1);
            listener.afterInvocation(iInvokedMethod, iTestResult);
            listener.afterInvocation(iInvokedMethod, iTestResult);
            assertThat(BuggyExecutionListener.getSteps(), is(empty()));
        }

        @Test
        @DisplayName("Check afterInvocation without @Details")
        void unitTest_20180921222920() {
            BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod(MockITestClass.class,
                    "iTestResultMethodWithoutDetails", true);
            when(iInvokedMethod.isTestMethod()).thenReturn(true);
            ITestResult iTestResult = getMockITestResult(1);
            listener.afterInvocation(iInvokedMethod, iTestResult);
            listener.afterInvocation(iInvokedMethod, iTestResult);
            assertThat(BuggyExecutionListener.getSteps(), is(empty()));
        }

        @Test
        @DisplayName("Check afterInvocation configuration method")
        void unitTest_20180921224011() {
            BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            when(iInvokedMethod.isTestMethod()).thenReturn(false);
            listener.afterInvocation(iInvokedMethod, getMockITestResult(1));
            assertThat(BuggyExecutionListener.getSteps(), is(empty()));
        }

    }

    @Nested()
    @DisplayName("step() tests")
    class StepTests extends BaseUnitTest {

        @Test
        @DisplayName("Check step(LOG, \"msg\")")
        void unitTest_20180921192521() {
            BuggyExecutionListener.step(LOG, "msg");
            assertThat(BuggyExecutionListener.getSteps(), contains("Step 1. msg"));
        }

        @Test
        @DisplayName("Check step(LOG, \"msg {}\", \"with args\")")
        void unitTest_20180921192637() {
            BuggyExecutionListener.step(LOG, "msg {}", "with args");
            assertThat(BuggyExecutionListener.getSteps(), contains("Step 1. msg with args"));
        }

        @Test
        @DisplayName("Check ignoring step() ASC")
        void unitTest_20181004002108() {
            BuggyExecutionListener.step(LOG, "a");
            BuggyExecutionListener.step(LOG, "b");
            assertThat(BuggyExecutionListener.getSteps().toString(), is("[Step 1. a, Step 2. b]"));
        }

        @Test
        @DisplayName("Check ignoring step() DESC")
        void unitTest_20181004004254() {
            BuggyExecutionListener.step(LOG, "b");
            BuggyExecutionListener.step(LOG, "a");
            assertThat(BuggyExecutionListener.getSteps().toString(), is("[Step 1. b, Step 2. a]"));
        }

    }

    @Nested()
    @DisplayName("processTestMethodResult() tests")
    class ProcessTestMethodResultTests extends BaseUnitTest {

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details EXP_FIX Status with issues")
        void unitTest_20180922023815() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(EXP_FIX, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FIXED));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }
        
        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details EXP_FIX Status without issues")
        void unitTest_20180922031547() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(EXP_FIX));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FIXED));
            assertThat(listener.msg, is(isEmptyString()));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details BLOCKED Status with issues")
        void unitTest_20180922031825() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(BLOCKED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FIXED));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details BLOCKED Status without issues")
        void unitTest_20180922031943() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(BLOCKED));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FIXED));
            assertThat(listener.msg, is(isEmptyString()));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details EXP_IMPL Status with issues")
        void unitTest_20180922033333() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(EXP_IMPL, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(IMPLEMENTED));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details EXP_IMPL Status without issues")
        void unitTest_20180922033338() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(EXP_IMPL));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(IMPLEMENTED));
            assertThat(listener.msg, is(isEmptyString()));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details CORRUPTED Status with issues")
        void unitTest_20180922033427() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(CORRUPTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(CORRUPTED));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details CORRUPTED Status without issues")
        void unitTest_20180922033726() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(CORRUPTED));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(CORRUPTED));
            assertThat(listener.msg, is(isEmptyString()));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details FIXED Status with issues")
        void unitTest_20180922033959() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(FIXED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SUCCESS));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details IMPLEMENTED Status with issues")
        void unitTest_20180922034106() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(IMPLEMENTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SUCCESS));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details UNTESTED Status with issues")
        void unitTest_20180922034141() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(UNTESTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SUCCESS));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details SUCCESS Status with issues")
        void unitTest_20180922034318() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(SUCCESS, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SUCCESS));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details SKIP Status with issues")
        void unitTest_20180922034337() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(SKIP, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SUCCESS));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details FAILED Status with issues")
        void unitTest_20180922034357() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(FAILED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SUCCESS));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS_PERCENTAGE_FAILURE if Details EXP_FIX Status with issues")
        void unitTest_20180922034704() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS_PERCENTAGE_FAILURE),
                    getDetails(EXP_FIX, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(EXP_FIX));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details EXP_FIX Status with issues")
        void unitTest_20180922034924() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(EXP_FIX, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(EXP_FIX));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details CORRUPTED Status with issues")
        void unitTest_20180922035053() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(CORRUPTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(CORRUPTED));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details EXP_IMPL Status with issues")
        void unitTest_20180922035146() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(EXP_IMPL, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(EXP_IMPL));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details BLOCKED Status with issues")
        void unitTest_20180922035208() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(BLOCKED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(BLOCKED));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details FIXED Status with issues")
        void unitTest_20180922035238() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(FIXED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FAILED));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details IMPLEMENTED Status with issues")
        void unitTest_20180922035331() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(IMPLEMENTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FAILED));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details UNTESTED Status with issues")
        void unitTest_20180922035403() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(UNTESTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FAILED));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details SUCCESS Status with issues")
        void unitTest_20180922035419() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(SUCCESS, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FAILED));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details SKIP Status with issues")
        void unitTest_20180922035435() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(SKIP, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FAILED));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details FAILED Status with issues")
        void unitTest_20180922035529() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(FAILED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FAILED));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details EXP_IMPL Status with issues")
        void unitTest_20180922035630() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(EXP_IMPL, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details EXP_FIX Status with issues")
        void unitTest_20180922035830() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(EXP_FIX, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details BLOCKED Status with issues")
        void unitTest_20180922035902() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(BLOCKED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details CORRUPTED Status with issues")
        void unitTest_20180922035917() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(CORRUPTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details FIXED Status with issues")
        void unitTest_20180922035933() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(FIXED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details IMPLEMENTED Status with issues")
        void unitTest_20180922035950() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(IMPLEMENTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details UNTESTED Status with issues")
        void unitTest_20180922040014() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(UNTESTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details SUCCESS Status with issues")
        void unitTest_20180922040029() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(SUCCESS, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details SKIP Status with issues")
        void unitTest_20180922040042() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(SKIP, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details FAILED Status with issues")
        void unitTest_20180922040055() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(FAILED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is(" \u2B9E [JIRA-123] "));
        }

        @Test
        @DisplayName("Check ITestResult.CREATED is not unprocessed")
        void unitTest_20180922040117() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            int expErrors = Buggy.getBuggyErrors() + 1;
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.CREATED), getDetails(SUCCESS));
            assertThat(expErrors, is(Buggy.getBuggyErrors()));
            assertThat(listener.method, is(nullValue()));
            assertThat(listener.status, is(nullValue()));
            assertThat(listener.msg, is(nullValue()));
        }

        @Test
        @DisplayName("Check ITestResult.STARTED is not unprocessed")
        void unitTest_20180922040351() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod();
            int expErrors = Buggy.getBuggyErrors() + 1;
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.STARTED), getDetails(SUCCESS));
            assertThat(expErrors, is(Buggy.getBuggyErrors()));
            assertThat(listener.method, is(nullValue()));
            assertThat(listener.status, is(nullValue()));
            assertThat(listener.msg, is(nullValue()));
        }

    }

    @Nested()
    @DisplayName("processConfigurationMethodResult() tests")
    class ProcessConfigurationMethodResultTests extends BaseUnitTest {

        @Test
        @DisplayName("Check ITestResult.SUCCESS")
        void unitTest_20180922043219() {
            BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod(false);
            int expErrors = Buggy.getBuggyErrors();
            listener.processConfigurationMethodResult(method, getMockITestResult(ITestResult.SUCCESS));
            assertThat(Buggy.getBuggyErrors(), is(expErrors));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP")
        void unitTest_20180922061334() {
            BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod(false);
            int expErrors = Buggy.getBuggyErrors();
            listener.processConfigurationMethodResult(method, getMockITestResult(ITestResult.SKIP));
            assertThat(Buggy.getBuggyErrors(), is(expErrors));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE")
        void unitTest_20180922061357() {
            BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod(false);
            int expErrors = Buggy.getBuggyErrors();
            ITestResult result = getMockITestResult(ITestResult.FAILURE);
            when(result.getThrowable()).thenReturn(new Exception());
            listener.processConfigurationMethodResult(method, result);
            assertThat(Buggy.getBuggyErrors(), is(expErrors));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS_PERCENTAGE_FAILURE")
        void unitTest_20180922061412() {
            BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod(false);
            int expErrors = Buggy.getBuggyErrors();
            ITestResult result = getMockITestResult(ITestResult.SUCCESS_PERCENTAGE_FAILURE);
            when(result.getThrowable()).thenReturn(new Exception());
            listener.processConfigurationMethodResult(method, result);
            assertThat(Buggy.getBuggyErrors(), is(expErrors));
        }

        @Test
        @DisplayName("Check ITestResult.CREATED")
        void unitTest_20180922061437() {
            BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod(false);
            int expErrors = Buggy.getBuggyErrors() + 1;
            listener.processConfigurationMethodResult(method, getMockITestResult(ITestResult.CREATED));
            assertThat(Buggy.getBuggyErrors(), is(expErrors));
        }

        @Test
        @DisplayName("Check ITestResult.STARTED")
        void unitTest_20180922061516() {
            BuggyExecutionListener listener = new BuggyExecutionListener(LOG, LOG, LOG);
            IInvokedMethod method = getMockIInvokedMethod(false);
            int expErrors = Buggy.getBuggyErrors() + 1;
            listener.processConfigurationMethodResult(method, getMockITestResult(ITestResult.STARTED));
            assertThat(Buggy.getBuggyErrors(), is(expErrors));
        }

    }

    @Nested()
    @DisplayName("disableTestsByStatus() tests")
    class DisableTestsByStatusTests extends BaseUnitTest {

        @Test
        @DisplayName("Ignore disabling the test, if method is not contains the @Details annotation")
        void unitTest_20180922062020() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns() + 1;
            listener.disableTestsByStatus(suite);
            assertThat(listener.method, is(nullValue()));
            assertThat(listener.status, is(nullValue()));
            assertThat(listener.msg, is(nullValue()));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Ignore disabling the test, if method invocation count <= 0")
        void unitTest_20180922063213() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SUCCESS));
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            when(method.getInvocationCount()).thenReturn(0);
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns() + 1;
            listener.disableTestsByStatus(suite);
            assertThat(listener.method, is(nullValue()));
            assertThat(listener.status, is(nullValue()));
            assertThat(listener.msg, is(nullValue()));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Ignore disabling the test, if received configuration method")
        void unitTest_20180922063717() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SUCCESS));
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            when(method.isTest()).thenReturn(false);
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.disableTestsByStatus(suite);
            assertThat(listener.method, is(nullValue()));
            assertThat(listener.status, is(nullValue()));
            assertThat(listener.msg, is(nullValue()));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Ignore disabling the test, if expected Status.SUCCESS")
        void unitTest_20180922063942() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SUCCESS));
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            when(method.isTest()).thenReturn(true);
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.disableTestsByStatus(suite);
            assertThat(listener.method, is(nullValue()));
            assertThat(listener.status, is(nullValue()));
            assertThat(listener.msg, is(nullValue()));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Ignore disabling the test, if expected Status.FIXED")
        void unitTest_20180922065558() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(FIXED));
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            when(method.isTest()).thenReturn(true);
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.disableTestsByStatus(suite);
            assertThat(listener.method, is(nullValue()));
            assertThat(listener.status, is(nullValue()));
            assertThat(listener.msg, is(nullValue()));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Ignore disabling the test, if expected Status.IMPLEMENTED")
        void unitTest_20180922070145() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(IMPLEMENTED));
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            when(method.isTest()).thenReturn(true);
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.disableTestsByStatus(suite);
            assertThat(listener.method, is(nullValue()));
            assertThat(listener.status, is(nullValue()));
            assertThat(listener.msg, is(nullValue()));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Ignore disabling the test, if expected Status.UNTESTED")
        void unitTest_20180922070204() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(UNTESTED));
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            when(method.isTest()).thenReturn(true);
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.disableTestsByStatus(suite);
            assertThat(listener.method, is(nullValue()));
            assertThat(listener.status, is(nullValue()));
            assertThat(listener.msg, is(nullValue()));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Ignore disabling the test, if expected Status.SKIP")
        void unitTest_20180922070222() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SKIP));
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            when(method.isTest()).thenReturn(true);
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.disableTestsByStatus(suite);
            assertThat(listener.method, is(nullValue()));
            assertThat(listener.status, is(nullValue()));
            assertThat(listener.msg, is(nullValue()));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Ignore disabling the test, if expected Status.FAILED")
        void unitTest_20180922070246() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(FAILED));
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            when(method.isTest()).thenReturn(true);
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.disableTestsByStatus(suite);
            assertThat(listener.method, is(nullValue()));
            assertThat(listener.status, is(nullValue()));
            assertThat(listener.msg, is(nullValue()));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Check disabling the test, if expected Status.EXP_FIX")
        void unitTest_20180922070306() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(EXP_FIX));
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            when(method.isTest()).thenReturn(true);
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.disableTestsByStatus(suite);
            assertThat(listener.method, not(nullValue()));
            assertThat(listener.method.getInvocationCount(), not(0));
            assertThat(listener.status, is(EXP_FIX));
            assertThat(listener.msg, is(" \u2B9E forced run disabled"));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Check disabling the test, if expected Status.EXP_IMPL")
        void unitTest_20180922070547() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(EXP_IMPL));
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            when(method.isTest()).thenReturn(true);
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.disableTestsByStatus(suite);
            assertThat(listener.method, not(nullValue()));
            assertThat(listener.method.getInvocationCount(), not(0));
            assertThat(listener.status, is(EXP_IMPL));
            assertThat(listener.msg, is(" \u2B9E forced run disabled"));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Check disabling the test, if expected Status.BLOCKED")
        void unitTest_20180922070607() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(BLOCKED));
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            when(method.isTest()).thenReturn(true);
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.disableTestsByStatus(suite);
            assertThat(listener.method, not(nullValue()));
            assertThat(listener.method.getInvocationCount(), not(0));
            assertThat(listener.status, is(BLOCKED));
            assertThat(listener.msg, is(" \u2B9E forced run disabled"));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Check disabling the test, if expected Status.CORRUPTED")
        void unitTest_20180922070623() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(CORRUPTED));
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            when(method.isTest()).thenReturn(true);
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.disableTestsByStatus(suite);
            assertThat(listener.method, not(nullValue()));
            assertThat(listener.method.getInvocationCount(), not(0));
            assertThat(listener.status, is(CORRUPTED));
            assertThat(listener.msg, is(" \u2B9E forced run disabled"));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

    }

    @Nested()
    @DisplayName("disableTestsByType() tests")
    class DisableTestsByTypeTests extends BaseUnitTest {

        @Test
        @DisplayName("Ignore disabling the test, if @Details is not present")
        void unitTest_20180922073418() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
            when(method.isTest()).thenReturn(false);
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.disableTestsByType(suite);
            assertThat(listener.method, is(nullValue()));
            assertThat(listener.status, is(nullValue()));
            assertThat(listener.msg, is(nullValue()));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Ignore disabling the test, if config Type.isIncludeOrEquals(testType)")
        void unitTest_20180922081155() {
            Type configType = Buggy.getPrimaryConfig().getType();
            try {
                Buggy.getPrimaryConfig().setType(MODULE);
                UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SMOKE));
                ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
                when(method.isTest()).thenReturn(false);
                List<ITestNGMethod> allMethods = new ArrayList<>();
                allMethods.add(method);
                ISuite suite = mock(ISuite.class);
                when(suite.getAllMethods()).thenReturn(allMethods);
                int expWarn = Buggy.getBuggyWarns();
                listener.disableTestsByType(suite);
                assertThat(listener.method, is(nullValue()));
                assertThat(listener.status, is(nullValue()));
                assertThat(listener.msg, is(nullValue()));
                assertThat(Buggy.getBuggyWarns(), is(expWarn));
            } finally {
                Buggy.getPrimaryConfig().setType(configType);
            }
        }

        @Test
        @DisplayName("Check disabling the test, if config !Type.isIncludeOrEquals(testType)")
        void unitTest_20180922081522() {
            Type configType = Buggy.getPrimaryConfig().getType();
            try {
                Buggy.getPrimaryConfig().setType(MODULE);
                UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SYSTEM));
                ITestNGMethod method = getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithoutDetails");
                when(method.isTest()).thenReturn(false);
                List<ITestNGMethod> allMethods = new ArrayList<>();
                allMethods.add(method);
                ISuite suite = mock(ISuite.class);
                when(suite.getAllMethods()).thenReturn(allMethods);
                int expWarn = Buggy.getBuggyWarns();
                listener.disableTestsByType(suite);
                assertThat(listener.method, not(nullValue()));
                assertThat(listener.status, is(SKIP));
                assertThat(listener.msg, is(" \u2B9E SYSTEM test type"));
                assertThat(Buggy.getBuggyWarns(), is(expWarn));
            } finally {
                Buggy.getPrimaryConfig().setType(configType);
            }
        }

    }

    private static ITestNGMethod getMockITestNGMethod(Class<?> clazz, String methodName, boolean isTest) {
        ITestNGMethod method = getMockITestNGMethod(clazz, methodName);
        when(method.isTest()).thenReturn(isTest);
        return method;
    }

    private static ITestNGMethod getMockITestNGMethod(Class<?> clazz, String methodName) {
        Method method;
        try {
            method = clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        ConstructorOrMethod constructorOrMethod = mock(ConstructorOrMethod.class);
        when(constructorOrMethod.getMethod()).thenReturn(method);
        ITestNGMethod iTestNGMethod = mock(ITestNGMethod.class);
        when(iTestNGMethod.getConstructorOrMethod()).thenReturn(constructorOrMethod);
        when(iTestNGMethod.getMethodName()).thenReturn(methodName);
        when(iTestNGMethod.getRealClass()).thenReturn(clazz);
        when(iTestNGMethod.isTest()).thenReturn(true);
        when(iTestNGMethod.getInvocationCount()).thenReturn(1);
        return iTestNGMethod;
    }

    private static IInvokedMethod getMockIInvokedMethod() {
        return getMockIInvokedMethod(true);
    }

    private static IInvokedMethod getMockIInvokedMethod(boolean isTest) {
        return getMockIInvokedMethod(MockITestClass.class, "iTestResultMethodWithDetails", isTest);
    }

    @SuppressWarnings("SameParameterValue")
    private static IInvokedMethod getMockIInvokedMethod(Class<?> clazz, String methodName, boolean isTest) {
        ITestNGMethod iTestNGMethod = getMockITestNGMethod(clazz, methodName, isTest);
        IInvokedMethod iInvokedMethod = mock(IInvokedMethod.class);
        when(iInvokedMethod.getTestMethod()).thenReturn(iTestNGMethod);
        when(iInvokedMethod.isTestMethod()).thenReturn(isTest);
        return iInvokedMethod;
    }

    @SuppressWarnings("unchecked")
    private static ITestClass getMockITestClass(Class aClass) {
        ITestClass iTestClass = mock(ITestClass.class);
        when(iTestClass.getRealClass()).thenReturn(aClass);
        return iTestClass;
    }

    private static ITestResult getMockITestResult(Integer status) {
        Method method;
        try {
            method = MockITestClass.class.getMethod("iTestResultMethodWithDetails");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        ConstructorOrMethod constructorOrMethod = mock(ConstructorOrMethod.class);
        when(constructorOrMethod.getMethod()).thenReturn(method);

        ITestNGMethod iTestNGMethod = mock(ITestNGMethod.class);
        when(iTestNGMethod.getConstructorOrMethod()).thenReturn(constructorOrMethod);

        ITestResult iTestResult = mock(ITestResult.class);
        when(iTestResult.getStatus()).thenReturn(status);
        when(iTestResult.getMethod()).thenReturn(iTestNGMethod);
        return iTestResult;
    }

    public class UnitTestBuggyExecutionListener extends BuggyExecutionListener {

        ITestNGMethod method;
        Status status;
        String msg;
        Details details;

        UnitTestBuggyExecutionListener() {
            super(LOG, LOG, LOG);
        }

        UnitTestBuggyExecutionListener(Details details) {
            super(LOG, LOG, LOG);
            this.details = details;
        }

        UnitTestBuggyExecutionListener(Logger testLogger, Logger frameworkLogger, Logger consoleLogger) {
            super(testLogger, frameworkLogger, consoleLogger);
        }

        @Override
        public void resultLog(ITestNGMethod method, Status status, String msg) {
            this.method = method;
            this.status = status;
            this.msg = msg;
        }

        @Override
        protected @Nullable Details getDetails(Method method) {
            return details;
        }
    }

    @SuppressWarnings("WeakerAccess")
    @Suite(service = TestService.class, interfaze = TestInterface.class)
    public static class MockITestClass {
        @SuppressWarnings("WeakerAccess")
        @Details
        public void iTestResultMethodWithDetails() { }
        @SuppressWarnings({"WeakerAccess", "unused"})
        public void iTestResultMethodWithoutDetails() {
            // for getMockITestResult()
        }
    }

}
