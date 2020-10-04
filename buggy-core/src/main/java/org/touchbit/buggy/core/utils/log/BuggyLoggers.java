package org.touchbit.buggy.core.utils.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.File;
import java.util.Iterator;

public class BuggyLoggers {

    public static final String CONSOLE_LOGGER_NAME = "Console";
    public static final String FRAMEWORK_LOGGER_NAME = "Framework";
    public static final String SIFTING_LOGGER_NAME = "Sifting";
    public static final String CONFIG_PROPERTY = "logging.config";
    public static final String SIFTING_LOG_FILE_PATH = "sifting.log.file.path";

    public static final Logger C_LOG = LoggerFactory.getLogger(CONSOLE_LOGGER_NAME);
    public static final Logger CONSOLE = C_LOG;
    public static final Logger F_LOG = LoggerFactory.getLogger(FRAMEWORK_LOGGER_NAME);
    public static final Logger FRAMEWORK = F_LOG;
    public static final Logger S_LOG = LoggerFactory.getLogger(SIFTING_LOGGER_NAME);
    public static final Logger SIFTING = S_LOG;
    public static final String LOGGING_CONFIG_FILE = System.getProperty(CONFIG_PROPERTY, "buggy-logback.xml");
    public static final LoggerContext LOGGER_CONTEXT = (LoggerContext) LoggerFactory.getILoggerFactory();

    public static String getFrameworkLogFilePath() {
        return getLogFilePath(FRAMEWORK_LOGGER_NAME);
    }

    public static String getLogFilePath(String loggerName) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
            for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext();) {
                Appender<ILoggingEvent> appender = index.next();
                if (appender instanceof FileAppender && appender.getName().equalsIgnoreCase(loggerName)) {
                    FileAppender<ILoggingEvent> fileAppender = (FileAppender<ILoggingEvent>) appender;
                    return fileAppender.getFile();
                }
            }
        }
        return null;
    }

    public static void setSiftingLogFilePath(String dir, String filename) {
        setSiftingLogFilePath(new File(dir, filename));
    }

    public static void setSiftingLogFilePath(String filePath) {
        setSiftingLogFilePath(new File(filePath));
    }

    public static void setSiftingLogFilePath(File file) {
        MDC.put(SIFTING_LOG_FILE_PATH, file.getPath());
    }

    protected BuggyLoggers() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }

}
