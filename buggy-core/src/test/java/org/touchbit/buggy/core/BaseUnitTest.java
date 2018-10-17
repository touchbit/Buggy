package org.touchbit.buggy.core;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.slf4j.helpers.SubstituteLogger;
import org.testng.IInvokedMethod;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import org.testng.internal.ConstructorOrMethod;
import org.touchbit.buggy.core.config.PrimaryConfig;
import org.touchbit.buggy.core.indirect.TestExitHandler;
import org.touchbit.buggy.core.indirect.TestInterface;
import org.touchbit.buggy.core.indirect.TestService;
import org.touchbit.buggy.core.indirect.UnitTestPrimaryConfig;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.testng.listeners.BuggyExecutionListener;
import org.touchbit.buggy.core.testng.listeners.BuggyExecutionListenerTests;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.slf4j.helpers.NOPLogger.NOP_LOGGER;
import static org.touchbit.buggy.core.model.Status.SUCCESS;
import static org.touchbit.buggy.core.model.Type.SYSTEM;
import static org.touchbit.buggy.core.utils.log.BuggyLog.LOG_DIRECTORY;

/**
 * Created by Oleg Shaburov on 15.09.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseUnitTest {

    protected static final Logger LOG = NOP_LOGGER;
    protected static final String WASTE;
    protected static final String CLASSES;
    protected static final String TEST_CLASSES;
    protected static final TestExitHandler EXIT_HANDLER = new TestExitHandler();

    protected static final PrimaryConfig PRIMARY_CONFIG;

    static {
        Buggy.setExitHandler(EXIT_HANDLER);
        Buggy.setPrimaryConfigClass(UnitTestPrimaryConfig.class);
        Buggy.initBuggyConfiguration();
        WASTE = Buggy.getRunDir() + "/waste-unit-tests";
        CLASSES = Buggy.getRunDir() + "/classes";
        TEST_CLASSES = Buggy.getRunDir() + "/test-classes";
        System.setProperty(LOG_DIRECTORY, WASTE + "/");
        Buggy.initJCommander();
        PRIMARY_CONFIG = Buggy.getPrimaryConfig();
        PRIMARY_CONFIG.setAbsoluteLogPath(WASTE);
        File wasteDir = new File(WASTE);
        File logFile = new File(WASTE, "console.txt");
        delete(wasteDir);
        try {
            if (wasteDir.mkdirs() && logFile.createNewFile()) {
                System.out.println("\n\nSystem.out log file: " + logFile.getAbsolutePath() + "\n\n");
                System.setOut(outputFile(logFile));
                System.setErr(outputFile(logFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void clean() {
        EXIT_HANDLER.clean();
        BuggyExecutionListener.setSteps(new ArrayList<>());
    }

    protected void assertExitCode(Integer code) {
        assertExitCode(code, null, null);
    }

    protected void assertExitCode(Integer code, String msg) {
        assertExitCode(code, msg, null);
    }

    protected void assertExitCode(Integer code, String msg, Class<Throwable> throwableClass) {
        if (code == null) {
            assertThat(EXIT_HANDLER.getStatus(), is(nullValue()));
        } else {
            assertThat(EXIT_HANDLER.getStatus(), is(code));
        }
        if (msg == null) {
            assertThat(EXIT_HANDLER.getMsg(), is(nullValue()));
        } else {
            assertThat(EXIT_HANDLER.getMsg(), is(msg));
        }
        if (throwableClass == null) {
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        } else {
            assertThat(EXIT_HANDLER.getThrowable(), is(instanceOf(throwableClass)));
        }
    }

    private static PrintStream outputFile(File f) throws FileNotFoundException {
        return new PrintStream(new BufferedOutputStream(new FileOutputStream(f)), true);
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    private static void delete(File file) {
        if (file.exists() && file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                delete(listFile);
            }
        }
        file.delete();
    }

    protected void checkUtilityClassConstructor(Class<?> clazz) throws NoSuchMethodException {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        Throwable exception = execute(constructor::newInstance);
        assertThat(exception.getCause(), instanceOf(IllegalStateException.class));
        assertThat(exception.getCause().getMessage(), is("Utility class. Prohibit instantiation."));
    }

    @SuppressWarnings({"unchecked", "WeakerAccess"})
    protected Throwable execute(Executable executable) {
        Throwable throwable = null;
        try {
            executable.execute();
        } catch (Throwable e) {
            throwable = e;
        }
        assertThat("Exception is present", throwable != null, is(true));
        return throwable;
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    protected <T> T execute(Executable executable, Class<T> exceptionClass) {
        Throwable throwable = null;
        try {
            executable.execute();
        } catch (Throwable e) {
            throwable = e;
        }
        assertThat(throwable, instanceOf(exceptionClass));
        return (T) throwable;
    }

    protected static Details getDetails(Type type) {
        return getDetails(SUCCESS, type);
    }

    protected static Details getDetails(Status status, String... issue) {
        return getDetails(status, SYSTEM, issue);
    }

    protected static Details getDetails(Status status, Type type, String... issue) {
        return getDetails(new int[0], status, type, issue);
    }

    protected static Details getDetails(int[] ids, Status status, Type type, String... issue) {
        return new Details() {
            @Override
            public int[] id() { return ids; }
            @Override
            public Status status() { return status; }
            @Override
            public String[] issue() { return issue; }
            @Override
            public Type type() { return type; }
            @Override
            public Class<? extends Annotation> annotationType() { return Details.class; }
        };
    }

    public static class Log extends SubstituteLogger {

        public String msg;

        public Log() {
            super(null, null, true);
        }

        @Override
        public void info(String msg) {
            if (this.msg == null) {
                this.msg = msg;
            } else {
                this.msg = this.msg + "\n" + msg;
            }
        }

    }

    protected static IInvokedMethod getMockIInvokedMethod() {
        return getMockIInvokedMethod(true);
    }

    protected static IInvokedMethod getMockIInvokedMethod(boolean isTest) {
        return getMockIInvokedMethod(BuggyExecutionListenerTests.MockITestClass.class, "iTestResultMethodWithDetails", isTest);
    }

    @SuppressWarnings("SameParameterValue")
    protected static IInvokedMethod getMockIInvokedMethod(Class<?> clazz, String methodName, boolean isTest) {
        ITestNGMethod iTestNGMethod = getMockITestNGMethod(clazz, methodName, isTest);
        IInvokedMethod iInvokedMethod = mock(IInvokedMethod.class);
        when(iInvokedMethod.getTestMethod()).thenReturn(iTestNGMethod);
        when(iInvokedMethod.isTestMethod()).thenReturn(isTest);
        return iInvokedMethod;
    }

    protected static ITestNGMethod getMockITestNGMethod(Class<?> clazz, String methodName, boolean isTest) {
        ITestNGMethod method = getMockITestNGMethod(clazz, methodName);
        when(method.isTest()).thenReturn(isTest);
        return method;
    }

    protected static ITestNGMethod getMockITestNGMethod() {
        return getMockITestNGMethod(MockITestClass.class, "iTestResultMethodWithDetails");
    }

    protected static ITestNGMethod getMockITestNGMethod(Class<?> clazz, String methodName) {
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

    @SuppressWarnings("unchecked")
    protected static ITestClass getMockITestClass(Class aClass) {
        ITestClass iTestClass = mock(ITestClass.class);
        when(iTestClass.getRealClass()).thenReturn(aClass);
        return iTestClass;
    }

    protected static ITestResult getMockITestResult(Integer status) {
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

    protected class UnitTestBuggyExecutionListener extends BuggyExecutionListener {

        public ITestNGMethod method;
        public Status status;
        public String msg;
        public Details details;

        public UnitTestBuggyExecutionListener() {
            super(LOG, LOG, LOG);
        }

        public UnitTestBuggyExecutionListener(Details details) {
            super(LOG, LOG, LOG);
            this.details = details;
        }

        public UnitTestBuggyExecutionListener(Logger testLogger, Logger frameworkLogger, Logger consoleLogger) {
            super(testLogger, frameworkLogger, consoleLogger);
        }

        @Override
        public void resultLog(ITestNGMethod method, Status status, String details) {
            this.method = method;
            this.status = status;
            this.msg = details;
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
        @Test
        @Details
        public void iTestResultMethodWithDetails() { }
        @SuppressWarnings({"WeakerAccess", "unused"})
        public void iTestResultMethodWithoutDetails() {
            // for getMockITestResult()
        }
    }

}
