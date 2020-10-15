package org.touchbit.buggy.core.logback;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.testng.IInvokedMethod;
import org.testng.ITestNGMethod;
import org.touchbit.buggy.core.logback.appender.SiftingFileAppender;
import org.touchbit.buggy.core.model.ResultStatus;

import java.io.File;
import java.util.*;

public class SiftingTestLogger extends BaseLogbackWrapper {

    private static final Map<String, List<String>> STEPS = Collections.synchronizedMap(new HashMap<>());

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

    @Nullable
    public static File getSiftingLogFile() {
        return SiftingFileAppender.getTestLogFile();
    }

    public static void setTestResultStatus(ResultStatus status) {
        SiftingFileAppender.setTestResultStatus(status);
    }

    public void step(@NotNull final String msg, @NotNull final Object... args) {
        int stepNum = getSteps().size() + 1;
        String msgBody = msg;
        for (Object s : args) {
            msgBody = msgBody.replaceFirst("\\{}", String.valueOf(s));
        }
        String stepInfo = "STEP " + stepNum + ": " + msgBody;
        this.info(" ---> {}", stepInfo);
        getSteps().add(stepInfo);
    }

    protected List<String> getSteps() {
        File file = getSiftingLogFile();
        if (file != null) {
            String path = file.getPath();
            return STEPS.computeIfAbsent(path, k -> new ArrayList<>());
        }
        return new ArrayList<>();
    }

    public void steps() {
        List<String> steps = getSteps();
        if (steps != null && steps.size() > 0) {
            StringJoiner sj = new StringJoiner("\n", "Playback steps:\n", "");
            steps.forEach(sj::add);
            this.info(sj.toString());
        } else {
            this.info("No playback steps.");
        }
    }
}
