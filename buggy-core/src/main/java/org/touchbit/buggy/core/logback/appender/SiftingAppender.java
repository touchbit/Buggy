package org.touchbit.buggy.core.logback.appender;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.sift.MDCBasedDiscriminator;
import org.slf4j.LoggerFactory;

public class SiftingAppender extends ch.qos.logback.classic.sift.SiftingAppender {

    public static final String SIFTING_LOG_FILE_PATH = "sifting.test.log.file.path";

    public SiftingAppender() {
        super.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        MDCBasedDiscriminator discriminator = new MDCBasedDiscriminator();
        discriminator.setKey(SIFTING_LOG_FILE_PATH);
        discriminator.setDefaultValue("base-sifting");
        discriminator.start();
        super.setDiscriminator(discriminator);
    }

}
