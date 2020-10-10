package org.touchbit.buggy.spring.boot.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.touchbit.buggy.core.config.BuggyConfigurationYML;
import org.touchbit.buggy.core.goal.Goal;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.logback.ConfLogger;
import org.touchbit.buggy.core.logback.FrameworkLogger;
import org.touchbit.buggy.core.logback.appender.SiftingFileAppender;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.testng.BuggyListener;
import org.touchbit.buggy.core.utils.JUtils;
import org.touchbit.buggy.spring.boot.starter.jcommander.BuggyConfiguration;

import java.io.File;
import java.util.*;

import static org.springframework.boot.context.logging.LoggingApplicationListener.CONFIG_PROPERTY;
import static org.springframework.boot.logging.LogFile.FILE_PATH_PROPERTY;
import static org.touchbit.buggy.spring.boot.starter.conf.Qualifiers.*;

@SpringBootApplication
public class BuggyRunner implements CommandLineRunner {

    static {
        if (JUtils.isJetBrainsIdeConsoleRun()) {
            System.setProperty(FILE_PATH_PROPERTY, JUtils.getJetBrainsIdeConsoleRunTargetPath() + "/logs");
        }
        // If application.yml does not contain a variable 'logging.config'
        System.setProperty(CONFIG_PROPERTY, "classpath:buggy-logback.xml");
    }

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
            ConfLogger.blockDelimiter();
            String frameworkLogPath = "";
            if (status == 0) {
                ConfLogger.info(message);
            } else {
                File file = FrameworkLogger.getLogFile();
                if (file != null) {
                    frameworkLogPath = "\nFor more information see " + file.getName();
                }
                if (e != null) {
                    message += "\n" + e.getClass().getSimpleName() + ": " + e.getMessage();
                }
                ConfLogger.error(message + frameworkLogPath, e);
            }
        }
        ConfLogger.blockDelimiter();
        ConfLogger.dotPlaceholder("Exit code", status);
        ConfLogger.blockDelimiter();
        System.exit(status);
    }

    @Override
    public void run(String... args) {
        ConfLogger.blockDelimiter();
        Map<Suite, Set<Class<?>>> testClassesBySuitesMap = getTestClassesBySuitesMap(filteredTestClasses);
        List<XmlSuite> xmlSuites = getXmlSuites(testClassesBySuitesMap);
        TestNG testNG = getTestNG();
        try {
            testNG.setParallel(BuggyConfigurationYML.getParallelMode().getTestNGMode());
            testNG.setSuiteThreadPoolSize(xmlSuites.isEmpty() ? 1 : xmlSuites.size());
            testNG.setUseDefaultListeners(false);
            for (BuggyListener enabledBuggyListener : enabledBuggyListeners) {
                testNG.addListener(enabledBuggyListener);
            }
            testNG.setXmlSuites(xmlSuites);
            testNG.run();
            SiftingFileAppender.decomposeTestLogs();
            if (BuggyConfiguration.getExitStatus() != null) {
                exit(BuggyConfiguration.getExitStatus());
            }
            exit(testNG.getStatus());
        } catch (Exception e) {
            exit(1, "TestNG safely died.", e);
        } finally {
            SiftingFileAppender.decomposeTestLogs();
        }
    }

    public Map<Suite, Set<Class<?>>> getTestClassesBySuitesMap(Set<Class<?>> testClasses) {
        Map<Suite, Set<Class<?>>> result = new HashMap<>();
        for (Class<?> testClass : testClasses) {
            if (testClass.isAnnotationPresent(Suite.class)) {
                Suite suite = testClass.getAnnotation(Suite.class);
                if (result.containsKey(suite)) {
                    result.get(suite).add(testClass);
                } else {
                    Set<Class<?>> set = new LinkedHashSet<>();
                    set.add(testClass);
                    result.put(suite, set);
                }
            }
        }
        return result;
    }

    public List<XmlSuite> getXmlSuites(Map<Suite, Set<Class<?>>> buggySuitesTests) {
        List<XmlSuite> suites = new ArrayList<>();
        for (Map.Entry<Suite, Set<Class<?>>> entry : buggySuitesTests.entrySet()) {
            XmlSuite xmlSuite = new XmlSuite();
            xmlSuite.setParallel(BuggyConfigurationYML.getParallelMode().getTestNGMode());
            xmlSuite.setThreadCount(BuggyConfigurationYML.getThreads());
            Suite buggySuite = entry.getKey();
            try {
                Component cGoal = JUtils.getGoal(Suite::component, buggySuite);
                Service sGoal = JUtils.getGoal(Suite::service, buggySuite);
                Interface iGoal = JUtils.getGoal(Suite::interfaze, buggySuite);
                xmlSuite.setName(cGoal.getName() + " " + sGoal.getName() + " " + iGoal.getName() + " suite");
            } catch (Exception e) {
                exit(1, "Failed to generate TestNG XmlSuite list", e);
            }
            XmlTest testPackage = new XmlTest(xmlSuite);
            testPackage.setName(UUID.randomUUID().toString());
            for (Class<?> testClass : entry.getValue()) {
                XmlClass xmlClass = new XmlClass(testClass);
                testPackage.getXmlClasses().add(xmlClass);
            }
            suites.add(xmlSuite);
        }
        return suites;
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
