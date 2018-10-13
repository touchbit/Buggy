package org.touchbit.buggy.core;

import mockit.Expectations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.helpers.SubstituteLogger;
import org.touchbit.buggy.core.config.PrimaryConfig;
import org.touchbit.buggy.core.indirect.SuppressException;
import org.touchbit.buggy.core.indirect.UnitTestPrimaryConfig;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.testng.listeners.BuggyExecutionListener;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.touchbit.buggy.core.model.Status.SUCCESS;
import static org.touchbit.buggy.core.model.Type.SYSTEM;
import static org.touchbit.buggy.core.utils.log.BuggyLog.LOG_DIRECTORY;

/**
 * Created by Oleg Shaburov on 15.09.2018
 * shaburov.o.a@gmail.com
 */
public abstract class BaseUnitTest {

    protected static final String WASTE;
    protected static final String CLASSES;
    protected static final String TEST_CLASSES;

    protected static final PrimaryConfig PRIMARY_CONFIG;

    static {
        Buggy.setPrimaryConfigClass(UnitTestPrimaryConfig.class);
        Buggy.initBuggyConfiguration();
        WASTE = Buggy.getRunDir() + "/waste-unit-tests";
        CLASSES = Buggy.getRunDir() + "/classes";
        TEST_CLASSES = Buggy.getRunDir() + "/test-classes";
        System.setProperty(LOG_DIRECTORY, WASTE + "/");
        Buggy.initJCommander();
        PRIMARY_CONFIG = Buggy.getPrimaryConfig();
        PRIMARY_CONFIG.setAbsolutePath(WASTE);
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
    public void stepReset() {
        BuggyExecutionListener.setSteps(new ArrayList<>());
    }

    private static PrintStream outputFile(File f) throws FileNotFoundException {
        return new PrintStream(new BufferedOutputStream(new FileOutputStream(f)), true);
    }

    protected void suppressSystemExit() {
        suppressSystemExit(false);
    }

    protected void suppressSystemExit(boolean withSuppressException) {
        new Expectations(System.class) {{
            System.exit(anyInt);
            if (withSuppressException) {
                result = new SuppressException();
            }
        }};
    }

    protected void suppressSystemExit(int status) {
        suppressSystemExit(status, false);
    }

    protected void suppressSystemExit(int status, boolean withSuppressException) {
        new Expectations(System.class) {{
            System.exit(status);
            if (withSuppressException) {
                result = new SuppressException();
            }
        }};
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
            this.msg = msg;
        }

    }

}
