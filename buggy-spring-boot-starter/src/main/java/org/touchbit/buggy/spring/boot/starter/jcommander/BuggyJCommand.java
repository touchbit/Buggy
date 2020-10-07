package org.touchbit.buggy.spring.boot.starter.jcommander;

import com.beust.jcommander.Parameter;
import org.touchbit.buggy.core.config.BuggyConfig;
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
public final class BuggyJCommand implements JCommand {

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
        BuggyJCommand.version = version;
    }

    @Parameter(names = {EXIT_STATUS}, description = "Completion with the specified status.")
    public static void setExitStatus(Integer exitStatus) {
        BuggyJCommand.exitStatus = exitStatus;
    }

    @Parameter(names = {QUESTION_MARK, HELP}, help = true, description = "Print usage.")
    public static void setHelp(Boolean help) {
        BuggyConfig.setHelp(help);
    }

    @Parameter(names = {F, FORCE}, description = "Running all tests, including those that fall.")
    public static void setForce(Boolean force) {
        BuggyConfig.setForce(force);
    }

    @Parameter(names = {PRINT_SUITE}, description = "Display information on the Suite in the console log.")
    public static void setPrintSuite(Boolean printSuite) {
        BuggyConfig.setPrintSuite(printSuite);
    }

    @Parameter(names = {PRINT_CAUSE}, description = "Print the cause of a fail or skip test in the console log.")
    public static void setPrintCause(Boolean printCause) {
        BuggyConfig.setPrintCause(printCause);
    }

    @Parameter(names = {PRINT_LOG}, description = "Print the test log file path in the console log")
    public static void setPrintLog(Boolean printLog) {
        BuggyConfig.setPrintLog(printLog);
    }

    @Parameter(names = {PRINT_LOG_IF_FAIL}, description = "Print the failed test log file path in the console log")
    public static void setPrintLogOnlyFail(Boolean printLogOnlyFail) {
        BuggyConfig.setPrintLogOnlyFail(printLogOnlyFail);
    }

    @Parameter(names = {THREADS}, description = "The number of threads to run the test methods.")
    public static void setThreads(Integer threads) {
        BuggyConfig.setThreads(threads);
    }

    @Parameter(names = {ARTIFACTS_URL}, description = "The storage address for the builds (artifacts).")
    public static void setArtifactsUrl(String artifactsUrl) {
        BuggyConfig.setArtifactsUrl(artifactsUrl);
    }

    @Parameter(names = {T, TYPE}, description = "Type of tests to run.", validateWith = ParameterValidator.class)
    public static void setTypes(List<Type> types) {
       BuggyConfig.setTypes(types);
    }

    public static void setTypes(Type... types) {
        BuggyConfig.setTypes(types);
    }

    @Parameter(names = {S, SERVICES}, description = "List of tested services in the format: NAME,NAME,NAME.",
            validateWith = ParameterValidator.class, listConverter = ServiceConverter.class)
    public static void setServices(List<Service> services) {
        BuggyConfig.setServices(services);
    }

    @SafeVarargs
    public static <S extends Service> void setServices(S... services) {
        BuggyConfig.setServices(services);
    }

    @SafeVarargs
    public static <S extends Service> void setServices(Class<S>... services) {
        BuggyConfig.setServices(services);
    }

    @Parameter(names = {I, INTERFACE}, description = "List of tested interfaces in the format: NAME,NAME,NAME.",
            validateWith = ParameterValidator.class, listConverter = InterfaceConverter.class)
    public static void setInterfaces(List<Interface> interfaces) {
        BuggyConfig.setInterfaces(interfaces);
    }

    @SafeVarargs
    public static <I extends Interface> void setInterfaces(I... interfaces) {
        BuggyConfig.setInterfaces(interfaces);
    }

    @SafeVarargs
    public static <I extends Interface> void setInterfaces(Class<I>... interfaces) {
        BuggyConfig.setInterfaces(interfaces);
    }

    @Parameter(names = {C, COMPONENTS}, description = "List of tested components in the format: NAME,NAME,NAME.",
            validateWith = ParameterValidator.class, listConverter = ServiceConverter.class)
    public static void setComponents(List<Component> components) {
        BuggyConfig.setComponents(components);
    }

    @SafeVarargs
    public static <C extends Component> void setComponents(C... components) {
        BuggyConfig.setComponents(components);
    }

    @SafeVarargs
    public static <C extends Component> void setComponents(Class<C>... components) {
        BuggyConfig.setComponents(components);
    }

    @Parameter(names = {PARALLEL_MODE}, description = "TestNG parallel mode.")
    public static void setParallelMode(ParallelMode parallelMode) {
        BuggyConfig.setParallelMode(parallelMode);
    }

    @Parameter(names = {PROGRAM_NAME}, description = "Current program name")
    public static void setProgramName(String programName) {
        BuggyConfig.setProgramName(programName);
    }

    @Parameter(names = {"--task-tracker-issue-url"}, description = "TODO")
    public static void setTaskTrackerIssueUrl(String taskTrackerIssueUrl) {
        BuggyConfig.setTaskTrackerIssueUrl(taskTrackerIssueUrl);
    }

    public static Boolean isHelp() {
        return BuggyConfig.isHelp();
    }

    public static Boolean isPrintLogFileOnlyFail() {
        return BuggyConfig.isPrintLog();
    }

    public static Boolean isForce() {
        return BuggyConfig.isForce();
    }

    public static Boolean isPrintSuite() {
        return BuggyConfig.isPrintSuite();
    }

    public static Boolean isPrintCause() {
        return BuggyConfig.isPrintCause();
    }

    public static Boolean isPrintLog() {
        return BuggyConfig.isPrintLog();
    }

    public static Boolean isVersion() {
        return BuggyConfig.isVersion();
    }

    public static Integer getThreads() {
        return BuggyConfig.getThreads();
    }

    public static String getArtifactsUrl() {
        return BuggyConfig.getArtifactsUrl();
    }

    public static List<Type> getTypes() {
        return BuggyConfig.getTypes();
    }

    public static List<Service> getServices() {
        return BuggyConfig.getServices();
    }

    public static List<Interface> getInterfaces() {
        return BuggyConfig.getInterfaces();
    }

    public static List<Component> getComponents() {
        return BuggyConfig.getComponents();
    }

    public static ParallelMode getParallelMode() {
        return BuggyConfig.getParallelMode();
    }

    public static String getProgramName() {
        return BuggyConfig.getProgramName();
    }

    public static String getTaskTrackerIssueUrl() {
        return BuggyConfig.getTaskTrackerIssueUrl();
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
