package org.touchbit.buggy.core.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.touchbit.buggy.core.utils.JUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class BaseLogbackWrapper extends MarkerIgnoringBase {

    public static final String LOG_PATH = "LOG_PATH";
    protected static final String CONSOLE_LOGGER_NAME = "Console";
    protected static final String FRAMEWORK_LOGGER_NAME = "Framework";
    protected static final String SIFTING_LOGGER_NAME = "Sifting";
    private static final String LOGGING_CONFIG = "logging.config";
    private static final String LOGBACK_CONFIG = "logback.configurationFile";

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

    private final Logger logger;

    public BaseLogbackWrapper(final String name) {
        this.name = name;
        this.logger = LoggerFactory.getLogger(name);
    }

    public static String getConfFileName() {
        return System.getProperty(LOGGING_CONFIG, System.getProperty(LOGBACK_CONFIG, "undefined"))
                .replace("classpath:", "");
    }

    public static String getInCaseOfErrorsOrWarnings() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(stream);
        StatusPrinter.setPrintStream(printStream);
        StatusPrinter.printInCaseOfErrorsOrWarnings(getLoggerContext());
        return stream.toString();
    }

    public static List<ch.qos.logback.classic.Logger> getLoggerList() {
        return getLoggerContext().getLoggerList();
    }

    public static LoggerContext getLoggerContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        logger.trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        logger.trace(format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        logger.trace(format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        logger.trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        logger.trace(msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        logger.debug(format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        logger.debug(format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        logger.debug(format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        logger.debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        logger.info(format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        logger.info(format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        logger.info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        logger.info(msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        logger.warn(format, arg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        logger.warn(format, arguments);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        logger.warn(format, arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        logger.warn(msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        logger.error(format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        logger.error(format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        logger.error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }
}
