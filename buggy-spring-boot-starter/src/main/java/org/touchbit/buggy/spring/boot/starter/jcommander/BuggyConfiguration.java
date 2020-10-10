package org.touchbit.buggy.spring.boot.starter.jcommander;

import com.beust.jcommander.Parameter;
import org.touchbit.buggy.core.config.BuggyConfigurationYML;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.model.ParallelMode;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.spring.boot.starter.jcommander.converters.InterfaceConverter;
import org.touchbit.buggy.spring.boot.starter.jcommander.converters.ParameterValidator;
import org.touchbit.buggy.spring.boot.starter.jcommander.converters.ServiceConverter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.touchbit.buggy.spring.boot.starter.jcommander.BParameters.*;

/**
 * Configuration class for customizing Buggy.
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("unused")
public final class BuggyConfiguration implements JCommand {

    private static Boolean version;
    private static Integer exitStatus;

    public static Boolean getVersion() {
        return version;
    }

    public static Integer getExitStatus() {
        return exitStatus;
    }

    @Parameter(names = {V, VERSION}, description = "Print program version")
    public static void setVersion(Boolean version) {
        BuggyConfiguration.version = version;
    }

    @Parameter(names = {EXIT_STATUS}, description = "Completion with the specified status.")
    public static void setExitStatus(Integer exitStatus) {
        BuggyConfiguration.exitStatus = exitStatus;
    }

    //  ---------------------- Map default BuggyConfigurationYML values ---------------------- //

    @Parameter(names = {QUESTION_MARK, HELP}, help = true, description = "Print usage.")
    public static void setHelp(Boolean help) {
        BuggyConfigurationYML.setHelp(help);
    }

    @Parameter(names = {F, FORCE}, description = "Running all tests, including those that fall.")
    public static void setForce(Boolean force) {
        BuggyConfigurationYML.setForce(force);
    }

    @Parameter(names = {TEST_SUITE_INFO}, description = "Display information on the Suite in the console log.")
    public static void setPrintSuite(Boolean printSuite) {
        BuggyConfigurationYML.setPrintSuite(printSuite);
    }

    @Parameter(names = {PRINT_CAUSE}, description = "Print the cause of a fail or skip test in the console log.")
    public static void setPrintCause(Boolean printCause) {
        BuggyConfigurationYML.setPrintCause(printCause);
    }

    @Parameter(names = {TEST_LOG_FILE_PATH}, description = "Print the test log file path in the console log")
    public static void setPrintLog(Boolean printLog) {
        BuggyConfigurationYML.setPrintLog(printLog);
    }

    @Parameter(names = {PRINT_LOG_IF_FAIL}, description = "Print the failed test log file path in the console log")
    public static void setPrintLogOnlyFail(Boolean printLogOnlyFail) {
        BuggyConfigurationYML.setPrintLogOnlyFail(printLogOnlyFail);
    }

    @Parameter(names = {THREADS}, description = "The number of threads to run the test methods.")
    public static void setThreads(Integer threads) {
        BuggyConfigurationYML.setThreads(threads);
    }

    @Parameter(names = {ARTIFACTS_URL}, description = "The storage address for the builds (artifacts).")
    public static void setArtifactsUrl(String artifactsUrl) {
        BuggyConfigurationYML.setArtifactsUrl(artifactsUrl);
    }

    @Parameter(names = {T, TYPE}, description = "Type of tests to run.", validateWith = ParameterValidator.class)
    public static void setTypes(List<Type> types) {
       BuggyConfigurationYML.setTypes(types);
    }

    public static void setTypes(Type... types) {
        BuggyConfigurationYML.setTypes(types);
    }

    @Parameter(names = {S, SERVICES}, description = "List of tested services in the format: NAME,NAME,NAME.",
            validateWith = ParameterValidator.class, listConverter = ServiceConverter.class)
    public static void setServices(List<Service> services) {
        BuggyConfigurationYML.setServices(services);
    }

    @SafeVarargs
    public static <S extends Service> void setServices(S... services) {
        BuggyConfigurationYML.setServices(services);
    }

    @SafeVarargs
    public static <S extends Service> void setServices(Class<S>... services) {
        BuggyConfigurationYML.setServices(services);
    }

    @Parameter(names = {I, INTERFACE}, description = "List of tested interfaces in the format: NAME,NAME,NAME.",
            validateWith = ParameterValidator.class, listConverter = InterfaceConverter.class)
    public static void setInterfaces(List<Interface> interfaces) {
        BuggyConfigurationYML.setInterfaces(interfaces);
    }

    @SafeVarargs
    public static <I extends Interface> void setInterfaces(I... interfaces) {
        BuggyConfigurationYML.setInterfaces(interfaces);
    }

    @SafeVarargs
    public static <I extends Interface> void setInterfaces(Class<I>... interfaces) {
        BuggyConfigurationYML.setInterfaces(interfaces);
    }

    @Parameter(names = {C, COMPONENTS}, description = "List of tested components in the format: NAME,NAME,NAME.",
            validateWith = ParameterValidator.class, listConverter = ServiceConverter.class)
    public static void setComponents(List<Component> components) {
        BuggyConfigurationYML.setComponents(components);
    }

    @SafeVarargs
    public static <C extends Component> void setComponents(C... components) {
        BuggyConfigurationYML.setComponents(components);
    }

    @SafeVarargs
    public static <C extends Component> void setComponents(Class<C>... components) {
        BuggyConfigurationYML.setComponents(components);
    }

    @Parameter(names = {PARALLEL_MODE}, description = "TestNG parallel mode.")
    public static void setParallelMode(ParallelMode parallelMode) {
        BuggyConfigurationYML.setParallelMode(parallelMode);
    }

    @Parameter(names = {PROGRAM_NAME}, description = "Current program name")
    public static void setProgramName(String programName) {
        BuggyConfigurationYML.setProgramName(programName);
    }

    @Parameter(names = {"--issues-url"}, description = "Task tracker issues URL.")
    public static void setTaskTrackerIssueUrl(String taskTrackerIssueUrl) {
        BuggyConfigurationYML.setIssuesUrl(taskTrackerIssueUrl);
    }

    public static Boolean isHelp() {
        return BuggyConfigurationYML.isHelp();
    }

    public static Boolean isPrintLogFileOnlyFail() {
        return BuggyConfigurationYML.isPrintLog();
    }

    public static Boolean isForce() {
        return BuggyConfigurationYML.isForce();
    }

    public static Boolean isPrintSuite() {
        return BuggyConfigurationYML.isPrintSuite();
    }

    public static Boolean isPrintCause() {
        return BuggyConfigurationYML.isPrintCause();
    }

    public static Boolean isPrintLog() {
        return BuggyConfigurationYML.isPrintLog();
    }

    public static Boolean isVersion() {
        return BuggyConfigurationYML.isVersion();
    }

    public static Integer getThreads() {
        return BuggyConfigurationYML.getThreads();
    }

    public static String getArtifactsUrl() {
        return BuggyConfigurationYML.getArtifactsUrl();
    }

    public static List<Type> getTypes() {
        return BuggyConfigurationYML.getTypes();
    }

    public static List<Service> getServices() {
        return BuggyConfigurationYML.getServices();
    }

    public static List<Interface> getInterfaces() {
        return BuggyConfigurationYML.getInterfaces();
    }

    public static List<Component> getComponents() {
        return BuggyConfigurationYML.getComponents();
    }

    public static ParallelMode getParallelMode() {
        return BuggyConfigurationYML.getParallelMode();
    }

    public static String getProgramName() {
        return BuggyConfigurationYML.getProgramName();
    }

    public static String getTaskTrackerIssueUrl() {
        return BuggyConfigurationYML.getIssuesUrl();
    }

    @Override
    public Map<String, Object> sort(Map<String, Object> map) {
        String[] sort = new String[]
                {THREADS, PARALLEL_MODE, F, TEST_LOG_FILE_PATH, PRINT_CAUSE, TEST_SUITE_INFO,
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
