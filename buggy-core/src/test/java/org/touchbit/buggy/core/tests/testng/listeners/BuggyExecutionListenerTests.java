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

package org.touchbit.buggy.core.tests.testng.listeners;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.testng.*;
import org.touchbit.buggy.core.config.TestComponent;
import org.touchbit.buggy.core.config.TestInterface;
import org.touchbit.buggy.core.config.TestNGTestClassWithSuite;
import org.touchbit.buggy.core.config.TestService;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.tests.BaseUnitTest;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.exceptions.ExpectedImplementationException;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.testng.listeners.BuggyExecutionListener;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.touchbit.buggy.core.config.BParameters.PRINT_CAUSE;
import static org.touchbit.buggy.core.config.BParameters.PRINT_LOG;
import static org.touchbit.buggy.core.config.BParameters.PRINT_SUITE;
import static org.touchbit.buggy.core.model.Status.*;
import static org.touchbit.buggy.core.model.Type.*;

/**
 * Created by Oleg Shaburov on 21.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("BuggyExecutionListener class tests")
class BuggyExecutionListenerTests extends BaseUnitTest {

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
    @DisplayName("Check BuggyExecutionListener(Logger testLogger, Logger frameworkLogger, Logger consoleLogger)")
    void unitTest_20180921144624() {
        BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
        listener.onExecutionStart();
        listener.onExecutionFinish();
        assertThat(listener.isEnable(), is(true));
    }

    @Test
    @DisplayName("Check 'do nothing' methods")
    void unitTest_20180921145653() {
        BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
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
        BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
        ITestResult iTestResult = getMockITestResult(1);
        listener.onTestSuccess(iTestResult);
    }

    @Test
    @DisplayName("Check onTestFailure(ITestResult result)")
    void unitTest_20180921150916() {
        BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
        ITestResult iTestResult = getMockITestResult(2);
        listener.onTestFailure(iTestResult);
    }

    @Test
    @DisplayName("Check onTestSkipped(ITestResult result)")
    void unitTest_20180921151028() {
        BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
        ITestResult iTestResult = getMockITestResult(3);
        listener.onTestSkipped(iTestResult);
    }

    @Test
    @DisplayName("Check onTestFailedButWithinSuccessPercentage(ITestResult result)")
    void unitTest_20180921151130() {
        BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
        ITestResult iTestResult = getMockITestResult(4);
        listener.onTestFailedButWithinSuccessPercentage(iTestResult);
    }

    @Test
    @DisplayName("Check onTestFinish with ExpectedImplementationException")
    void unitTest_20180921182848() {
        BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
        ITestResult iTestResult = getMockITestResult(4);
        when(iTestResult.getThrowable()).thenReturn(new ExpectedImplementationException(""));
        listener.onTestFinish(iTestResult);
    }

    @Test
    @DisplayName("Check onTestFinish with Exception")
    void unitTest_20180921183436() {
        BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
        ITestResult iTestResult = getMockITestResult(4);
        when(iTestResult.getThrowable()).thenReturn(new Exception(""));
        listener.onTestFinish(iTestResult);
    }

    @Test
    @DisplayName("Check onTestFinish with null steps")
    void unitTest_20180921183125() {
        BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
        ITestResult iTestResult = getMockITestResult(1);
        BuggyExecutionListener.setSteps(null);
        listener.onTestFinish(iTestResult);
    }

    @Test
    @DisplayName("Check onTestFinish with empty steps")
    void unitTest_20180921183228() {
        BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
        ITestResult iTestResult = getMockITestResult(1);
        BuggyExecutionListener.setSteps(new ArrayList<String>() {{ add("steppp"); }});
        listener.onTestFinish(iTestResult);
    }

    @Test
    @DisplayName("Check onAfterClass(ITestClass iTestClass) with Suite")
    void unitTest_20180921183611() {
        BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
        ITestClass iTestClass = getMockITestClass(TestNGTestClassWithSuite.class);
        BuggyExecutionListener.setSteps(new ArrayList<>());
        listener.onAfterClass(iTestClass);
    }

    @Test
    @DisplayName("Check onAfterClass(ITestClass iTestClass) without Suite")
    void unitTest_20180921184448() {
        BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
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
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
            List<ITestNGMethod> allMethods = new ArrayList<>();
            allMethods.add(method);
            ISuite suite = mock(ISuite.class);
            when(suite.getAllMethods()).thenReturn(allMethods);
            int expWarn = Buggy.getBuggyWarns();
            listener.onStart(suite);
            assertThat(listener.method, not(nullValue()));
            assertThat(listener.status, is(EXP_FIX));
            assertThat(listener.msg, is("forced test run disabled"));
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
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
            BuggyExecutionListener listener = getBuggyExecutionListener();
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            ITestResult iTestResult = getMockITestResult(16);
            listener.beforeInvocation(iInvokedMethod, iTestResult);
            String logResult = TEST_LOGGER.takeLoggedMessages().toString();
            assertThat(logResult, containsString("Test method is running:\niTestResultMethodWithDetails - null"));
            assertThat(logResult, containsString("Declared method annotations:\n@"));
        }

        @Test
        @DisplayName("Check Check beforeInvocation() with CREATED status")
        void unitTest_20180921191657() {
            BuggyExecutionListener listener = getBuggyExecutionListener();
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            ITestResult iTestResult = getMockITestResult(-1);
            listener.beforeInvocation(iInvokedMethod, iTestResult);
            String logResult = TEST_LOGGER.takeLoggedMessages().toString();
            assertThat(logResult, containsString("Test method is running:\niTestResultMethodWithDetails - null"));
            assertThat(logResult, containsString("Declared method annotations:\n@"));
        }

        @Test
        @DisplayName("Check beforeInvocation() is test method")
        void unitTest_20180921191731() {
            BuggyExecutionListener listener = getBuggyExecutionListener();
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            ITestResult iTestResult = getMockITestResult(16);
            when(iInvokedMethod.isTestMethod()).thenReturn(true);
            listener.beforeInvocation(iInvokedMethod, iTestResult);
            String logResult = TEST_LOGGER.takeLoggedMessages().toString();
            assertThat(logResult, containsString("Test method is running:\niTestResultMethodWithDetails - null"));
            assertThat(logResult, containsString("Declared method annotations:\n@"));
        }

        @Test
        @DisplayName("Check beforeInvocation() is no test method")
        void unitTest_20180921191829() {
            BuggyExecutionListener listener = getBuggyExecutionListener();
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            ITestResult iTestResult = getMockITestResult(16);
            when(iInvokedMethod.isTestMethod()).thenReturn(false);
            listener.beforeInvocation(iInvokedMethod, iTestResult);
            String logResult = TEST_LOGGER.takeLoggedMessages().toString();
            assertThat(logResult, containsString("Configuration method is running:\niTestResultMethodWithDetails - null."));
            assertThat(logResult, containsString("Declared method annotations:\n@"));
        }

        @Test
        @DisplayName("Check debug info")
        void unitTest_20180922090145() {
            TEST_LOGGER.whenDebugEnabled(false);
            BuggyExecutionListener listener = getBuggyExecutionListener();
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            ITestResult iTestResult = getMockITestResult(16);
            when(iInvokedMethod.isTestMethod()).thenReturn(false);
            listener.beforeInvocation(iInvokedMethod, iTestResult);
            String logResult = TEST_LOGGER.takeLoggedMessages().toString();
            assertThat(logResult, containsString("Configuration method is running:\niTestResultMethodWithDetails - null."));
            assertThat(logResult, is(not(containsString("Declared method annotations:\n@"))));
        }

    }

    @Nested()
    @DisplayName("afterInvocation() tests")
    class AfterInvocationTests extends BaseUnitTest {

        @Test
        @DisplayName("Added ERROR to step when test method with exception and step list more than own")
        void unitTest_20180921220759() {
            BuggyExecutionListener listener = getBuggyExecutionListener();
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            when(iInvokedMethod.isTestMethod()).thenReturn(true);
            ITestResult iTestResult = getMockITestResult(1);
            when(iTestResult.getThrowable()).thenReturn(new Exception());
            BuggyExecutionListener.step(TEST_LOGGER, "with exception");
            listener.afterInvocation(iInvokedMethod, iTestResult);
            assertThat(BuggyExecutionListener.getSteps(), contains("Step 1. with exception - ERROR"));
            assertThat(TEST_LOGGER.takeLoggedMessages(), contains(
                    " ------------> Step 1. with exception",
                    "Execution of iTestResultMethodWithDetails resulted in an error.",
                    "iTestResultMethodWithDetails - SUCCESS null",
                    "iTestResultMethodWithDetails............SUCCESS"));
        }

        @Test
        @DisplayName("Skip add ERROR to step when test method with exception and step list is empty")
        void unitTest_20181019023045() {
            BuggyExecutionListener listener = getBuggyExecutionListener();
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            when(iInvokedMethod.isTestMethod()).thenReturn(true);
            ITestResult iTestResult = getMockITestResult(1);
            when(iTestResult.getThrowable()).thenReturn(new Exception());
            listener.afterInvocation(iInvokedMethod, iTestResult);
            assertThat(BuggyExecutionListener.getSteps(), is(empty()));
            assertThat(TEST_LOGGER.takeLoggedMessages(), contains(
                    "Execution of iTestResultMethodWithDetails resulted in an error.",
                    "iTestResultMethodWithDetails - SUCCESS null",
                    "iTestResultMethodWithDetails............SUCCESS"));
        }

        @Test
        @DisplayName("Check afterInvocation test method without exception")
        void unitTest_20180921221233() {
            BuggyExecutionListener listener = getBuggyExecutionListener();
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            when(iInvokedMethod.isTestMethod()).thenReturn(true);
            ITestResult iTestResult = getMockITestResult(1);
            listener.afterInvocation(iInvokedMethod, iTestResult);
            assertThat(BuggyExecutionListener.getSteps(), is(empty()));
            assertThat(TEST_LOGGER.takeLoggedMessages(), contains(
                    "iTestResultMethodWithDetails - SUCCESS null",
                    "iTestResultMethodWithDetails............SUCCESS"
            ));
        }

        @Test
        @DisplayName("Check afterInvocation without @Details")
        void unitTest_20180921222920() {
            BuggyExecutionListener listener = getBuggyExecutionListener();
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod(TestNGTestClassWithSuite.class,
                    "iTestResultMethodWithoutDetails", true);
            when(iInvokedMethod.isTestMethod()).thenReturn(true);
            ITestResult iTestResult = getMockITestResult(1);
            listener.afterInvocation(iInvokedMethod, iTestResult);
            assertThat(BuggyExecutionListener.getSteps(), is(empty()));
            assertThat(TEST_LOGGER.takeLoggedMessages(), contains(
                    "The test method iTestResultMethodWithoutDetails does not contain the @Details annotation"
            ));
        }

        @Test
        @DisplayName("Check afterInvocation configuration method")
        void unitTest_20180921224011() {
            BuggyExecutionListener listener = getBuggyExecutionListener();
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            when(iInvokedMethod.isTestMethod()).thenReturn(false);
            listener.afterInvocation(iInvokedMethod, getMockITestResult(1));
            assertThat(BuggyExecutionListener.getSteps(), is(empty()));
            assertThat(TEST_LOGGER.takeLoggedMessages(), contains(
                    "Invoke configuration method [iTestResultMethodWithDetails] completed successfully"
            ));
        }

        @Test
        @DisplayName("Test count increment when status != SKIP")
        void unitTest_20181019022603() {
            BuggyExecutionListener listener = getBuggyExecutionListener();
            IInvokedMethod iInvokedMethod = getMockIInvokedMethod();
            when(iInvokedMethod.isTestMethod()).thenReturn(true);
            int expTestCount = listener.getTestCount() + 1;
            listener.afterInvocation(iInvokedMethod, getMockITestResult(ITestResult.SUCCESS));
            assertThat(listener.getTestCount(), is(expTestCount));
            listener.afterInvocation(iInvokedMethod, getMockITestResult(ITestResult.SKIP));
            assertThat(listener.getTestCount(), is(expTestCount));
        }

    }

    @Nested()
    @DisplayName("step() tests")
    class StepTests extends BaseUnitTest {

        @Test
        @DisplayName("Check step(LOG, \"msg\")")
        void unitTest_20180921192521() {
            BuggyExecutionListener.step(TEST_LOGGER, "msg");
            assertThat(BuggyExecutionListener.getSteps(), contains("Step 1. msg"));
        }

        @Test
        @DisplayName("Check step(LOG, \"msg {}\", \"with args\")")
        void unitTest_20180921192637() {
            BuggyExecutionListener.step(TEST_LOGGER, "msg {}", "with args");
            assertThat(BuggyExecutionListener.getSteps(), contains("Step 1. msg with args"));
        }

        @Test
        @DisplayName("Check ignoring step() ASC")
        void unitTest_20181004002108() {
            BuggyExecutionListener.step(TEST_LOGGER, "a");
            BuggyExecutionListener.step(TEST_LOGGER, "b");
            assertThat(BuggyExecutionListener.getSteps().toString(), is("[Step 1. a, Step 2. b]"));
        }

        @Test
        @DisplayName("Check ignoring step() DESC")
        void unitTest_20181004004254() {
            BuggyExecutionListener.step(TEST_LOGGER, "b");
            BuggyExecutionListener.step(TEST_LOGGER, "a");
            assertThat(BuggyExecutionListener.getSteps().toString(), is("[Step 1. b, Step 2. a]"));
        }

    }

    @Nested()
    @DisplayName("processTestMethodResult() tests")
    class ProcessTestMethodResultTests extends BaseUnitTest {

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details EXP_FIX Status with issues")
        void unitTest_20180922023815() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(EXP_FIX, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FIXED));
            assertThat(listener.msg, is("[JIRA-123]"));
        }
        
        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details EXP_FIX Status without issues")
        void unitTest_20180922031547() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(EXP_FIX));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FIXED));
            assertThat(listener.msg, is(isEmptyString()));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details BLOCKED Status with issues")
        void unitTest_20180922031825() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(BLOCKED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FIXED));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details BLOCKED Status without issues")
        void unitTest_20180922031943() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(BLOCKED));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FIXED));
            assertThat(listener.msg, is(isEmptyString()));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details EXP_IMPL Status with issues")
        void unitTest_20180922033333() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(EXP_IMPL, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(IMPLEMENTED));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details EXP_IMPL Status without issues")
        void unitTest_20180922033338() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(EXP_IMPL));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(IMPLEMENTED));
            assertThat(listener.msg, is(isEmptyString()));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details CORRUPTED Status with issues")
        void unitTest_20180922033427() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(CORRUPTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(CORRUPTED));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details CORRUPTED Status without issues")
        void unitTest_20180922033726() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(CORRUPTED));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(CORRUPTED));
            assertThat(listener.msg, is(isEmptyString()));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details FIXED Status with issues")
        void unitTest_20180922033959() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(FIXED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SUCCESS));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details IMPLEMENTED Status with issues")
        void unitTest_20180922034106() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(IMPLEMENTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SUCCESS));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details UNTESTED Status with issues")
        void unitTest_20180922034141() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(UNTESTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SUCCESS));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details SUCCESS Status with issues")
        void unitTest_20180922034318() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(SUCCESS, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SUCCESS));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details SKIP Status with issues")
        void unitTest_20180922034337() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(SKIP, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SUCCESS));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS if Details FAILED Status with issues")
        void unitTest_20180922034357() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS), getDetails(FAILED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SUCCESS));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SUCCESS_PERCENTAGE_FAILURE if Details EXP_FIX Status with issues")
        void unitTest_20180922034704() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SUCCESS_PERCENTAGE_FAILURE),
                    getDetails(EXP_FIX, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(EXP_FIX));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details EXP_FIX Status with issues")
        void unitTest_20180922034924() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(EXP_FIX, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(EXP_FIX));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details CORRUPTED Status with issues")
        void unitTest_20180922035053() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(CORRUPTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(CORRUPTED));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details EXP_IMPL Status with issues")
        void unitTest_20180922035146() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(EXP_IMPL, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(EXP_IMPL));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details BLOCKED Status with issues")
        void unitTest_20180922035208() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(BLOCKED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(BLOCKED));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details FIXED Status with issues")
        void unitTest_20180922035238() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(FIXED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FAILED));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details IMPLEMENTED Status with issues")
        void unitTest_20180922035331() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(IMPLEMENTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FAILED));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details UNTESTED Status with issues")
        void unitTest_20180922035403() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(UNTESTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FAILED));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details SUCCESS Status with issues")
        void unitTest_20180922035419() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(SUCCESS, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FAILED));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details SKIP Status with issues")
        void unitTest_20180922035435() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(SKIP, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FAILED));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE if Details FAILED Status with issues")
        void unitTest_20180922035529() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.FAILURE), getDetails(FAILED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(FAILED));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details EXP_IMPL Status with issues")
        void unitTest_20180922035630() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(EXP_IMPL, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details EXP_FIX Status with issues")
        void unitTest_20180922035830() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(EXP_FIX, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details BLOCKED Status with issues")
        void unitTest_20180922035902() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(BLOCKED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details CORRUPTED Status with issues")
        void unitTest_20180922035917() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(CORRUPTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details FIXED Status with issues")
        void unitTest_20180922035933() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(FIXED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details IMPLEMENTED Status with issues")
        void unitTest_20180922035950() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(IMPLEMENTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details UNTESTED Status with issues")
        void unitTest_20180922040014() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(UNTESTED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details SUCCESS Status with issues")
        void unitTest_20180922040029() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(SUCCESS, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details SKIP Status with issues")
        void unitTest_20180922040042() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(SKIP, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP if Details FAILED Status with issues")
        void unitTest_20180922040055() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod();
            listener.processTestMethodResult(method, getMockITestResult(ITestResult.SKIP), getDetails(FAILED, "JIRA-123"));
            assertThat(listener.method.getMethodName(), is(method.getTestMethod().getMethodName()));
            assertThat(listener.status, is(SKIP));
            assertThat(listener.msg, is("[JIRA-123]"));
        }

        @Test
        @DisplayName("Check ITestResult.CREATED is not unprocessed")
        void unitTest_20180922040117() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
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
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
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
            BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod(false);
            int expErrors = Buggy.getBuggyErrors();
            listener.processConfigurationMethodResult(method, getMockITestResult(ITestResult.SUCCESS));
            assertThat(Buggy.getBuggyErrors(), is(expErrors));
        }

        @Test
        @DisplayName("Check ITestResult.SKIP")
        void unitTest_20180922061334() {
            BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod(false);
            int expErrors = Buggy.getBuggyErrors();
            listener.processConfigurationMethodResult(method, getMockITestResult(ITestResult.SKIP));
            assertThat(Buggy.getBuggyErrors(), is(expErrors));
        }

        @Test
        @DisplayName("Check ITestResult.FAILURE")
        void unitTest_20180922061357() {
            BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
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
            BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
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
            BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
            IInvokedMethod method = getMockIInvokedMethod(false);
            int expErrors = Buggy.getBuggyErrors() + 1;
            listener.processConfigurationMethodResult(method, getMockITestResult(ITestResult.CREATED));
            assertThat(Buggy.getBuggyErrors(), is(expErrors));
        }

        @Test
        @DisplayName("Check ITestResult.STARTED")
        void unitTest_20180922061516() {
            BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
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
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
        @DisplayName("Ignore disabling the test, if method has @Details and invocation count <= 0")
        void unitTest_20180922063213() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SUCCESS));
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithDetails");
            when(method.getInvocationCount()).thenReturn(0);
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
        @DisplayName("Ignore disabling the test, if received configuration method")
        void unitTest_20180922063717() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SUCCESS));
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
            assertThat(listener.msg, is("forced test run disabled"));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Check disabling the test, if expected Status.EXP_IMPL")
        void unitTest_20180922070547() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(EXP_IMPL));
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
            assertThat(listener.msg, is("forced test run disabled"));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Check disabling the test, if expected Status.BLOCKED")
        void unitTest_20180922070607() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(BLOCKED));
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
            assertThat(listener.msg, is("forced test run disabled"));
            assertThat(Buggy.getBuggyWarns(), is(expWarn));
        }

        @Test
        @DisplayName("Check disabling the test, if expected Status.CORRUPTED")
        void unitTest_20180922070623() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(CORRUPTED));
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
            assertThat(listener.msg, is("forced test run disabled"));
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
            ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
                ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
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
                ITestNGMethod method = getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithoutDetails");
                when(method.isTest()).thenReturn(false);
                List<ITestNGMethod> allMethods = new ArrayList<>();
                allMethods.add(method);
                ISuite suite = mock(ISuite.class);
                when(suite.getAllMethods()).thenReturn(allMethods);
                int expWarn = Buggy.getBuggyWarns();
                listener.disableTestsByType(suite);
                assertThat(listener.method, not(nullValue()));
                assertThat(listener.status, is(SKIP));
                assertThat(listener.msg, is("SYSTEM test type"));
                assertThat(Buggy.getBuggyWarns(), is(expWarn));
            } finally {
                Buggy.getPrimaryConfig().setType(configType);
            }
        }

    }

    @Nested()
    @DisplayName("Steps tests")
    class StepsTests extends BaseUnitTest {

        @Test
        @DisplayName("Check getSteps() if setSteps(List)")
        void unitTest_20181018171824() {
            List<String> steps = new ArrayList<>();
            steps.add("s1");
            BuggyExecutionListener.setSteps(steps);
            assertThat(BuggyExecutionListener.getSteps(), contains("s1"));
            assertThat(TEST_LOGGER.takeLoggedMessages(), is(empty()));
        }

        @Test
        @DisplayName("Check getSteps() if setSteps(null)")
        void unitTest_20181018172255() {
            BuggyExecutionListener.setSteps(null);
            assertThat(BuggyExecutionListener.getSteps(), is(empty()));
            assertThat(TEST_LOGGER.takeLoggedMessages(), is(empty()));
        }

    }

    @Nested()
    @DisplayName("copyTestMethodLogFile() tests")
    class CopyTestMethodLogFileTests extends BaseUnitTest {

        @Test
        @DisplayName("Check copied log files if ITestResult.SUCCESS")
        void unitTest_20181018183643() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.SUCCESS);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SUCCESS));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(nullValue()));
            assertThat(listener.targetFile, is(nullValue()));
            assertThat(TEST_LOGGER.takeLoggedMessages(), is(empty()));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.FAILURE & details == null")
        void unitTest_20181018191517() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.FAILURE);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/errors/new/iTestResultMethodWithDetails.log")));
            assertThat(TEST_LOGGER.takeLoggedMessages(), is(empty()));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.SUCCESS_PERCENTAGE_FAILURE & details == null")
        void unitTest_20181018205528() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.SUCCESS_PERCENTAGE_FAILURE);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/errors/new/iTestResultMethodWithDetails.log")));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.SUCCESS & details == null")
        void unitTest_20181018205529() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.SUCCESS);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(nullValue()));
            assertThat(listener.targetFile, is(nullValue()));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.SUCCESS & Status.EXP_FIX")
        void unitTest_20181018211903() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.SUCCESS);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(EXP_FIX));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/fixed/iTestResultMethodWithDetails.log")));
            assertThat(TEST_LOGGER.takeLoggedMessages(), is(empty()));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.SUCCESS & Status.BLOCKED")
        void unitTest_20181018211949() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.SUCCESS);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(BLOCKED));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/fixed/iTestResultMethodWithDetails.log")));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.SUCCESS & Status.EXP_IMPL")
        void unitTest_20181018212222() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.SUCCESS);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(EXP_IMPL));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/implemented/iTestResultMethodWithDetails.log")));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.FAILURE & Status.EXP_FIX")
        void unitTest_20181018214458() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.FAILURE);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(EXP_FIX));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/errors/exp_fix/iTestResultMethodWithDetails.log")));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.FAILURE & Status.EXP_IMPL")
        void unitTest_20181018214554() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.FAILURE);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(EXP_IMPL));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/errors/exp_impl/iTestResultMethodWithDetails.log")));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.FAILURE & Status.BLOCKED")
        void unitTest_20181018214613() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.FAILURE);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(BLOCKED));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/errors/blocked/iTestResultMethodWithDetails.log")));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.FAILURE & Status.CORRUPTED")
        void unitTest_20181018214633() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.FAILURE);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(CORRUPTED));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/errors/corrupted/iTestResultMethodWithDetails.log")));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.FAILURE & Status.FAILED")
        void unitTest_20181018214735() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.FAILURE);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(FAILED));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/errors/new/iTestResultMethodWithDetails.log")));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.FAILURE & Status.SKIP")
        void unitTest_20181018214857() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.FAILURE);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SKIP));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/errors/new/iTestResultMethodWithDetails.log")));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.FAILURE & Status.SUCCESS")
        void unitTest_20181018214917() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.FAILURE);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SUCCESS));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/errors/new/iTestResultMethodWithDetails.log")));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.FAILURE & Status.UNTESTED")
        void unitTest_20181018215040() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.FAILURE);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(UNTESTED));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/errors/new/iTestResultMethodWithDetails.log")));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.FAILURE & Status.IMPLEMENTED")
        void unitTest_20181018215101() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.FAILURE);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(IMPLEMENTED));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/errors/new/iTestResultMethodWithDetails.log")));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.FAILURE & Status.FIXED")
        void unitTest_20181018215119() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.FAILURE);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(FIXED));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/errors/new/iTestResultMethodWithDetails.log")));
        }

        @Test
        @DisplayName("Check copied log files if ITestResult.SUCCESS_PERCENTAGE_FAILURE & Status.SUCCESS")
        void unitTest_20181018215341() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.SUCCESS_PERCENTAGE_FAILURE);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SUCCESS));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(new File(WASTE, "/tests/iTestResultMethodWithDetails.log")));
            assertThat(listener.targetFile, is(new File(WASTE, "/errors/new/iTestResultMethodWithDetails.log")));
        }

        @Test
        @DisplayName("Check files if ITestResult.STARTED & Status.SUCCESS")
        void unitTest_20181018215411() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.STARTED);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SUCCESS));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(nullValue()));
            assertThat(listener.targetFile, is(nullValue()));
        }

        @Test
        @DisplayName("Check files if ITestResult.SKIP & Status.SUCCESS")
        void unitTest_20181018215433() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.SKIP);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SUCCESS));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(nullValue()));
            assertThat(listener.targetFile, is(nullValue()));
        }

        @Test
        @DisplayName("Check files if ITestResult.SUCCESS & Status.SUCCESS")
        void unitTest_20181018215544() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.SUCCESS);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SUCCESS));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(nullValue()));
            assertThat(listener.targetFile, is(nullValue()));
        }

        @Test
        @DisplayName("Check files if ITestResult.CREATED & Status.SUCCESS")
        void unitTest_20181018215622() throws Exception {
            IInvokedMethod method = getMockIInvokedMethod();
            ITestResult iTestResult = mock(ITestResult.class);
            when(iTestResult.getStatus()).thenReturn(ITestResult.CREATED);
            when(method.getTestResult()).thenReturn(iTestResult);
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener(getDetails(SUCCESS));
            listener.copyTestMethodLogFile(method);
            assertThat(listener.sourceFile, is(nullValue()));
            assertThat(listener.targetFile, is(nullValue()));
        }

    }

    @Nested()
    @DisplayName("resultLog() tests")
    class ResultLogTest extends BaseUnitTest {

        private final Suite suite = new Suite() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return Suite.class;
            }

            @Override
            public Class<? extends Component> component() {
                return TestComponent.class;
            }

            @Override
            public Class<? extends Service> service() {
                return TestService.class;
            }

            @Override
            public Class<? extends Interface> interfaze() {
                return TestInterface.class;
            }

            @Override
            public String purpose() {
                return "TestTask";
            }
        };

        @Test
        @DisplayName("Check logs when " + PRINT_SUITE + "=false " + PRINT_CAUSE + "=false " + PRINT_LOG + "=false")
        void unitTest_20181019011816() {
            BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER) {
                @Override
                protected Suite getSuite(ITestNGMethod method) {
                    return suite;
                }
            };
            ITestNGMethod iTestNGMethod = getMockITestNGMethod();
            listener.resultLog(iTestNGMethod, Status.SUCCESS, "cause");
            assertThat(TEST_LOGGER.takeLoggedMessages(), contains(
                    "iTestResultMethodWithDetails - SUCCESS null",
                    "iTestResultMethodWithDetails............SUCCESS"
            ));
        }

        @Test
        @DisplayName("Check logs when " + PRINT_SUITE + "=true " + PRINT_CAUSE + "=false " + PRINT_LOG + "=false")
        void unitTest_20181019012936() {
            Buggy.getPrimaryConfig().setPrintSuite(true);
            Buggy.getPrimaryConfig().setPrintCause(false);
            Buggy.getPrimaryConfig().setPrintLogFile(false);
            BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER) {
                @Override
                protected Suite getSuite(ITestNGMethod method) {
                    return suite;
                }
            };
            ITestNGMethod iTestNGMethod = getMockITestNGMethod();
            listener.resultLog(iTestNGMethod, Status.SUCCESS, "cause");
            assertThat(TEST_LOGGER.takeLoggedMessages(), contains(
                    "iTestResultMethodWithDetails - SUCCESS null",
                    "iTestResultMethodWithDetails............SUCCESS [TESTCOMPONENT TESTSERVICE TESTINTERFACE TestTask]"
            ));
        }

        @Test
        @DisplayName("Check logs when " + PRINT_SUITE + "=false " + PRINT_CAUSE + "=true " + PRINT_LOG + "=false")
        void unitTest_20181019013142() {
            Buggy.getPrimaryConfig().setPrintSuite(false);
            Buggy.getPrimaryConfig().setPrintCause(true);
            Buggy.getPrimaryConfig().setPrintLogFile(false);
            BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER) {
                @Override
                protected Suite getSuite(ITestNGMethod method) {
                    return suite;
                }
            };
            ITestNGMethod iTestNGMethod = getMockITestNGMethod();
            listener.resultLog(iTestNGMethod, Status.SUCCESS, "cause");
            assertThat(TEST_LOGGER.takeLoggedMessages(), contains(
                    "iTestResultMethodWithDetails - SUCCESS null",
                    "iTestResultMethodWithDetails............SUCCESS cause"
            ));
        }

        @Test
        @DisplayName("Check logs when " + PRINT_SUITE + "=false " + PRINT_CAUSE + "=false " + PRINT_LOG + "=true")
        void unitTest_20181019013245() {
            Buggy.getPrimaryConfig().setPrintCause(false);
            Buggy.getPrimaryConfig().setPrintSuite(false);
            Buggy.getPrimaryConfig().setPrintLogFile(true);
            BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER) {
                @Override
                protected Suite getSuite(ITestNGMethod method) {
                    return suite;
                }
            };
            ITestNGMethod iTestNGMethod = getMockITestNGMethod();
            listener.resultLog(iTestNGMethod, Status.SUCCESS, "cause");
            assertThat(TEST_LOGGER.takeLoggedMessages(), contains(
                    "iTestResultMethodWithDetails - SUCCESS null",
                    "iTestResultMethodWithDetails............SUCCESS └ file://" + WASTE +
                            "/tests/iTestResultMethodWithDetails.log"
            ));
        }

        @Test
        @DisplayName("Check logs when " + PRINT_SUITE + "=true " + PRINT_CAUSE + "=true " + PRINT_LOG + "=true")
        void unitTest_20181019013427() {
            Buggy.getPrimaryConfig().setPrintCause(true);
            Buggy.getPrimaryConfig().setPrintSuite(true);
            Buggy.getPrimaryConfig().setPrintLogFile(true);
            BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER) {
                @Override
                protected Suite getSuite(ITestNGMethod method) {
                    return suite;
                }
            };
            ITestNGMethod iTestNGMethod = getMockITestNGMethod();
            listener.resultLog(iTestNGMethod, Status.SUCCESS, "cause");
            assertThat(TEST_LOGGER.takeLoggedMessages(), contains(
                    "iTestResultMethodWithDetails - SUCCESS null",
                    "iTestResultMethodWithDetails............SUCCESS [TESTCOMPONENT TESTSERVICE TESTINTERFACE TestTask] " +
                            "cause \n└ file://" + WASTE +
                            "/tests/iTestResultMethodWithDetails.log"
            ));
        }

        @Test
        @DisplayName("Check increment call when isTest=true and getInvocationCount() > 0")
        void unitTest_20181019014923() {
            BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER) {
                @Override
                protected Suite getSuite(ITestNGMethod method) {
                    return suite;
                }
            };
            ITestNGMethod iTestNGMethod = getMockITestNGMethod();
            when(iTestNGMethod.isTest()).thenReturn(true);
            when(iTestNGMethod.getInvocationCount()).thenReturn(1);
            int expectedErrors = listener.getNewError() + 1;
            listener.resultLog(iTestNGMethod, Status.FAILED, "cause");
            assertThat(TEST_LOGGER.takeLoggedMessages(), contains(
                    "iTestResultMethodWithDetails - FAILED null",
                    "iTestResultMethodWithDetails.............FAILED"
            ));
            assertThat(listener.getNewError(), is(expectedErrors));
        }

        @Test
        @DisplayName("Check increment call when isTest=true and getInvocationCount() < 1")
        void unitTest_20181019015521() {
            BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER) {
                @Override
                protected Suite getSuite(ITestNGMethod method) {
                    return suite;
                }
            };
            ITestNGMethod iTestNGMethod = getMockITestNGMethod();
            when(iTestNGMethod.isTest()).thenReturn(true);
            when(iTestNGMethod.getInvocationCount()).thenReturn(0);
            int expectedErrors = listener.getNewError();
            int expectedSkiped = listener.getSkippedTests() + 1;
            listener.resultLog(iTestNGMethod, Status.FAILED, "skip cause");
            assertThat(TEST_LOGGER.takeLoggedMessages(), contains(
                    "iTestResultMethodWithDetails - FAILED null",
                    "iTestResultMethodWithDetails.............FAILED skip cause"
            ));
            assertThat(listener.getNewError(), is(expectedErrors));
            assertThat(listener.getSkippedTests(), is(expectedSkiped));
        }

        @Test
        @DisplayName("Check increment call when isTest=false")
        void unitTest_20181019015450() {
            BuggyExecutionListener listener = new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER) {
                @Override
                protected Suite getSuite(ITestNGMethod method) {
                    return suite;
                }
            };
            ITestNGMethod iTestNGMethod = getMockITestNGMethod();
            when(iTestNGMethod.isTest()).thenReturn(false);
            int expectedErrors = listener.getNewError();
            listener.resultLog(iTestNGMethod, Status.FAILED, "cause");
            assertThat(TEST_LOGGER.takeLoggedMessages(), contains(
                    "iTestResultMethodWithDetails - FAILED null",
                    "iTestResultMethodWithDetails.............FAILED"
            ));
            assertThat(listener.getNewError(), is(expectedErrors));
        }

    }

    @Nested()
    @DisplayName("printASCIIStatus() tests")
    class PrintASCIIStatusTest extends BaseUnitTest {

        @Test
        @DisplayName("Check ASCII print FAILED Status")
        void unitTest_20181019010409() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.printASCIIStatus(FAILED, "FAILED msg");
            assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains("ERROR FAILED msg"));
        }

        @Test
        @DisplayName("Check ASCII print CORRUPTED Status")
        void unitTest_20181019010858() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.printASCIIStatus(CORRUPTED, "CORRUPTED msg");
            assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains("ERROR CORRUPTED msg"));
        }

        @Test
        @DisplayName("Check ASCII print EXP_IMPL Status")
        void unitTest_20181019010928() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.printASCIIStatus(EXP_IMPL, "EXP_IMPL msg");
            assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains("WARN EXP_IMPL msg"));
        }

        @Test
        @DisplayName("Check ASCII print EXP_FIX Status")
        void unitTest_20181019010955() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.printASCIIStatus(EXP_FIX, "EXP_FIX msg");
            assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains("WARN EXP_FIX msg"));
        }

        @Test
        @DisplayName("Check ASCII print BLOCKED Status")
        void unitTest_20181019011009() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.printASCIIStatus(BLOCKED, "BLOCKED msg");
            assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains("WARN BLOCKED msg"));
        }

        @Test
        @DisplayName("Check ASCII print SKIP Status")
        void unitTest_20181019011024() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.printASCIIStatus(SKIP, "SKIP msg");
            assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains("WARN SKIP msg"));
        }

        @Test
        @DisplayName("Check ASCII print IMPLEMENTED Status")
        void unitTest_20181019011041() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.printASCIIStatus(IMPLEMENTED, "IMPLEMENTED msg");
            assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains("DEBUG IMPLEMENTED msg"));
        }

        @Test
        @DisplayName("Check ASCII print FIXED Status")
        void unitTest_20181019011102() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.printASCIIStatus(FIXED, "FIXED msg");
            assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains("DEBUG FIXED msg"));
        }

        @Test
        @DisplayName("Check ASCII print SUCCESS Status")
        void unitTest_20181019011120() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.printASCIIStatus(SUCCESS, "SUCCESS msg");
            assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains("INFO SUCCESS msg"));
        }

        @Test
        @DisplayName("Check ASCII print UNTESTED Status")
        void unitTest_20181019011215() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.printASCIIStatus(UNTESTED, "UNTESTED msg");
            assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains("INFO UNTESTED msg"));
        }

    }

    @Nested()
    @DisplayName("increment() tests")
    class IncrementTests extends BaseUnitTest {

        @Test
        @DisplayName("Check increment FAILED status")
        void unitTest_20181019004343() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.increment(FAILED);
            assertTestStatusCount(listener, 0, 0, 0, 0, 0, 0, 1, 0, 0);
        }

        @Test
        @DisplayName("Check increment CORRUPTED status")
        void unitTest_20181019005331() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.increment(CORRUPTED);
            assertTestStatusCount(listener, 0, 0, 1, 0, 0, 0, 0, 0, 0);
        }

        @Test
        @DisplayName("Check increment EXP_IMPL status")
        void unitTest_20181019005426() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.increment(EXP_IMPL);
            assertTestStatusCount(listener, 0, 0, 0, 0, 1, 0, 0, 0, 0);
        }

        @Test
        @DisplayName("Check increment EXP_FIX status")
        void unitTest_20181019005447() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.increment(EXP_FIX);
            assertTestStatusCount(listener, 0, 0, 0, 1, 0, 0, 0, 0, 0);
        }

        @Test
        @DisplayName("Check increment BLOCKED status")
        void unitTest_20181019005511() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.increment(BLOCKED);
            assertTestStatusCount(listener, 0, 0, 0, 0, 0, 1, 0, 0, 0);
        }

        @Test
        @DisplayName("Check increment IMPLEMENTED status")
        void unitTest_20181019005613() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.increment(IMPLEMENTED);
            assertTestStatusCount(listener, 0, 0, 0, 0, 0, 0, 0, 0, 1);
        }

        @Test
        @DisplayName("Check increment FIXED status")
        void unitTest_20181019005635() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.increment(FIXED);
            assertTestStatusCount(listener, 0, 0, 0, 0, 0, 0, 0, 1, 0);
        }

        @Test
        @DisplayName("Check increment SKIP status")
        void unitTest_20181019005659() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.increment(SKIP);
            assertTestStatusCount(listener, 0, 1, 0, 0, 0, 0, 0, 0, 0);
        }

        @Test
        @DisplayName("Check increment UNTESTED status")
        void unitTest_20181019005918() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.increment(UNTESTED);
            assertTestStatusCount(listener, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        @Test
        @DisplayName("Check increment SUCCESS status")
        void unitTest_20181019005938() {
            UnitTestBuggyExecutionListener listener = new UnitTestBuggyExecutionListener();
            listener.increment(SUCCESS);
            assertTestStatusCount(listener, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        @SuppressWarnings("SameParameterValue")
        private void assertTestStatusCount(UnitTestBuggyExecutionListener listener,
                                           int testCount,
                                           int skippedTests,
                                           int corruptedError,
                                           int expFixError,
                                           int expImplError,
                                           int blockedError,
                                           int newError,
                                           int fixed,
                                           int implemented) {
            assertThat(listener.getTestCount(), is(testCount));
            assertThat(listener.getSkippedTests(), is(skippedTests));
            assertThat(listener.getCorruptedError(), is(corruptedError));
            assertThat(listener.getExpFixError(), is(expFixError));
            assertThat(listener.getExpImplError(), is(expImplError));
            assertThat(listener.getBlockedError(), is(blockedError));
            assertThat(listener.getNewError(), is(newError));
            assertThat(listener.getFixed(), is(fixed));
            assertThat(listener.getImplemented(), is(implemented));
        }

    }

    @Test
    @DisplayName("Check checkDebugAndPrint when count = 0")
    void unitTest_20181019021242() {
        new UnitTestBuggyExecutionListener() {{
            checkDebugAndPrint("checkDebugAndPrint", 0);
        }};
        assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains(
                "INFO checkDebugAndPrint............................0"
        ));
    }

    @Test
    @DisplayName("Check checkDebugAndPrint when count = 10")
    void unitTest_20181019021752() {
        new UnitTestBuggyExecutionListener() {{
            checkDebugAndPrint("checkDebugAndPrint", 10);
        }};
        assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains(
                "DEBUG checkDebugAndPrint...........................10"
        ));
    }

    @Test
    @DisplayName("Check checkErrorAndPrint when count = 0")
    void unitTest_20181019021932() {
        new UnitTestBuggyExecutionListener() {{
            checkErrorAndPrint("checkErrorAndPrint", 0);
        }};
        assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains(
                "INFO checkErrorAndPrint............................0"
        ));
    }

    @Test
    @DisplayName("Check checkErrorAndPrint when count = 10")
    void unitTest_20181019021954() {
        new UnitTestBuggyExecutionListener() {{
            checkErrorAndPrint("checkErrorAndPrint", 10);
        }};
        assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains(
                "ERROR checkErrorAndPrint...........................10"
        ));
    }

    @Test
    @DisplayName("Check checkWarnAndPrint when count = 0")
    void unitTest_20181019022103() {
        new UnitTestBuggyExecutionListener() {{
            checkWarnAndPrint("checkWarnAndPrint", 0);
        }};
        assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains(
                "INFO checkWarnAndPrint.............................0"
        ));
    }

    @Test
    @DisplayName("Check checkWarnAndPrint when count = 10")
    void unitTest_20181019022107() {
        new UnitTestBuggyExecutionListener() {{
            checkWarnAndPrint("checkWarnAndPrint", 10);
        }};
        assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains(
                "WARN checkWarnAndPrint............................10"
        ));
    }

    @Test
    @DisplayName("Check checkTraceAndPrint when count = 0")
    void unitTest_20181019022204() {
        new UnitTestBuggyExecutionListener() {{
            checkTraceAndPrint("checkTraceAndPrint", 0);
        }};
        assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains(
                "INFO checkTraceAndPrint............................0"
        ));
    }

    @Test
    @DisplayName("Check checkTraceAndPrint when count = 10")
    void unitTest_20181019022306() {
        new UnitTestBuggyExecutionListener() {{
            checkTraceAndPrint("checkTraceAndPrint", 10);
        }};
        assertThat(TEST_LOGGER.takeLoggedMessagesWithLevel(), contains(
                "TRACE checkTraceAndPrint...........................10"
        ));
    }

    @Test
    @DisplayName("printTestStatistic when test run complete without errors")
    void unitTest_20181019023610() {
        new UnitTestBuggyExecutionListener(SYSTEM_OUT_LOGGER, SYSTEM_OUT_LOGGER, SYSTEM_OUT_LOGGER) {{
            testCount = new AtomicInteger(9);
            skippedTests = new AtomicInteger(0);
            corruptedError = new AtomicInteger(0);
            expFixError = new AtomicInteger(0);
            expImplError = new AtomicInteger(0);
            blockedError = new AtomicInteger(0);
            newError = new AtomicInteger(0);
            fixed = new AtomicInteger(0);
            implemented = new AtomicInteger(0);
            printTestStatistic();
        }};
        assertThat(SYSTEM_OUT_LOGGER.takeLoggedMessagesWithLevel(), contains(
                "INFO ===============================================",
                "INFO Total tests run...............................9",
                "INFO Successful tests..............................9",
                "INFO Skipped tests.................................0",
                "INFO Errors........................................0",
                "INFO ===============================================",
                "INFO Execution time.....................00:00:00,000",
                "INFO ==============================================="
        ));
    }

    @Test
    @DisplayName("printTestStatistic when test run complete with Buggy errors")
    void unitTest_20181019024931() {
        Buggy.incrementBuggyErrors();
        new UnitTestBuggyExecutionListener(SYSTEM_OUT_LOGGER, SYSTEM_OUT_LOGGER, SYSTEM_OUT_LOGGER) {{
            testCount = new AtomicInteger(9);
            skippedTests = new AtomicInteger(0);
            corruptedError = new AtomicInteger(0);
            expFixError = new AtomicInteger(0);
            expImplError = new AtomicInteger(0);
            blockedError = new AtomicInteger(0);
            newError = new AtomicInteger(0);
            fixed = new AtomicInteger(0);
            implemented = new AtomicInteger(0);
            printTestStatistic();
        }};
        assertThat(SYSTEM_OUT_LOGGER.takeLoggedMessagesWithLevel(), contains(
                "INFO ===============================================",
                "ERROR Framework errors..............................1",
                "INFO ===============================================",
                "INFO Total tests run...............................9",
                "INFO Successful tests..............................9",
                "INFO Skipped tests.................................0",
                "INFO Errors........................................0",
                "INFO ===============================================",
                "INFO Execution time.....................00:00:00,000",
                "INFO ==============================================="
        ));
    }

    @Test
    @DisplayName("printTestStatistic when test run complete with Buggy warns")
    void unitTest_20181019025058() {
        Buggy.incrementBuggyWarns();
        new UnitTestBuggyExecutionListener(SYSTEM_OUT_LOGGER, SYSTEM_OUT_LOGGER, SYSTEM_OUT_LOGGER) {{
            testCount = new AtomicInteger(9);
            skippedTests = new AtomicInteger(0);
            corruptedError = new AtomicInteger(0);
            expFixError = new AtomicInteger(0);
            expImplError = new AtomicInteger(0);
            blockedError = new AtomicInteger(0);
            newError = new AtomicInteger(0);
            fixed = new AtomicInteger(0);
            implemented = new AtomicInteger(0);
            printTestStatistic();
        }};
        assertThat(SYSTEM_OUT_LOGGER.takeLoggedMessagesWithLevel(), contains(
                "INFO ===============================================",
                "WARN Framework warns...............................1",
                "INFO ===============================================",
                "INFO Total tests run...............................9",
                "INFO Successful tests..............................9",
                "INFO Skipped tests.................................0",
                "INFO Errors........................................0",
                "INFO ===============================================",
                "INFO Execution time.....................00:00:00,000",
                "INFO ==============================================="
        ));
    }

    @Test
    @DisplayName("printTestStatistic when test run complete with errors")
    void unitTest_20181019024528() {
        new UnitTestBuggyExecutionListener(SYSTEM_OUT_LOGGER, SYSTEM_OUT_LOGGER, SYSTEM_OUT_LOGGER) {{
            testCount = new AtomicInteger(100500);
            skippedTests = new AtomicInteger(1);
            corruptedError = new AtomicInteger(1);
            expFixError = new AtomicInteger(1);
            expImplError = new AtomicInteger(1);
            blockedError = new AtomicInteger(1);
            newError = new AtomicInteger(1);
            fixed = new AtomicInteger(1);
            implemented = new AtomicInteger(1);
            printTestStatistic();
        }};
        assertThat(SYSTEM_OUT_LOGGER.takeLoggedMessagesWithLevel(), contains(
                "INFO ===============================================",
                "INFO Total tests run..........................100500",
                "INFO Successful tests.........................100495",
                "WARN Skipped tests.................................1",
                "WARN Failed tests..................................5",
                "ERROR New Errors....................................1",
                "INFO ===============================================",
                "WARN Waiting to fix a defect.......................1",
                "WARN Waiting for implementation....................1",
                "WARN Blocked tests.................................1",
                "ERROR Corrupted tests...............................1",
                "INFO ===============================================",
                "DEBUG Fixed cases...................................1",
                "DEBUG Implemented cases.............................1",
                "INFO ===============================================",
                "INFO Execution time.....................00:00:00,000",
                "INFO ==============================================="
        ));
    }

    @Test
    @DisplayName("Successful copyFile call when file exists")
    void unitTest_20181019025312() throws IOException {
        File source = new File(WASTE, "unitTest_20181019025312.source");
        //noinspection ResultOfMethodCallIgnored
        source.createNewFile();
        File dist = new File(WASTE, "unitTest_20181019025312.dist");
        new BuggyExecutionListener(SYSTEM_OUT_LOGGER, SYSTEM_OUT_LOGGER, SYSTEM_OUT_LOGGER) {{
            copyFile(source, dist);
        }};
        assertThat(dist.exists(), is(true));
    }

}
