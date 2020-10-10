package org.touchbit.buggy.core.logback.appender;

import ch.qos.logback.core.FileAppender;
import org.touchbit.buggy.core.utils.JUtils;

import java.io.File;

public abstract class BaseBuggyFileAppender<E> extends FileAppender<E> {

    public static final String LOG_PATH = "LOG_PATH";

    static {
        if (System.getProperty("logback.configurationFile") == null) {
            System.setProperty("logback.configurationFile", "buggy-logback.xml");
        }
        if (JUtils.isJetBrainsIdeTestNGPluginRun()) {
            System.setProperty(LOG_PATH, "target/logs");
        } else if (JUtils.isJetBrainsIdeConsoleRun()) {
            System.setProperty(LOG_PATH, JUtils.getJetBrainsIdeConsoleRunTargetPath() + "/logs");
        } else {
            System.setProperty(LOG_PATH, "logs");
        }
    }

    public static final File LOG_DIR = new File(getLogPath());

    public static final File TEST_DIR = new File(LOG_DIR, "tests");
    public static final File FIXED_DIR = new File(LOG_DIR, "fixed");
    public static final File IMPLEMENTED_DIR = new File(LOG_DIR, "implemented");

    public static final File ERRORS = new File(LOG_DIR, "errors");
    public static final File NEW_DIR = new File(ERRORS, "new");
    public static final File CORRUPTED_DIR = new File(ERRORS, "corrupted");
    public static final File BLOCKED_DIR = new File(ERRORS, "blocked");
    public static final File EXP_FIX_DIR = new File(ERRORS, "exp_fix");
    public static final File EXP_IMPL_DIR = new File(ERRORS, "exp_impl");
    public static final String LOG_EXT = ".log";

    public static String getLogPath() {
        return System.getProperty(LOG_PATH, "logs");
    }

}
