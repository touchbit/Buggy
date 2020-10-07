package org.touchbit.buggy.core.config;

import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.goal.component.AllComponents;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.AllInterfaces;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.AllServices;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.model.ParallelMode;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.utils.JUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.touchbit.buggy.core.model.ParallelMode.METHODS;
import static org.touchbit.buggy.core.model.Type.ALL;

/**
 * Configuration class for customizing Buggy.
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("unused")
public final class BuggyConfig {

    private static Boolean help = false;
    private static Boolean force = false;
    private static Boolean printSuite = false;
    private static Boolean printCause = false;
    private static Boolean printLog = false;
    private static Boolean printLogOnlyFail = false;
    private static Boolean version = false;
    private static Integer threads = 50;
    private static String artifactsUrl;
    private static ParallelMode parallelMode = METHODS;
    private static List<Component> components = JUtils.getListWith(AllComponents::new);
    private static List<Service> services = JUtils.getListWith(AllServices::new);
    private static List<Interface> interfaces = JUtils.getListWith(AllInterfaces::new);
    private static List<Type> types = JUtils.getListWith(ALL);
    private static String programName = "Buggy";
    private static String taskTrackerIssueUrl = "";

    public static void setPrintLogFileOnlyFail(Boolean printLogIfFail) {
        BuggyConfig.printLogOnlyFail = printLogIfFail;
    }

    public static Boolean isPrintLogFileOnlyFail() {
        return printLogOnlyFail;
    }

    public static Boolean isHelp() {
        return BuggyConfig.help;
    }

    public static void setHelp(Boolean help) {
        BuggyConfig.help = help;
    }

    public static Boolean isForce() {
        return force;
    }

    public static void setForce(Boolean force) {
        BuggyConfig.force = force;
    }

    public static Boolean isPrintSuite() {
        return printSuite;
    }

    public static void setPrintSuite(Boolean printSuite) {
        BuggyConfig.printSuite = printSuite;
    }

    public static Boolean isPrintCause() {
        return printCause;
    }

    public static void setPrintCause(Boolean printCause) {
        BuggyConfig.printCause = printCause;
    }

    public static Boolean isPrintLog() {
        return printLog;
    }

    public static void setPrintLog(Boolean printLog) {
        BuggyConfig.printLog = printLog;
    }

    public static void setPrintLogOnlyFail(Boolean printLogOnlyFail) {
        BuggyConfig.printLogOnlyFail = printLogOnlyFail;
    }

    public static Boolean isVersion() {
        return version;
    }

    public static void setVersion(Boolean version) {
        BuggyConfig.version = version;
    }

    public static Integer getThreads() {
        return threads;
    }

    public static void setThreads(Integer threads) {
        BuggyConfig.threads = threads;
    }

    public static String getArtifactsUrl() {
        return artifactsUrl;
    }

    public static void setArtifactsUrl(String artifactsUrl) {
        BuggyConfig.artifactsUrl = artifactsUrl;
    }

    public static List<Type> getTypes() {
        return types;
    }

    public static void setTypes(List<Type> types) {
        if (types != null) {
            BuggyConfig.types = types;
        }
    }

    public static void setTypes(Type... types) {
        if (types != null) {
            setTypes(Arrays.asList(types));
        }
    }

    public static List<Service> getServices() {
        return services;
    }

    public static void setServices(List<Service> services) {
        if (services != null) {
            BuggyConfig.services = services;
        }
    }

    @SafeVarargs
    public static <S extends Service> void setServices(S... services) {
        if (services != null) {
            setServices(Arrays.asList(services));
        }
    }

    @SafeVarargs
    public static <S extends Service> void setServices(Class<S>... services) {
        BuggyConfig.services = new ArrayList<>();
        for (Class<? extends Service> service : services) {
            Service instance = JUtils.newInstance(service, BuggyConfigurationException::new);
            BuggyConfig.services.add(instance);
        }
    }

    public static List<Interface> getInterfaces() {
        return interfaces;
    }

    public static void setInterfaces(List<Interface> interfaces) {
        if (interfaces != null) {
            BuggyConfig.interfaces = interfaces;
        }
    }

    @SafeVarargs
    public static <I extends Interface> void setInterfaces(I... interfaces) {
        if (interfaces != null) {
            setInterfaces(Arrays.asList(interfaces));
        }
    }

    @SafeVarargs
    public static <I extends Interface> void setInterfaces(Class<I>... interfaces) {
        BuggyConfig.interfaces = new ArrayList<>();
        for (Class<? extends Interface> anInterface : interfaces) {
            Interface instance = JUtils.newInstance(anInterface, BuggyConfigurationException::new);
            BuggyConfig.interfaces.add(instance);
        }
    }

    public static List<Component> getComponents() {
        return components;
    }

    public static void setComponents(List<Component> components) {
        if (components != null) {
            BuggyConfig.components = components;
        }
    }

    @SafeVarargs
    public static <C extends Component> void setComponents(C... components) {
        if (components != null) {
            setComponents(Arrays.asList(components));
        }
    }

    @SafeVarargs
    public static <C extends Component> void setComponents(Class<C>... components) {
        BuggyConfig.components = new ArrayList<>();
        for (Class<? extends Component> component : components) {
            Component instance = JUtils.newInstance(component, BuggyConfigurationException::new);
            BuggyConfig.components.add(instance);
        }
    }

    public static ParallelMode getParallelMode() {
        return parallelMode;
    }

    public static void setParallelMode(ParallelMode parallelMode) {
        BuggyConfig.parallelMode = parallelMode;
    }

    public static String getProgramName() {
        return BuggyConfig.programName;
    }

    public static void setProgramName(String programName) {
        BuggyConfig.programName = programName;
    }

    public static String getTaskTrackerIssueUrl() {
        return taskTrackerIssueUrl;
    }

    public static void setTaskTrackerIssueUrl(String taskTrackerIssueUrl) {
        BuggyConfig.taskTrackerIssueUrl = taskTrackerIssueUrl;
    }
}
