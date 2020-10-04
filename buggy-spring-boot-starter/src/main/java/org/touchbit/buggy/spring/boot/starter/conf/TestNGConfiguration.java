package org.touchbit.buggy.spring.boot.starter.conf;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.*;
import org.touchbit.buggy.core.config.BuggyConfig;
import org.touchbit.buggy.core.goal.Goal;
import org.touchbit.buggy.core.goal.component.AllComponents;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.AllInterfaces;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.AllServices;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.testng.listeners.BuggyListener;
import org.touchbit.buggy.core.utils.JUtils;
import org.touchbit.buggy.spring.boot.starter.BuggyRunner;
import org.touchbit.buggy.core.utils.log.ConfigurationLogger;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final Set<Goal> goals;
    private final Set<Component> enabledComponents;
    private final Set<Service> enabledServices;
    private final Set<Interface> enabledInterfaces;

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
        this.goals = getGoals();
        ConfigurationLogger.stepDelimiter();
        ConfigurationLogger.center("Enabled suites goals");
        ConfigurationLogger.stepDelimiter();
        ConfigurationLogger.center("Components");
        this.enabledComponents = filter(this.goals, BuggyConfig.getComponents(), Component.class, AllComponents.class);
        this.enabledComponents.forEach(g -> ConfigurationLogger.dotPlaceholder(JUtils.getSimpleNameC(g), g.getName()));
        ConfigurationLogger.stepDelimiter();
        ConfigurationLogger.center("Services");
        this.enabledServices = filter(this.goals, BuggyConfig.getComponents(), Service.class, AllServices.class);
        this.enabledServices.forEach(g -> ConfigurationLogger.dotPlaceholder(JUtils.getSimpleNameC(g), g.getName()));
        ConfigurationLogger.stepDelimiter();
        ConfigurationLogger.center("Interfaces");
        this.enabledInterfaces = filter(this.goals, BuggyConfig.getComponents(), Interface.class, AllInterfaces.class);
        this.enabledInterfaces.forEach(g -> ConfigurationLogger.dotPlaceholder(JUtils.getSimpleNameC(g), g.getName()));
    }

    @SuppressWarnings("unchecked")
    public <T> Set<T> filter(final Set<? extends Goal> allGoals,
                             final List<? extends Goal> expectedGoals,
                             final Class<T> goalFilter,
                             final Class<? extends Goal> allFilter) {
        if (allGoals.stream().anyMatch(c -> c.getClass().equals(allFilter))) {
            return (Set<T>) allGoals.stream()
                    .filter(g -> goalFilter.isAssignableFrom(g.getClass()))
                    .filter(Goal::isActive)
                    .collect(Collectors.toSet());
        } else {
            return (Set<T>) allGoals.stream()
                    .filter(g -> goalFilter.isAssignableFrom(g.getClass()))
                    .filter(Goal::isActive)
                    .filter(g -> expectedGoals
                            .stream()
                            .anyMatch(c -> g.getClass().equals(c.getClass())))
                    .collect(Collectors.toSet());
        }
    }

    @Bean("getEnabledComponents")
    public Set<Component> getEnabledComponents() {
        return enabledComponents;
    }

    @Bean("getEnabledServices")
    public Set<Service> getEnabledServices() {
        return enabledServices;
    }

    @Bean("getEnabledInterfaces")
    public Set<Interface> getEnabledInterfaces() {
        return enabledInterfaces;
    }

    @Bean("getAllGoals")
    public Set<Goal> getAllGoals() {
        return goals;
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

    public Set<Goal> getGoals() {
        final boolean useDefaultFilters = properties.isTestNGScannerListenerUseDefaultFilters();
        final String basePackage = properties.getTestNGScannerListenerBasePackage();
        final Set<BeanDefinition> defs = scanBeanDefinitions(useDefaultFilters, basePackage, Goal.class);
        return getBeanDefinitionInstances(defs, Goal.class);
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
