package org.touchbit.buggy.spring.boot.starter;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.testng.TestNG;
import org.touchbit.buggy.core.config.BuggyConfig;
import org.touchbit.buggy.core.goal.Goal;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.testng.listeners.BuggyListener;
import org.touchbit.buggy.core.utils.log.BuggyLoggers;
import org.touchbit.buggy.core.utils.log.ConfigurationLogger;

import java.util.Set;

import static org.touchbit.buggy.spring.boot.starter.conf.Qualifiers.*;

@SpringBootApplication
public class BuggyRunner implements ApplicationRunner {

    private Set<BuggyListener> allBuggyListeners;
    private Set<BuggyListener> enabledBuggyListeners;
    private Set<Class<?>> allTestClasses;
    private Set<Class<?>> filteredTestClasses;
    private Set<Goal> allGoals;
    private Set<Component> allComponents;
    private Set<Component> availableComponents;
    private Set<Service> allServices;
    private Set<Service> availableServices;
    private Set<Interface> allInterfaces;
    private Set<Interface> availableInterfaces;
    private TestNG testNG = new TestNG();

    @Override
    public void run(ApplicationArguments args) {
        TestNG testNG = getTestNG();
        try {
            testNG.run();
            if (BuggyConfig.getStatus() != null) {
                exit(BuggyConfig.getStatus());
            }
            exit(testNG.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            exit(1, "TestNG safely died.");
        }
    }

    public static void exit(String message, Exception e) {
        if (e != null) {
            exit(1, message, e);
        } else {
            exit(0, message);
        }
    }

    public static void exit(int status) {
        exit(status, null, null);
    }

    public static void exit(int status, String message) {
        exit(status, message, null);
    }

    public static void exit(int status, String message, Exception e) {
        if (message != null) {
            ConfigurationLogger.blockDelimiter();
            String frameworkLogPath = "";
            if (status == 0) {
                ConfigurationLogger.info(message);
            } else {
                if (BuggyLoggers.F_LOG == null) {
                    MDC.put("print.console.stacktrace", "true");
                } else {
                    String frameworkLogFilePath = BuggyLoggers.getFrameworkLogFilePath();
                    if (frameworkLogFilePath != null && !frameworkLogFilePath.isEmpty()) {
                        frameworkLogPath = "\nFor more information see " + frameworkLogFilePath;
                    }
                }
                String eMsg = "\n" + e.getClass().getSimpleName() + ": " + e.getMessage();
                ConfigurationLogger.error(message + eMsg + frameworkLogPath, e);
            }
        }
        ConfigurationLogger.blockDelimiter();
        ConfigurationLogger.dotPlaceholder("Exit code", status);
        ConfigurationLogger.blockDelimiter();
        System.exit(status);
    }

    public TestNG getTestNG() {
        return testNG;
    }

    public void setTestNG(TestNG testNG) {
        this.testNG = testNG;
    }

    public Set<BuggyListener> getAllBuggyListeners() {
        return allBuggyListeners;
    }

    @Autowired()
    @Qualifier(ALL_BUGGY_LISTENERS)
    public void setAllBuggyListeners(Set<BuggyListener> allBuggyListeners) {
        this.allBuggyListeners = allBuggyListeners;
    }

    public Set<BuggyListener> getEnabledBuggyListeners() {
        return enabledBuggyListeners;
    }

    @Autowired()
    @Qualifier(ENABLED_BUGGY_LISTENERS)
    public void setEnabledBuggyListeners(Set<BuggyListener> enabledBuggyListeners) {
        this.enabledBuggyListeners = enabledBuggyListeners;
    }

    public Set<Class<?>> getAllTestClasses() {
        return allTestClasses;
    }

    @Autowired()
    @Qualifier(ALL_BUGGY_TEST_CLASSES)
    public void setAllTestClasses(Set<Class<?>> allTestClasses) {
        this.allTestClasses = allTestClasses;
    }

    public Set<Class<?>> getFilteredTestClasses() {
        return filteredTestClasses;
    }

    @Autowired()
    @Qualifier(FILTERED_BUGGY_TEST_CLASSES)
    public void setFilteredTestClasses(Set<Class<?>> filteredTestClasses) {
        this.filteredTestClasses = filteredTestClasses;
    }

    public Set<Goal> getAllGoals() {
        return allGoals;
    }

    @Autowired()
    @Qualifier(ALL_BUGGY_GOALS)
    public void setAllGoals(Set<Goal> allGoals) {
        this.allGoals = allGoals;
    }

    public Set<Component> getAllComponents() {
        return allComponents;
    }

    @Autowired()
    @Qualifier(ALL_BUGGY_COMPONENTS)
    public void setAllComponents(Set<Component> allComponents) {
        this.allComponents = allComponents;
    }

    public Set<Component> getAvailableComponents() {
        return availableComponents;
    }

    @Autowired()
    @Qualifier(AVAILABLE_BUGGY_COMPONENTS)
    public void setAvailableComponents(Set<Component> availableComponents) {
        this.availableComponents = availableComponents;
    }

    public Set<Service> getAllServices() {
        return allServices;
    }

    @Autowired()
    @Qualifier(ALL_BUGGY_SERVICES)
    public void setAllServices(Set<Service> allServices) {
        this.allServices = allServices;
    }

    public Set<Service> getAvailableServices() {
        return availableServices;
    }

    @Autowired()
    @Qualifier(AVAILABLE_BUGGY_SERVICES)
    public void setAvailableServices(Set<Service> availableServices) {
        this.availableServices = availableServices;
    }

    public Set<Interface> getAllInterfaces() {
        return allInterfaces;
    }

    @Autowired()
    @Qualifier(ALL_BUGGY_INTERFACES)
    public void setAllInterfaces(Set<Interface> allInterfaces) {
        this.allInterfaces = allInterfaces;
    }

    public Set<Interface> getAvailableInterfaces() {
        return availableInterfaces;
    }

    @Autowired()
    @Qualifier(AVAILABLE_BUGGY_INTERFACES)
    public void setAvailableInterfaces(Set<Interface> availableInterfaces) {
        this.availableInterfaces = availableInterfaces;
    }

}
