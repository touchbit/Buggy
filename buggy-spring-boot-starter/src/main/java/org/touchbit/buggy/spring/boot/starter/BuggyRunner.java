package org.touchbit.buggy.spring.boot.starter;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.touchbit.buggy.core.goal.Goal;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.testng.listeners.BuggyListener;
import org.touchbit.buggy.core.utils.log.ConfigurationLogger;
import org.touchbit.buggy.core.utils.log.BuggyLoggers;

import java.util.LinkedHashSet;
import java.util.Set;

@SpringBootApplication
public class BuggyRunner implements ApplicationRunner {

    private Set<BuggyListener> enabledBuggyListeners;
    private Set<Class<?>> testClasses;
    private Set<Component> enabledComponents;
    private Set<Service> enabledServices;
    private Set<Interface> enabledInterfaces;

    @Override
    public void run(ApplicationArguments args) {
        ConfigurationLogger.blockDelimiter();
        ConfigurationLogger.info(" >>>>> " + enabledBuggyListeners);
        ConfigurationLogger.info(" >>>>> " + testClasses);
        ConfigurationLogger.info(" >>>>> " + enabledComponents);
        ConfigurationLogger.info(" >>>>> " + enabledServices);
        ConfigurationLogger.info(" >>>>> " + enabledInterfaces);
        exit(0);
    }

    @Autowired
    public void setEnabledBuggyListeners(Set<BuggyListener> enabledBuggyListeners) {
        this.enabledBuggyListeners = enabledBuggyListeners;
    }

    @Autowired
    public void setTestClasses(Set<Class<?>> testClasses) {
        this.testClasses = testClasses;
    }

    @Autowired
    public void setEnabledComponents(Set<Component> enabledComponents) {
        this.enabledComponents = enabledComponents;
    }

    @Autowired
    public void setEnabledServices(Set<Service> enabledServices) {
        this.enabledServices = enabledServices;
    }

    @Autowired
    public void setEnabledInterfaces(Set<Interface> enabledInterfaces) {
        this.enabledInterfaces = enabledInterfaces;
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

}
