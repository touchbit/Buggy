package org.touchbit.buggy.core.log;

import org.slf4j.MDC;
import org.touchbit.buggy.core.log.appender.DecomposeTestLogsFileAppender;
import org.touchbit.buggy.core.model.IStatus;

import java.io.File;

public class SiftingTestLogger extends BaseLogbackWrapper {

    public static final String SIFTING_LOG_FILE_PATH = "sifting.test.log.file.path";
    public static final String SIFTING_LOG_DIR = "tests";

    public SiftingTestLogger() {
        super(SIFTING_LOGGER_NAME);
    }

    public static void setTestLogFileName(String logFileName) {
        setTestLogFile(new File(SIFTING_LOG_DIR, logFileName));
    }

    public static void setTestLogFile(File file) {
        MDC.put(SIFTING_LOG_FILE_PATH, file.getPath());
    }

    public static File getSiftingLogFile(String fileName) {
        return DecomposeTestLogsFileAppender.getFile(fileName);
    }

    public static void setTestStatus(String fileName, IStatus status) {
        DecomposeTestLogsFileAppender.setTestStatus(fileName, status);
    }
}
