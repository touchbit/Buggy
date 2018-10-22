package org.touchbit.buggy.core.tests;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.internal.ConstructorOrMethod;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.ExitHandler;
import org.touchbit.buggy.core.config.*;
import org.touchbit.buggy.core.helpful.SystemOutLogger;
import org.touchbit.buggy.core.helpful.UnitTestLogger;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.process.Component;
import org.touchbit.buggy.core.process.Interface;
import org.touchbit.buggy.core.process.Service;
import org.touchbit.buggy.core.testng.listeners.BuggyExecutionListener;
import org.touchbit.buggy.core.utils.log.BuggyLog;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.touchbit.buggy.core.model.Status.SUCCESS;
import static org.touchbit.buggy.core.model.Type.MODULE;
import static org.touchbit.buggy.core.model.Type.SYSTEM;
import static org.touchbit.buggy.core.utils.log.BuggyLog.LOG_DIRECTORY;

/**
 * Created by Oleg Shaburov on 15.09.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseUnitTest {

    private static String runDir = new File(BaseUnitTest.class.getProtectionDomain().getCodeSource().getLocation().getPath())
            .getParentFile().getAbsolutePath();

//    protected static final Logger LOG = NOP_LOGGER;
    protected static final String WASTE;
    protected static final String CLASSES;
    protected static final String TEST_CLASSES;
    protected static final UnitTestLogger SYSTEM_OUT_LOGGER;
    protected static final UnitTestLogger TEST_LOGGER = new UnitTestLogger();
    protected static final TestExitHandler EXIT_HANDLER = new TestExitHandler();

    static {
        WASTE = runDir + "/waste-unit-tests";
        CLASSES = runDir + "/classes";
        TEST_CLASSES = runDir + "/test-classes";
        System.setProperty(LOG_DIRECTORY, WASTE + "/");
        File wasteDir = new File(WASTE);
        File logFile = new File(WASTE, "console.txt");
        delete(wasteDir);
        wasteDir.mkdirs();
        SYSTEM_OUT_LOGGER = new SystemOutLogger(logFile);
        Buggy.setBuggyLogClass(TestBuggyLog.class);
        Buggy.setBuggyProcessor(new UnitTestBuggyProcessor());
        Buggy.setPrimaryConfigClass(UnitTestPrimaryConfig.class);
        Buggy.prepare();
    }

    @BeforeEach
    public void clean() {
        Buggy.setDefault();
        Buggy.setBuggyProcessor(new UnitTestBuggyProcessor());
        Buggy.setPrimaryConfigClass(UnitTestPrimaryConfig.class);
        Buggy.setSecondaryConfigClasses(new ArrayList<>());
        Buggy.setBuggyLogClass(TestBuggyLog.class);
        EXIT_HANDLER.clean();
        TEST_LOGGER.reset();
        SYSTEM_OUT_LOGGER.reset();
        Buggy.getPrimaryConfig().setCheck(false);
        Buggy.getPrimaryConfig().setAbsoluteLogPath(WASTE);
        Buggy.getPrimaryConfig().setPrintAllParameters(false);
        Buggy.getPrimaryConfig().setPrintCause(false);
        Buggy.getPrimaryConfig().setPrintSuite(false);
        Buggy.getPrimaryConfig().setPrintLogFile(false);
        Buggy.getPrimaryConfig().setArtifactsUrl(null);
        BuggyExecutionListener.setSteps(new ArrayList<>());
        new BuggyExecutionListener() {};
    }

    public static class TestBuggyLog extends BuggyLog {

        public TestBuggyLog() {
            super(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
        }

    }

    public static class UnitTestBuggyProcessor extends Buggy.DefaultBuggyProcessor {
        @Override
        public TestExitHandler getExitHandler() {
            return EXIT_HANDLER;
        }

        @Override
        public String getReportsOutputDirectory() {
            return WASTE + "/reports";
        }

        public String getRealReportsOutputDirectory() {
            return super.getReportsOutputDirectory();
        }

        public ExitHandler getRealExitHandler() {
            return super.getExitHandler();
        }
    }

    protected void assertExitCode(Integer code) {
        assertExitCode(code, null, null);
    }

    @SuppressWarnings("SameParameterValue")
    protected void assertExitCode(Integer code, String msg) {
        assertExitCode(code, msg, null);
    }

    @SuppressWarnings("SameParameterValue")
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

    protected static IInvokedMethod getMockIInvokedMethod() {
        return getMockIInvokedMethod(true);
    }

    protected static IInvokedMethod getMockIInvokedMethod(boolean isTest) {
        return getMockIInvokedMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithDetails", isTest);
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
        return getMockITestNGMethod(TestNGTestClassWithSuite.class, "iTestResultMethodWithDetails");
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
            method = TestNGTestClassWithSuite.class.getMethod("iTestResultMethodWithDetails");
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

    protected BuggyExecutionListener getBuggyExecutionListener() {
        return getBuggyExecutionListener(true);
    }

    protected BuggyExecutionListener getBuggyExecutionListener(boolean withOverrideCopyFile) {

        return new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER) {

            @Override
            public void copyFile(File sourceFile, File destFile) throws IOException {
                if (withOverrideCopyFile) {
                    // do nothing
                } else {
                    super.copyFile(sourceFile, destFile);
                }
            }

        };

    }

    protected class UnitTestBuggyExecutionListener extends BuggyExecutionListener {

        public ITestNGMethod method;
        public Status status;
        public String msg;
        public Details details;
        public File sourceFile;
        public File targetFile;

        public UnitTestBuggyExecutionListener() {
            this(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
        }

        public UnitTestBuggyExecutionListener(Details details) {
            this(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER, details);
        }

        public UnitTestBuggyExecutionListener(Logger testLogger, Logger frameworkLogger, Logger consoleLogger) {
            this(testLogger, frameworkLogger, consoleLogger, null);
        }

        public UnitTestBuggyExecutionListener(Logger testLogger, Logger frameworkLogger, Logger consoleLogger, Details details) {
            super(testLogger, frameworkLogger, consoleLogger);
            this.details = details;
            testCount.set(0);
            skippedTests.set(0);
            corruptedError.set(0);
            expFixError.set(0);
            expImplError.set(0);
            blockedError.set(0);
            newError.set(0);
            fixed.set(0);
            implemented.set(0);
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

        @Override
        public void copyFile(File sourceFile, File destFile) {
            this.sourceFile = sourceFile;
            this.targetFile = destFile;
        }

        @Override
        public void printASCIIStatus(Status status, String msg) {
            super.printASCIIStatus(status, msg);
        }

        @Override
        public void increment(Status status) {
            super.increment(status);
        }

    }

    protected static Details getDetails() {
        return getDetails(SUCCESS, MODULE);
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

    protected static Suite getSuite() {
        return new Suite() {

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
            public String task() {
                return UUID.randomUUID().toString();
            }
        };
    }

}
