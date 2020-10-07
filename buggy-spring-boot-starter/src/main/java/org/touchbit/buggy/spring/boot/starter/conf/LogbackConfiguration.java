package org.touchbit.buggy.spring.boot.starter.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.touchbit.buggy.core.logback.BaseLogbackWrapper;
import org.touchbit.buggy.core.logback.ConfLogger;
import org.touchbit.buggy.core.logback.FrameworkLogger;
import org.touchbit.buggy.spring.boot.starter.BuggyRunner;

import static org.springframework.boot.context.logging.LoggingApplicationListener.CONFIG_PROPERTY;

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

    public LogbackConfiguration(boolean isJCommanderConfigured, ConfigurableEnvironment environment) {
        String property = environment.getProperty(CONFIG_PROPERTY);

        if (property != null) {
            System.setProperty(CONFIG_PROPERTY, property);
        }
        if (!isJCommanderConfigured) {
            BuggyRunner.exit(1, "JCommander must be initialized");
        }
        ConfLogger.blockDelimiter();
        ConfLogger.centerBold("Logback configuration (" + BaseLogbackWrapper.getConfFileName() + ")");
        ConfLogger.stepDelimiter();
        new FrameworkLogger().info(BaseLogbackWrapper.getInCaseOfErrorsOrWarnings());
        for (ch.qos.logback.classic.Logger logger : BaseLogbackWrapper.getLoggerList()) {
            if (logger.getLevel() != null) {
                String name = logger.getName();
                try {
                    Class<?> c = this.getClass().getClassLoader().loadClass(name);
                    name = c.getSimpleName() + ".class";
                } catch (ClassNotFoundException ignore) {
                }
                ConfLogger.dotPlaceholder(name, logger.getLevel());
            }
        }
        isLogbackConfigurationInitialized = true;
    }

    @Bean()
    public boolean isLogbackConfigurationInitialized() {
        return isLogbackConfigurationInitialized;
    }

}
