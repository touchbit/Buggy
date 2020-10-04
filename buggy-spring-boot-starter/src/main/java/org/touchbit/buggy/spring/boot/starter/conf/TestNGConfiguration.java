package org.touchbit.buggy.spring.boot.starter.conf;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.*;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.testng.listeners.BuggyListener;
import org.touchbit.buggy.spring.boot.starter.BuggyRunner;
import org.touchbit.buggy.core.utils.log.ConfigurationLogger;

import java.util.LinkedHashSet;
import java.util.Set;

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
public class TestNGConfiguration implements IConfiguration {

    private final ApplicationProperties properties;
    private final Set<BuggyListener> buggyListeners;
    private final Set<BuggyListener> enabledBuggyListeners = new LinkedHashSet<>();
    private final Set<Class<?>> testClasses;

    public TestNGConfiguration(final boolean isBuggyLoggerInitialized, final ApplicationProperties properties) {
        this.properties = properties;
        if (!isBuggyLoggerInitialized) {
            BuggyRunner.exit(1, "Logger must be initialized");
        }
        ConfigurationLogger.blockDelimiter();
        ConfigurationLogger.centerBold("Loading TestNG configuration");
        ConfigurationLogger.stepDelimiter();
        ConfigurationLogger.center("TestNG listeners");
        this.buggyListeners = getTestNGListeners();
        for (BuggyListener buggyListener : this.buggyListeners) {
            if (buggyListener.isEnable()) {
                enabledBuggyListeners.add(buggyListener);
                ConfigurationLogger.dotPlaceholder(buggyListener.getClass().getSimpleName(), "Enable");
            } else {
                ConfigurationLogger.dotPlaceholder(buggyListener.getClass().getSimpleName(), "Disable");
            }
        }
        this.testClasses = getTestClassesWithSuiteAnnotation();
    }

    @Bean("getTestClasses")
    public Set<Class<?>> getTestClasses() {
        return testClasses;
    }

    @Bean("getBuggyListeners")
    public Set<BuggyListener> getBuggyListeners() {
        return buggyListeners;
    }

    @Bean("getEnabledBuggyListeners")
    public Set<BuggyListener> getEnabledBuggyListeners() {
        return enabledBuggyListeners;
    }

    public Set<Class<?>> getTestClassesWithSuiteAnnotation() {
        final boolean useDefaultFilters = properties.isTestNGScannerSuiteUseDefaultFilters();
        final String basePackage = properties.getTestNGScannerSuiteBasePackage();
        final Set<BeanDefinition> definitions = scanBeanDefinitions(useDefaultFilters, basePackage, Suite.class);
        return getBeanDefinitionAnnotatedClasses(definitions, Suite.class);
    }

    public Set<BuggyListener> getTestNGListeners() {
        final boolean useDefaultFilters = properties.isTestNGScannerListenerUseDefaultFilters();
        final String basePackage = properties.getTestNGScannerListenerBasePackage();
        final Set<BeanDefinition> defs = scanBeanDefinitions(useDefaultFilters, basePackage, BuggyListener.class);
        return getBeanDefinitionInstances(defs, BuggyListener.class);
    }

}
