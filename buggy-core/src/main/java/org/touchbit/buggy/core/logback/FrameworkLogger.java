package org.touchbit.buggy.core.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FrameworkLogger extends BaseLogbackWrapper {

    public FrameworkLogger() {
        super(FRAMEWORK_LOGGER_NAME);
    }

    public static File getLogFile() {
        for (ch.qos.logback.classic.Logger logger : getLoggerList()) {
            FileAppender<ILoggingEvent> appender = Stream.generate(logger.iteratorForAppenders()::next)
                    .limit(1000)
                    .filter(a -> a instanceof FileAppender)
                    .filter(fileAppender -> fileAppender.getName().equals(FRAMEWORK_LOGGER_NAME))
                    .map(f -> (FileAppender<ILoggingEvent>) f)
                    .findFirst().orElse(null);
            if (appender != null) {
                return new File(appender.getFile());
            }
        }
        return null;
    }

}
