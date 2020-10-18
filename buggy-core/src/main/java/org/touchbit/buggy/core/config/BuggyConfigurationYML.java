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

import java.util.*;

import static org.touchbit.buggy.core.model.ParallelMode.METHODS;

/**
 * Configuration class for customizing Buggy.
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("unused")
public final class BuggyConfigurationYML implements ConfigurationYML {

    private static String programName = "Buggy";
    private static Boolean help = false;
    private static Boolean forceRun = false;
    private static Integer threads = 50;
    private static ParallelMode parallelMode = METHODS;
    private static String artifactsUrl;
    private static List<Component> components = new ArrayList<Component>() {{ add(new AllComponents()); }};
    private static List<Service> services = new ArrayList<Service>() {{ add(new AllServices()); }};
    private static List<Interface> interfaces = new ArrayList<Interface>() {{ add(new AllInterfaces()); }};
    private static Type[] types = new Type[] { Type.REGRESSION };
    private static String issuesUrl = "";
    private static Boolean testCaseTitle = true;
    private static Boolean testSuiteInfo = false;
    private static Boolean testLogFilePath = true;
    private static Boolean testIssuesInfo = false;
    private static Boolean testBugsInfo = true;
    private static Boolean testErrorInfo = true;
    private static OutputRule outputRule = OutputRule.UNSUCCESSFUL;

    public static Boolean isHelp() {
        return BuggyConfigurationYML.help;
    }

    public static void setHelp(Boolean help) {
        BuggyConfigurationYML.help = help;
    }

    public static Boolean isForceRun() {
        return forceRun;
    }

    public static void setForceRun(Boolean forceRun) {
        BuggyConfigurationYML.forceRun = forceRun;
    }

    public static Integer getThreads() {
        return threads;
    }

    public static void setThreads(Integer threads) {
        BuggyConfigurationYML.threads = threads;
    }

    public static String getArtifactsUrl() {
        return artifactsUrl;
    }

    public static void setArtifactsUrl(String artifactsUrl) {
        BuggyConfigurationYML.artifactsUrl = artifactsUrl;
    }

    public static Type[] getTypes() {
        return types;
    }

    public static void setTypes(Type... types) {
        if (types != null) {
            BuggyConfigurationYML.types = types;
        }
    }

    public static List<Service> getServices() {
        return services;
    }

    public static void setServices(List<Service> services) {
        if (services != null) {
            BuggyConfigurationYML.services = services;
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
        BuggyConfigurationYML.services = new ArrayList<>();
        for (Class<? extends Service> service : services) {
            Service instance = JUtils.newInstance(service, BuggyConfigurationException::new);
            BuggyConfigurationYML.services.add(instance);
        }
    }

    public static List<Interface> getInterfaces() {
        return interfaces;
    }

    public static void setInterfaces(List<Interface> interfaces) {
        if (interfaces != null) {
            BuggyConfigurationYML.interfaces = interfaces;
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
        BuggyConfigurationYML.interfaces = new ArrayList<>();
        for (Class<? extends Interface> anInterface : interfaces) {
            Interface instance = JUtils.newInstance(anInterface, BuggyConfigurationException::new);
            BuggyConfigurationYML.interfaces.add(instance);
        }
    }

    public static List<Component> getComponents() {
        return components;
    }

    public static void setComponents(List<Component> components) {
        if (components != null) {
            BuggyConfigurationYML.components = components;
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
        BuggyConfigurationYML.components = new ArrayList<>();
        for (Class<? extends Component> component : components) {
            Component instance = JUtils.newInstance(component, BuggyConfigurationException::new);
            BuggyConfigurationYML.components.add(instance);
        }
    }

    public static ParallelMode getParallelMode() {
        return parallelMode;
    }

    public static void setParallelMode(ParallelMode parallelMode) {
        BuggyConfigurationYML.parallelMode = parallelMode;
    }

    public static String getProgramName() {
        return BuggyConfigurationYML.programName;
    }

    public static void setProgramName(String programName) {
        BuggyConfigurationYML.programName = programName;
    }

    public static String getIssuesUrl() {
        return issuesUrl;
    }

    public static void setIssuesUrl(String issuesUrl) {
        BuggyConfigurationYML.issuesUrl = issuesUrl;
    }

    public static Boolean getHelp() {
        return help;
    }

    public static Boolean getForceRun() {
        return forceRun;
    }

    public static Boolean getTestCaseTitle() {
        return testCaseTitle;
    }

    public static void setTestCaseTitle(Boolean testCaseTitle) {
        BuggyConfigurationYML.testCaseTitle = testCaseTitle;
    }

    public static Boolean getTestSuiteInfo() {
        return testSuiteInfo;
    }

    public static void setTestSuiteInfo(Boolean testSuiteInfo) {
        BuggyConfigurationYML.testSuiteInfo = testSuiteInfo;
    }

    public static Boolean getTestLogFilePath() {
        return testLogFilePath;
    }

    public static void setTestLogFilePath(Boolean testLogFilePath) {
        BuggyConfigurationYML.testLogFilePath = testLogFilePath;
    }

    public static Boolean getTestIssuesInfo() {
        return testIssuesInfo;
    }

    public static void setTestIssuesInfo(Boolean testIssuesInfo) {
        BuggyConfigurationYML.testIssuesInfo = testIssuesInfo;
    }

    public static Boolean getTestBugsInfo() {
        return testBugsInfo;
    }

    public static void setTestBugsInfo(Boolean testBugsInfo) {
        BuggyConfigurationYML.testBugsInfo = testBugsInfo;
    }

    public static Boolean getTestErrorInfo() {
        return testErrorInfo;
    }

    public static void setTestErrorInfo(Boolean testErrorInfo) {
        BuggyConfigurationYML.testErrorInfo = testErrorInfo;
    }

    public static OutputRule getOutputRule() {
        return outputRule;
    }

    public static void setOutputRule(OutputRule outputRule) {
        BuggyConfigurationYML.outputRule = outputRule;
    }

}
