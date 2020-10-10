package org.touchbit.buggy.core.tests;

import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.internal.ConstructorOrMethod;
import org.touchbit.buggy.core.config.TestComponent;
import org.touchbit.buggy.core.config.TestInterface;
import org.touchbit.buggy.core.config.TestNGTestClassWithSuite;
import org.touchbit.buggy.core.config.TestService;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.helpful.UnitTestLogger;
import org.touchbit.buggy.core.model.Buggy;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.testng.BuggyExecutionListener;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.touchbit.buggy.core.model.Status.SUCCESS;
import static org.touchbit.buggy.core.model.Type.MODULE;
import static org.touchbit.buggy.core.model.Type.SYSTEM;

/**
 * Created by Oleg Shaburov on 15.09.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings({"WeakerAccess", "ResultOfMethodCallIgnored"})
public abstract class BaseUnitTest {

    //    protected static final Logger LOG = NOP_LOGGER;
    protected static final UnitTestLogger TEST_LOGGER = new UnitTestLogger();
    private static String runDir = new File(BaseUnitTest.class
            .getProtectionDomain().getCodeSource().getLocation().getPath())
            .getParentFile().getAbsolutePath();

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    private static void delete(File file) {
        if (file.exists() && file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                delete(listFile);
            }
        }
        file.delete();
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

    @SuppressWarnings("unused")
    protected static Buggy getDetails() {
        return getDetails(SUCCESS, MODULE);
    }

    protected static Buggy getDetails(Type type) {
        return getDetails(SUCCESS, type);
    }

    protected static Buggy getDetails(Status status, String... issue) {
        return getDetails(status, SYSTEM, issue);
    }

    protected static Buggy getDetails(Status status, Type type, String... issue) {
        return getDetails(new String[] {"0"}, status, new Type[]{type}, issue);
    }

    protected static Buggy getDetails(String[] ids, Status status, Type[] type, String... issue) {
        return new Buggy() {
            @Override
            public String value() {
                return null;
            }

            @Override
            public String testCase() {
                return null;
            }

            @Override
            public String[] caseIDs() {
                return ids;
            }

            @Override
            public String[] IDs() {
                return ids;
            }

            @Override
            public Status status() {
                return status;
            }

            @Override
            public String[] issues() {
                return issue;
            }

            @Override
            public String[] bugs() {
                return new String[]{};
            }

            @Override
            public Type[] types() {
                return type;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Buggy.class;
            }
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
            public String purpose() {
                return UUID.randomUUID().toString();
            }
        };
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

    protected BuggyExecutionListener getBuggyExecutionListener() {
        return getBuggyExecutionListener(true);
    }

    @SuppressWarnings("SameParameterValue")
    protected BuggyExecutionListener getBuggyExecutionListener(boolean withOverrideCopyFile) {

        return new BuggyExecutionListener(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER) {

//            @Override
//            public void copyFile(File sourceFile, File destFile) throws IOException {
//                if (!withOverrideCopyFile) {
//                    super.copyFile(sourceFile, destFile);
//                }
//            }

        };

    }

    protected class UnitTestBuggyExecutionListener extends BuggyExecutionListener {

        public ITestNGMethod method;
        public Status status;
        public String msg;
        public Buggy buggy;
        public File sourceFile;
        public File targetFile;

        public UnitTestBuggyExecutionListener() {
            this(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
        }

        public UnitTestBuggyExecutionListener(Buggy buggy) {
            this(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER, buggy);
        }

        public UnitTestBuggyExecutionListener(Logger testLogger, Logger frameworkLogger, Logger consoleLogger) {
            this(testLogger, frameworkLogger, consoleLogger, null);
        }

        public UnitTestBuggyExecutionListener(Logger testLogger, Logger frameworkLogger, Logger consoleLogger, Buggy buggy) {
            super(testLogger, frameworkLogger, consoleLogger);
            this.buggy = buggy;
        }

    }

}
