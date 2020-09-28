package org.touchbit.buggy.spring.boot.starter;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.testng.TestNG;
import org.touchbit.buggy.core.utils.StringUtils;
import org.touchbit.buggy.core.utils.log.BuggyLog;

import static org.touchbit.buggy.core.utils.BuggyUtils.CONSOLE_DELIMITER;

@Configuration()
@DependsOn({"initAndGetBuggyConfigurations", "isBuggyLoggerInitialized"})
@ConditionalOnNotWebApplication
public class BuggyAutoConfiguration {

    private Logger consoleLogger;

    public BuggyAutoConfiguration(boolean isBuggyLoggerInitialized) {
        if (isBuggyLoggerInitialized) {
            consoleLogger = LoggerConfiguration.console();
            consoleLogger.info(" <<<<<<>>>>>>>>>");
        }
        StringUtils.println(CONSOLE_DELIMITER);
        StringUtils.println("Loading TestNG configuration");
    }

    @Bean
    public TestNG testNG() {
        StringUtils.println(CONSOLE_DELIMITER);
        return new TestNG();
    }

}
