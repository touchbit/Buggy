package org.touchbit.buggy.spring.boot.starter.conf;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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

import javax.annotation.PostConstruct;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TestNG configuration
 * Filtering test classes for suites by:
 * 1. {@link Component}
 * 2. {@link Service}
 * 3. {@link Interface}
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
@Configuration()
@ConditionalOnNotWebApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class TestNGConfiguration implements IConfiguration {

    private final ApplicationProperties properties;
    private final Set<BuggyListener> buggyListeners;
    private final Set<BuggyListener> enabledBuggyListeners;
    private final Set<Class<?>> testClasses;
    private final Set<Goal> goals;
    private final Set<Component> availableComponents;
    private final Set<Service> availableServices;
    private final Set<Interface> availableInterfaces;

    public TestNGConfiguration(final boolean isLogbackConfigurationInitialized, final ApplicationProperties props) {
        beforeConfiguration(isLogbackConfigurationInitialized);
        this.properties = props;
        this.buggyListeners = getTestNGListeners();
        this.enabledBuggyListeners = this.buggyListeners.stream()
                .filter(BuggyListener::isEnable)
                .collect(Collectors.toSet());
        Set<Class<?>> testClassesWithSuiteAnnotation = getTestClassesWithSuiteAnnotation();
        this.testClasses = filterTestClassesByBuggyConfig(testClassesWithSuiteAnnotation);
        this.goals = getGoals();
        Set<Component> components =
                filterByGoalType(this.goals, BuggyConfig.getComponents(), Component.class, AllComponents.class);
        this.availableComponents = filterComponentsByTestClasses(components, testClasses);
        Set<Service> services =
                filterByGoalType(this.goals, BuggyConfig.getComponents(), Service.class, AllServices.class);
        this.availableServices = filterServiceByTestClasses(services, testClasses);
        Set<Interface> interfaces =
                filterByGoalType(this.goals, BuggyConfig.getComponents(), Interface.class, AllInterfaces.class);
        this.availableInterfaces = filterInterfaceByTestClasses(interfaces, testClasses);
    }

    public void beforeConfiguration(final boolean isLogbackConfigurationInitialized) {
        if (!isLogbackConfigurationInitialized) {
            BuggyRunner.exit(1, "LogbackConfiguration must be initialized");
        }
        ConfigurationLogger.blockDelimiter();
        ConfigurationLogger.centerBold("Preparing TestNG configuration");
        ConfigurationLogger.stepDelimiter();
    }

    @PostConstruct
    public void printTestNgConfigurationInfo() {
        ConfigurationLogger.center("TestNG listeners");
        for (BuggyListener buggyListener : this.buggyListeners) {
            if (buggyListener.isEnable()) {
                ConfigurationLogger.dotPlaceholder(buggyListener.getClass().getSimpleName(), "Enable");
            } else {
                ConfigurationLogger.dotPlaceholder(buggyListener.getClass().getSimpleName(), "Disable");
            }
        }
        ConfigurationLogger.stepDelimiter();
        ConfigurationLogger.center("Available suites goals");
        ConfigurationLogger.stepDelimiter();
        ConfigurationLogger.center("Components");
        this.availableComponents.forEach(g -> ConfigurationLogger.dotPlaceholder(JUtils.getSimpleNameC(g), g.getName()));
        ConfigurationLogger.stepDelimiter();
        ConfigurationLogger.center("Services");
        this.availableServices.forEach(g -> ConfigurationLogger.dotPlaceholder(JUtils.getSimpleNameC(g), g.getName()));
        ConfigurationLogger.stepDelimiter();
        ConfigurationLogger.center("Interfaces");
        this.availableInterfaces.forEach(g -> ConfigurationLogger.dotPlaceholder(JUtils.getSimpleNameC(g), g.getName()));
    }

    public Set<Class<?>> filterTestClassesByBuggyConfig(Set<Class<?>> testClassesWithSuiteAnnotation) {
        List<Class<? extends Component>> components = BuggyConfig.getComponents().stream()
                .map(Component::getClass)
                .collect(Collectors.toList());
        List<Class<? extends Service>> services = BuggyConfig.getServices().stream()
                .map(Service::getClass)
                .collect(Collectors.toList());
        List<Class<? extends Interface>> interfaces = BuggyConfig.getInterfaces().stream()
                .map(Interface::getClass)
                .collect(Collectors.toList());
        return testClassesWithSuiteAnnotation.stream()
                .filter(c ->
                        components.contains(AllComponents.class) ||
                                components.contains(c.getAnnotation(Suite.class).component()))
                .filter(c ->
                        services.contains(AllServices.class) ||
                                services.contains(c.getAnnotation(Suite.class).service()))
                .filter(c ->
                        interfaces.contains(AllInterfaces.class) ||
                                interfaces.contains(c.getAnnotation(Suite.class).interfaze()))
                .collect(Collectors.toSet());
    }

    public Set<Component> filterComponentsByTestClasses(final Set<Component> components,
                                                        final Set<Class<?>> testClasses) {
        List<Class<? extends Component>> testComponents = testClasses.stream()
                .filter(c -> c.isAnnotationPresent(Suite.class))
                .map(c -> c.getAnnotation(Suite.class).component())
                .collect(Collectors.toList());
        return components.stream()
                .filter(a -> testComponents.contains(a.getClass()))
                .collect(Collectors.toSet());
    }

    public Set<Service> filterServiceByTestClasses(final Set<Service> services,
                                                   final Set<Class<?>> testClasses) {
        List<Class<? extends Service>> tetServices = testClasses.stream()
                .filter(c -> c.isAnnotationPresent(Suite.class))
                .map(c -> c.getAnnotation(Suite.class).service())
                .collect(Collectors.toList());
        return services.stream()
                .filter(a -> tetServices.contains(a.getClass()))
                .collect(Collectors.toSet());
    }

    public Set<Interface> filterInterfaceByTestClasses(final Set<Interface> interfaces,
                                                       final Set<Class<?>> testClasses) {
        List<Class<? extends Interface>> testInterfaces = testClasses.stream()
                .filter(c -> c.isAnnotationPresent(Suite.class))
                .map(c -> c.getAnnotation(Suite.class).interfaze())
                .collect(Collectors.toList());
        return interfaces.stream()
                .filter(a -> testInterfaces.contains(a.getClass()))
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    public <T> Set<T> filterByGoalType(final Set<? extends Goal> allGoals,
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
    public Set<Component> getAvailableComponents() {
        return availableComponents;
    }

    @Bean("getEnabledServices")
    public Set<Service> getAvailableServices() {
        return availableServices;
    }

    @Bean("getEnabledInterfaces")
    public Set<Interface> getAvailableInterfaces() {
        return availableInterfaces;
    }

    @Bean("getAllGoals")
    public Set<Goal> getAllGoals() {
        return goals;
    }

    @Bean("getTestClasses")
    public Set<Class<?>> getTestClasses() {
        return testClasses;
    }

    @Bean("getEnabledBuggyListeners")
    public Set<BuggyListener> getEnabledBuggyListeners() {
        return enabledBuggyListeners;
    }

    public Set<Goal> getGoals() {
        final Set<BeanDefinition> defs = new LinkedHashSet<>();
        final List<String> basePackages = properties
                .getGoalsScannerBasePackages().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        for (String basePackage : basePackages) {
            defs.addAll(scanBeanDefinitions(false, basePackage, Goal.class));
        }
        return getBeanDefinitionInstances(defs, Goal.class);
    }

    public Set<Class<?>> getTestClassesWithSuiteAnnotation() {
        final Set<BeanDefinition> defs = new LinkedHashSet<>();
        final List<String> basePackages = properties
                .getSuitesScannerBasePackages().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        for (String basePackage : basePackages) {
            defs.addAll(scanBeanDefinitions(false, basePackage, Suite.class));
        }
        return getBeanDefinitionAnnotatedClasses(defs, Suite.class);
    }

    public Set<BuggyListener> getTestNGListeners() {
        final Set<BeanDefinition> defs = new LinkedHashSet<>();
        final List<String> basePackages = properties
                .getListenersScannerBasePackages().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        for (String basePackage : basePackages) {
            defs.addAll(scanBeanDefinitions(false, basePackage, BuggyListener.class));
        }
        return getBeanDefinitionInstances(defs, BuggyListener.class);
    }

}
