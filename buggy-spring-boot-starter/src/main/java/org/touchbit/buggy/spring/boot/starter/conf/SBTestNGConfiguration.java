package org.touchbit.buggy.spring.boot.starter.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.testng.TestNG;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.spring.boot.starter.log.ConfigurationLogger;

import java.util.Map;


/**
 * TestNG configuration
 * Filter test classes for suites by:
 * 1. {@link Component}*
 * 2. {@link Service}*
 * 3. {@link Interface}*
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
@Configuration()
@ConditionalOnNotWebApplication
@DependsOn({"initAndGetBuggyConfigurations", "isBuggyLoggerInitialized"})
public class SBTestNGConfiguration implements SBConfiguration {

    @Autowired
    Map<Class<? extends JCConfiguration>, JCConfiguration> getBuggyConfigurations;

    public SBTestNGConfiguration(boolean isBuggyLoggerInitialized) {
        if (!isBuggyLoggerInitialized) {
            exitRunWithErr("Logger must be initialized");
        }
        ConfigurationLogger.blockDelimeter();
        ConfigurationLogger.centerBold("Loading TestNG configuration");
    }

    @Bean
    public TestNG testNG() {
        ConfigurationLogger.stepDelimeter();
        return new TestNG();
    }

}
