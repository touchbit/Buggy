package org.touchbit.buggy.core.logback.appender;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.LoggerFactory;
import org.touchbit.buggy.core.logback.layout.FrameworkLoggerLayout;

import java.io.File;

public class FrameworkFileAppender<E extends ILoggingEvent> extends BaseBuggyFileAppender<E> {

    public FrameworkFileAppender() {
        super.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        super.setLayout(new FrameworkLoggerLayout<>());
        super.setFile(new File(LOG_DIR, "Framework" + LOG_EXT).getPath());
        super.append = true;
    }

}
