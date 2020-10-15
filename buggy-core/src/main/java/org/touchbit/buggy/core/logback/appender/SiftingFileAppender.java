package org.touchbit.buggy.core.logback.appender;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.touchbit.buggy.core.logback.layout.ShiftingLoggerLayout;
import org.touchbit.buggy.core.model.IStatus;
import org.touchbit.buggy.core.model.ResultStatus;
import org.touchbit.buggy.core.utils.IOHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.touchbit.buggy.core.logback.appender.SiftingAppender.SIFTING_LOG_FILE_PATH;

public class SiftingFileAppender<E extends ILoggingEvent> extends BaseBuggyFileAppender<E> {

    private static final List<TestLog> TEST_LOGS = new ArrayList<>();

    public SiftingFileAppender() {
        super.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        super.setLayout(new ShiftingLoggerLayout<>());
        super.append = false;
    }

    public static synchronized void setTestLogFileName(String logFileName) {
        List<TestLog> testLogs = TEST_LOGS.stream()
                .filter(l -> l.getFileName().contains(logFileName))
                .collect(Collectors.toList());
        String fileName = logFileName + "_" + testLogs.size();
        File logFile = new File(TEST_DIR, fileName + LOG_EXT);
        TestLog testLog = new TestLog();
        testLog.setFileName(fileName);
        testLog.setFile(logFile);
        TEST_LOGS.add(testLog);
        MDC.put(SIFTING_LOG_FILE_PATH, fileName);
    }

    @Override
    public String getFile() {
        File testLogFile = getTestLogFile();
        if (testLogFile != null) {
            return testLogFile.getPath();
        }
        return null;
    }

    public static File getTestLogFile() {
        String logFile = MDC.get(SIFTING_LOG_FILE_PATH);
        return TEST_LOGS.stream()
                .filter(f -> f.getFileName().equals(logFile))
                .map(TestLog::getFile)
                .findFirst().orElse(null);
    }

    public static void setTestResultStatus(ResultStatus status) {
        String logFile = MDC.get(SIFTING_LOG_FILE_PATH);
        TEST_LOGS.stream()
                .filter(f -> f.getFileName().equals(logFile))
                .forEach(f -> f.setResultStatus(status));
    }

    public static void decomposeTestLogs() {
        for (TestLog testLog : TEST_LOGS) {
            ResultStatus resultStatus = testLog.getResultStatus();
            String fileName = testLog.getFileName();
            File destFile = null;
            if (resultStatus != null) {
                switch (resultStatus) {
                    case FAILED:
                        destFile = new File(NEW_DIR, fileName);
                        break;
                    case CORRUPTED:
                        destFile = new File(CORRUPTED_DIR, fileName);
                        break;
                    case BLOCKED:
                        destFile = new File(BLOCKED_DIR, fileName);
                        break;
                    case FIXED:
                        destFile = new File(FIXED_DIR, fileName);
                        break;
                    case IMPLEMENTED:
                        destFile = new File(IMPLEMENTED_DIR, fileName);
                        break;
                    case EXP_FIX:
                        destFile = new File(EXP_FIX_DIR, fileName);
                        break;
                    case EXP_IMPL:
                        destFile = new File(EXP_IMPL_DIR, fileName);
                        break;
                    case SUCCESS:
                    case SKIP:
                    default:
                        // do nothing
                }
            }
            if (destFile != null) {
                try {
                    IOHelper.copyFile(testLog.getFile(), destFile);
                } catch (Exception e) {
                    // ignore todo
                }
            }
        }
    }

}
