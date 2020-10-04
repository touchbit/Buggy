package org.touchbit.buggy.spring.boot.starter.conf;

import ch.qos.logback.core.util.StatusPrinter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.touchbit.buggy.core.utils.log.BuggyLoggers;
import org.touchbit.buggy.core.utils.log.ConfigurationLogger;
import org.touchbit.buggy.spring.boot.starter.BuggyRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Logback loggers configuration
 * <p>
 * Created by Oleg Shaburov on 30.09.2020
 * shaburov.o.a@gmail.com
 */
@Configuration()
@ConditionalOnNotWebApplication
public class LogbackConfiguration implements IConfiguration {

    private final boolean isLogbackConfigurationInitialized;

    public LogbackConfiguration(boolean isJCommanderConfigured) {
        if (!isJCommanderConfigured) {
            BuggyRunner.exit(1, "JCommander must be initialized");
        }
        ConfigurationLogger.blockDelimiter();
        ConfigurationLogger.centerBold("Logback configuration (" + BuggyLoggers.LOGGING_CONFIG_FILE + ")");
        ConfigurationLogger.stepDelimiter();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(stream);
        StatusPrinter.setPrintStream(printStream);
        StatusPrinter.printInCaseOfErrorsOrWarnings(BuggyLoggers.LOGGER_CONTEXT);
        BuggyLoggers.F_LOG.info(stream.toString());
        for (ch.qos.logback.classic.Logger logger : BuggyLoggers.LOGGER_CONTEXT.getLoggerList()) {
            if (logger.getLevel() != null) {
                String name = logger.getName();
                try {
                    Class<?> c = this.getClass().getClassLoader().loadClass(name);
                    name = c.getSimpleName() + ".class";
                } catch (ClassNotFoundException ignore) {
                }
                ConfigurationLogger.dotPlaceholder(name, logger.getLevel());
            }
        }
        isLogbackConfigurationInitialized = true;
    }

    @Bean()
    public boolean isLogbackConfigurationInitialized() {
        return isLogbackConfigurationInitialized;
    }

}
