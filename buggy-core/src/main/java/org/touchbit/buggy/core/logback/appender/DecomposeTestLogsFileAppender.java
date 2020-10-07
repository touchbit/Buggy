package org.touchbit.buggy.core.logback.appender;

import ch.qos.logback.core.FileAppender;

import java.io.File;
import java.util.*;

import org.touchbit.buggy.core.model.IStatus;
import org.touchbit.buggy.core.model.ResultStatus;
import org.touchbit.buggy.core.utils.IOHelper;

import static org.touchbit.buggy.core.logback.BaseLogbackWrapper.LOG_PATH;
import static org.touchbit.buggy.core.model.Status.*;

public class DecomposeTestLogsFileAppender<E> extends FileAppender<E> {

    public static final Map<File, IStatus> TEST_LOGS_WITH_STATUS = new HashMap<>();
    public static final String LOG_DIR = System.getProperty(LOG_PATH, "logs");
    public static final File FIXED_DIR = new File(LOG_DIR, "fixed");
    public static final File IMPLEMENTED_DIR = new File(LOG_DIR, "implemented");
    public static final File ERRORS = new File(LOG_DIR, "errors");
    public static final File NEW_DIR = new File(ERRORS, "new");
    public static final File CORRUPTED_DIR = new File(ERRORS, "corrupted");
    public static final File BLOCKED_DIR = new File(ERRORS, "blocked");
    public static final File EXP_FIX_DIR = new File(ERRORS, "exp_fix");
    public static final File EXP_IMPL_DIR = new File(ERRORS, "exp_impl");

    @Override
    public void setFile(String file) {
        super.setFile(file);
        TEST_LOGS_WITH_STATUS.put(new File(file), UNTESTED);
    }

    public static File getFile(String fileName) {
        return TEST_LOGS_WITH_STATUS.entrySet().stream()
                .filter(e -> e.getKey().getName().equals(fileName))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static void setTestStatus(String fileName, IStatus status) {
        TEST_LOGS_WITH_STATUS.entrySet().stream()
                .filter(e -> e.getKey().getName().contains(fileName))
                .findFirst()
                .ifPresent(first -> TEST_LOGS_WITH_STATUS.put(first.getKey(), status));
    }

    public static void decomposeTestLogs() {
        for (Map.Entry<File, IStatus> log : TEST_LOGS_WITH_STATUS.entrySet()) {
            String fileName = log.getKey().getName();
            File destFile = null;
            switch (ResultStatus.valueOf(log.getValue().getStatus())) {
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
            if (destFile != null) {
                try {
                    IOHelper.copyFile(log.getKey(), destFile);
                } catch (Exception e) {
                    // ignore todo
                }
            }
        }
    }

}
