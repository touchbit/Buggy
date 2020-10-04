package org.touchbit.buggy.core.config;

import com.beust.jcommander.Parameter;
import org.touchbit.buggy.core.config.jcommander.InterfaceConverter;
import org.touchbit.buggy.core.config.jcommander.ParameterValidator;
import org.touchbit.buggy.core.config.jcommander.ServiceConverter;
import org.touchbit.buggy.core.goal.component.AllComponents;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.AllInterfaces;
import org.touchbit.buggy.core.goal.service.AllServices;
import org.touchbit.buggy.core.model.BParallelMode;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;

import java.util.*;

import static org.touchbit.buggy.core.config.BParameters.*;
import static org.touchbit.buggy.core.model.BParallelMode.METHODS;

/**
 * Configuration class for customizing Buggy.
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("unused")
public class BuggyConfig implements JCConfiguration {

    @Parameter(names = {QUESTION_MARK, HELP}, help = true, description = "Print usage.")
    private static Boolean help = false;

    @Parameter(names = {F, FORCE}, description = "Running all tests, including those that fall.")
    private static Boolean force = false;

    @Parameter(names = {PRINT_SUITE}, description = "Display information on the Suite in the console log.")
    private static Boolean printSuite = false;

    @Parameter(names = {PRINT_CAUSE},  description = "Print the cause of a fail or skip test in the console log.")
    private static Boolean printCause = false;

    @Parameter(names = {PRINT_LOG}, description = "Print the test log file path in the console log")
    private static Boolean printLog = false;

    private static Boolean printLogOnlyFail = false;

    @Parameter(names = {V, VERSION}, description = "Print program version")
    private static Boolean version = false;

    @Parameter(names = {THREADS}, description = "The number of threads to run the test methods.")
    private static Integer threads = 50;

    @Parameter(names = {EXIT_STATUS}, description = "Completion with the specified status.")
    private static Integer status;

    @Parameter(names = {ARTIFACTS_URL}, description = "The storage address for the builds (artifacts).")
    private static String artifactsUrl;

    @Parameter(names = {PARALLEL_MODE}, description = "TestNG parallel mode.")
    private static BParallelMode parallelMode = METHODS;

    @Parameter(names = {C, COMPONENTS}, description = "List of tested components in the format: NAME,NAME,NAME.",
            validateWith = ParameterValidator.class, listConverter = ServiceConverter.class)
    private static List<Component> components = new ArrayList<Component>() {{ add(new AllComponents()); }};

    @Parameter(names = {S, SERVICES}, description = "List of tested services in the format: NAME,NAME,NAME.",
            validateWith = ParameterValidator.class, listConverter = ServiceConverter.class)
    private static List<Service> services = new ArrayList<Service>() {{ add(new AllServices()); }};

    @Parameter(names = {I, INTERFACE}, description = "List of tested interfaces in the format: NAME,NAME,NAME.",
            validateWith = ParameterValidator.class, listConverter = InterfaceConverter.class)
    private static List<Interface> interfaces = new ArrayList<Interface>() {{ add(new AllInterfaces()); }};

    @Parameter(names = {T, TYPE}, description = "Type of tests to run.", validateWith = ParameterValidator.class)
    private static List<Type> types = new ArrayList<Type>() {{ add(Type.ALL); }};

    public static Boolean getHelp() {
        return help;
    }

    public static void setHelp(Boolean help) {
        BuggyConfig.help = help;
    }

    public static Boolean getForce() {
        return force;
    }

    public static void setForce(Boolean force) {
        BuggyConfig.force = force;
    }

    public static Boolean getPrintSuite() {
        return printSuite;
    }

    public static void setPrintSuite(Boolean printSuite) {
        BuggyConfig.printSuite = printSuite;
    }

    public static Boolean getPrintCause() {
        return printCause;
    }

    public static void setPrintCause(Boolean printCause) {
        BuggyConfig.printCause = printCause;
    }

    public static Boolean getPrintLog() {
        return printLog;
    }

    public static void setPrintLog(Boolean printLog) {
        BuggyConfig.printLog = printLog;
    }

    public static Boolean getPrintLogOnlyFail() {
        return printLogOnlyFail;
    }

    public static void setPrintLogOnlyFail(Boolean printLogOnlyFail) {
        BuggyConfig.printLogOnlyFail = printLogOnlyFail;
    }

    public static Boolean getVersion() {
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

    public static Integer getStatus() {
        return status;
    }

    public static void setStatus(Integer status) {
        BuggyConfig.status = status;
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

    public static void addTypes(List<Type> types) {
        if (types != null && !types.isEmpty()) {
            BuggyConfig.types.addAll(types);
        }
    }

    public static void addTypes(Type... types) {
        if (types != null) {
            addTypes(Arrays.asList(types));
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

    public static void addServices(List<Service> services) {
        if (services != null) {
            BuggyConfig.services.addAll(services);
        }
    }

    public static void addServices(Service... services) {
        if (services != null) {
            addServices(Arrays.asList(services));
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

    public static void addInterfaces(List<Interface> interfaces) {
        if (interfaces != null) {
            BuggyConfig.interfaces.addAll(interfaces);
        }
    }

    public static void addInterfaces(Interface... interfaces) {
        if (interfaces != null) {
            addInterfaces(Arrays.asList(interfaces));
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

    public static void addComponents(List<Component> components) {
        if (components != null) {
            BuggyConfig.components.addAll(components);
        }
    }

    public static void addComponents(Component... components) {
        if (components != null) {
            addComponents(Arrays.asList(components));
        }
    }

    public static BParallelMode getParallelMode() {
        return parallelMode;
    }

    public static void setParallelMode(BParallelMode parallelMode) {
        BuggyConfig.parallelMode = parallelMode;
    }

    @Override
    public Map<String, Object> sort(Map<String, Object> map) {
        String[] sort = new String[]
                {THREADS, PARALLEL_MODE, F, PRINT_LOG, PRINT_CAUSE, PRINT_SUITE,
                        C, S, I, T, LOGS_PATH, ARTIFACTS_URL, EXIT_STATUS};
        Map<String, Object> sorted = new LinkedHashMap<>();
        for (String s : sort) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String[] keys = entry.getKey()
                        .replace(",", "")
                        .replace("]", "")
                        .replace("[", "")
                        .split(" ");
                for (String key : keys) {
                    if (key.equals(s)) {
                        sorted.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        sorted.putAll(map);
        return sorted;
    }

}
