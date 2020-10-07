package org.touchbit.buggy.core.log;

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
            List<FileAppender<ILoggingEvent>> collect = Stream.generate(logger.iteratorForAppenders()::next)
                    .limit(1000)
                    .filter(appender -> appender instanceof FileAppender)
                    .filter(fileAppender -> fileAppender.getName().equals(FRAMEWORK_LOGGER_NAME))
                    .map(f -> (FileAppender<ILoggingEvent>) f)
                    .collect(Collectors.toList());
            if (!collect.isEmpty()) {
                return new File(collect.get(1).getFile());
            }
        }
        return null;
    }

}
