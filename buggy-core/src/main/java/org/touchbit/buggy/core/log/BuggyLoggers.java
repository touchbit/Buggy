package org.touchbit.buggy.core.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.touchbit.buggy.core.utils.JUtils;

import java.io.File;
import java.util.Iterator;

public class BuggyLoggers {

    static {
        if (JUtils.isJetBrainsIdeRun()) {
            System.setProperty("LOG_PATH", "target/logs");
        } else {
            System.setProperty("LOG_PATH", "logs");
        }
        System.setProperty("logging.config", "buggy-logback.xml");
        System.setProperty("logback.configurationFile", "buggy-logback.xml");
    }

    public static final String CONSOLE_LOGGER_NAME = "Console";
    public static final String FRAMEWORK_LOGGER_NAME = "Framework";
    public static final String SIFTING_LOGGER_NAME = "Sifting";
    public static final String CONFIG_PROPERTY = "logging.config";
    public static final String SIFTING_LOG_FILE_PATH = "sifting.log.file.path";

    public static final Logger CONSOLE = LoggerFactory.getLogger(CONSOLE_LOGGER_NAME);
    public static final Logger FRAMEWORK = LoggerFactory.getLogger(FRAMEWORK_LOGGER_NAME);
    public static final Logger SIFTING = LoggerFactory.getLogger(SIFTING_LOGGER_NAME);
    public static final String LOGGING_CONFIG_FILE = System.getProperty(CONFIG_PROPERTY, "buggy-logback.xml");
    public static final LoggerContext LOGGER_CONTEXT = (LoggerContext) LoggerFactory.getILoggerFactory();

    protected BuggyLoggers() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }

    public static String getFrameworkLogFilePath() {
        return getLogFilePath(FRAMEWORK_LOGGER_NAME);
    }

    public static String getLogFilePath(String loggerName) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
            for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext(); ) {
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

}
