package org.touchbit.buggy.spring.boot.starter.jcommander;

import com.beust.jcommander.Parameter;
import org.touchbit.buggy.core.config.BuggyConfigurationYML;
import org.touchbit.buggy.core.config.JCommand;
import org.touchbit.buggy.core.config.OutputRule;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.model.ParallelMode;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.spring.boot.starter.jcommander.converters.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.touchbit.buggy.spring.boot.starter.jcommander.CommandNames.*;

/**
 * Configuration class for customizing Buggy.
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("unused")
public final class BuggyConfiguration implements JCommand {

    private static Boolean version = false;
    private static Integer exitStatus = null;

    public static Boolean getVersion() {
        return version;
    }

    public static Integer getExitStatus() {
        return exitStatus;
    }

    @Parameter(names = {VERSION}, description = "Print program version")
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
        BuggyConfigurationYML.setForceRun(force);
    }

    @Parameter(names = {THREADS}, description = "The number of threads to run the test methods.")
    public static void setThreads(Integer threads) {
        BuggyConfigurationYML.setThreads(threads);
    }

    @Parameter(names = {ARTIFACTS_URL}, description = "The storage address for the builds (artifacts).")
    public static void setArtifactsUrl(String artifactsUrl) {
        BuggyConfigurationYML.setArtifactsUrl(artifactsUrl);
    }

    @Parameter(names = {T, TYPE}, description = "Type of tests to run.", validateWith = ParameterValidator.class,
            listConverter = TypeListConverter.class)
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

    @Parameter(names = {I, INTERFACES}, description = "List of tested interfaces in the format: NAME,NAME,NAME.",
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
            validateWith = ParameterValidator.class, listConverter = ComponentConverter.class)
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
    public static void setIssuesUrl(String taskTrackerIssueUrl) {
        BuggyConfigurationYML.setIssuesUrl(taskTrackerIssueUrl);
    }

    public static Boolean isHelp() {
        return BuggyConfigurationYML.isHelp();
    }

    @Parameter(names = {TEST_CASE_TITLE}, arity = 1, description = "Print the title of the test case to the console log.")
    public static void setTestCaseTitle(Boolean testCaseTitle) {
        BuggyConfigurationYML.setTestCaseTitle(testCaseTitle);
    }

    @Parameter(names = {TEST_SUITE_INFO}, description = "Print the suite information in the console log.")
    public static void setTestSuiteInfo(Boolean testSuiteInfo) {
        BuggyConfigurationYML.setTestSuiteInfo(testSuiteInfo);
    }

    @Parameter(names = {TEST_LOG_FILE_PATH}, description = "Print the test log file path in the console log.")
    public static void setTestLogFilePath(Boolean testLogFilePath) {
        BuggyConfigurationYML.setTestLogFilePath(testLogFilePath);
    }

    @Parameter(names = {TEST_ISSUES_INFO}, description = "Print URLs of related issues.")
    public static void setTestIssuesInfo(Boolean testIssuesInfo) {
        BuggyConfigurationYML.setTestIssuesInfo(testIssuesInfo);
    }

    @Parameter(names = {TEST_BUGS_INFO}, description = "Print URLs of related bugs.")
    public static void setTestBugsInfo(Boolean testBugsInfo) {
        BuggyConfigurationYML.setTestBugsInfo(testBugsInfo);
    }

    @Parameter(names = {TEST_ERROR_INFO}, description = "Print the cause of a fail test in the console log.")
    public static void setTestErrorInfo(Boolean testErrorInfo) {
        BuggyConfigurationYML.setTestErrorInfo(testErrorInfo);
    }

    @Parameter(names = {OUTPUT_RULE}, description = "Print the cause of a fail test in the console log.")
    public static void setOutputRule(OutputRule outputRule) {
        BuggyConfigurationYML.setOutputRule(outputRule);
    }

    public static Boolean getTestCaseTitle() {
        return BuggyConfigurationYML.getTestCaseTitle();
    }

    public static Boolean getTestSuiteInfo() {
        return BuggyConfigurationYML.getTestSuiteInfo();
    }

    public static Boolean getTestLogFilePath() {
        return BuggyConfigurationYML.getTestLogFilePath();
    }

    public static Boolean getTestIssuesInfo() {
        return BuggyConfigurationYML.getTestIssuesInfo();
    }

    public static Boolean getTestBugsInfo() {
        return BuggyConfigurationYML.getTestBugsInfo();
    }

    public static Boolean getTestErrorInfo() {
        return BuggyConfigurationYML.getTestErrorInfo();
    }

    public static OutputRule getOutputRule() {
        return BuggyConfigurationYML.getOutputRule();
    }

    public static Boolean isVersion() {
        return version;
    }

    public static Boolean isForce() {
        return BuggyConfigurationYML.isForceRun();
    }

    public static Integer getThreads() {
        return BuggyConfigurationYML.getThreads();
    }

    public static String getArtifactsUrl() {
        return BuggyConfigurationYML.getArtifactsUrl();
    }

    public static Type[] getTypes() {
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
                {THREADS, PARALLEL_MODE, F, TEST_CASE_TITLE, TEST_LOG_FILE_PATH, TEST_SUITE_INFO, TEST_LOG_FILE_PATH,
                        TEST_ISSUES_INFO, TEST_BUGS_INFO, TEST_ERROR_INFO, OUTPUT_RULE, C, S, I, T,
                        LOGS_PATH, ARTIFACTS_URL, EXIT_STATUS};
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
