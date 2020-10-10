package org.touchbit.buggy.core.logback.appender;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.LoggerFactory;
import org.touchbit.buggy.core.logback.layout.TestSupervisorLoggerLayout;

import java.io.File;

public class TestSupervisorFileAppender<E extends ILoggingEvent> extends BaseBuggyFileAppender<E> {

    public TestSupervisorFileAppender() {
        super.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        super.setLayout(new TestSupervisorLoggerLayout<>());
        super.setFile(new File(LOG_DIR, "TestSupervisor.log").getPath());
        super.append = true;
    }

}
