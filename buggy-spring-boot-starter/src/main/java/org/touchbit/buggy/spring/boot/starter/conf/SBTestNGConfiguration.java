package org.touchbit.buggy.spring.boot.starter.conf;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.context.annotation.*;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.testng.listeners.BuggyListener;
import org.touchbit.buggy.spring.boot.starter.log.ConfigurationLogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.reflect.Modifier.*;

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
@DependsOn({"initAndGetBuggyConfigurations"})
public class SBTestNGConfiguration implements SBConfiguration {

    private final ApplicationProperties properties;

    private final List<BuggyListener> listenerList;
    private final List<BuggyListener> enabledBuggyListeners = new ArrayList<>();
    private final List<Class<?>> testClasses;

    public SBTestNGConfiguration(final boolean isBuggyLoggerInitialized, final ApplicationProperties properties) {
        this.properties = properties;
        if (!isBuggyLoggerInitialized) {
            exitRunWithErr("Logger must be initialized");
        }
        ConfigurationLogger.blockDelimeter();
        ConfigurationLogger.centerBold("Loading TestNG configuration");
        this.testClasses = getTestClassesWithSuiteAnnotation();
        this.listenerList = getTestNGListeners();
        for (BuggyListener buggyListener : this.listenerList) {
            if (buggyListener.isEnable()) {
                enabledBuggyListeners.add(buggyListener);
                ConfigurationLogger.dotPlaceholder(buggyListener.getClass().getSimpleName(), "Enable");
            } else {
                ConfigurationLogger.dotPlaceholder(buggyListener.getClass().getSimpleName(), "Disable");
            }
        }
    }

    @Bean("getTestClasses")
    public List<Class<?>> getTestClasses() {
        return testClasses;
    }

    @Bean("getBuggyListeners")
    public List<BuggyListener> getBuggyListeners() {
        return listenerList;
    }

    @Bean("getEnabledBuggyListeners")
    public List<BuggyListener> getEnabledBuggyListeners() {
        return enabledBuggyListeners;
    }

    public List<Class<?>> getTestClassesWithSuiteAnnotation() {
        List<Class<?>> resultList = new ArrayList<>();
        boolean useDefaultFilters = properties.isTestNGScannerSuiteUseDefaultFilters();
        String basePackage = properties.getTestNGScannerSuiteBasePackage();
        Set<BeanDefinition> definitions = scanBeanDefinitions(useDefaultFilters, basePackage, Suite.class);
        for (BeanDefinition bd : definitions) {
            try {
                Class<?> testClass = Thread.currentThread().getContextClassLoader().loadClass(bd.getBeanClassName());
                int mods = testClass.getModifiers();
                if (isPublic(mods) && !isAbstract(mods) && !isInterface(mods)) {
                    resultList.add(testClass);
                }
            } catch (ClassNotFoundException e) {
                exitRunWithErr("Failed to load @Suite-annotated class.", e);
            }
        }
        return resultList;
    }

    public List<BuggyListener> getTestNGListeners() {
        List<BuggyListener> result = new ArrayList<>();
        boolean useDefaultFilters = properties.isTestNGScannerListenerUseDefaultFilters();
        String basePackage = properties.getTestNGScannerListenerBasePackage();
        ConfigurationLogger.stepDelimeter();
        ConfigurationLogger.center("TestNG listeners");
        Set<BeanDefinition> definitions = scanBeanDefinitions(useDefaultFilters, basePackage, BuggyListener.class);
        for (BeanDefinition bd : definitions) {
            try {
                Class<?> c = BeanGenerator.class.getClassLoader().loadClass(bd.getBeanClassName());
                Constructor<?> con = c.getConstructor();
                BuggyListener instance = (BuggyListener) con.newInstance();
                result.add(instance);
            } catch (Exception e) {
                exitRunWithErr("Failed to load inheritor of class BuggyListener.", e);
            }
        }
        return result;
    }

    public Set<BeanDefinition> scanBeanDefinitions(boolean useDefaultFilters, String basePackage, Class<?> c) {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(useDefaultFilters);
        if (c.isAnnotation()) {
            Class<Annotation> annotation = (Class<Annotation>) c;
            scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));
        } else {
            scanner.addIncludeFilter(new AssignableTypeFilter(c));
        }
        return scanner.findCandidateComponents(basePackage);
    }

}
