package org.touchbit.buggy.core.logback.appender;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.slf4j.LoggerFactory;
import org.touchbit.buggy.core.logback.layout.ConsoleLoggerColorLayout;

public class ConsoleColorAppender<E extends ILoggingEvent> extends ConsoleAppender<E> {

    public ConsoleColorAppender() {
        super.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        super.setLayout(new ConsoleLoggerColorLayout<>());
    }

}
