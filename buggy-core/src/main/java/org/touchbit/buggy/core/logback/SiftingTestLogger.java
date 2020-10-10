package org.touchbit.buggy.core.logback;

import org.testng.IInvokedMethod;
import org.testng.ITestNGMethod;
import org.touchbit.buggy.core.logback.appender.SiftingFileAppender;
import org.touchbit.buggy.core.model.ResultStatus;

import java.io.File;

public class SiftingTestLogger extends BaseLogbackWrapper {

    public SiftingTestLogger() {
        super(SIFTING_LOGGER_NAME);
    }

    public static void setTestLogFileName(IInvokedMethod method) {
        if (method != null) {
            setTestLogFileName(method.getTestMethod());
        }
    }

    public static void setTestLogFileName(ITestNGMethod iTestNGMethod) {
        if (iTestNGMethod != null) {
            String name = iTestNGMethod.getConstructorOrMethod().getMethod().getName();
            SiftingFileAppender.setTestLogFileName(name);
        }
    }

    public static File getSiftingLogFile() {
        return SiftingFileAppender.getTestLogFile();
    }

    public static void setTestResultStatus(ResultStatus status) {
        SiftingFileAppender.setTestResultStatus(status);
    }

}
