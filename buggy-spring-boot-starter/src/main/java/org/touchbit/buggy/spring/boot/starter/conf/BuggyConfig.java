package org.touchbit.buggy.spring.boot.starter.conf;

import com.beust.jcommander.Parameter;
import org.springframework.core.annotation.Order;
import org.touchbit.buggy.core.config.jcommander.InterfaceConverter;
import org.touchbit.buggy.core.config.jcommander.ParameterValidator;
import org.touchbit.buggy.core.config.jcommander.ServiceConverter;
import org.touchbit.buggy.core.config.jcommander.ValueValidator;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.process.Interface;
import org.touchbit.buggy.core.process.Service;
import org.touchbit.buggy.core.utils.BuggyUtils;

import java.util.List;

import static org.touchbit.buggy.core.config.BParameters.*;

/**
 * Configuration class for customizing Buggy.
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
@Order(1)
public class BuggyConfig implements JCConfiguration {

    @Parameter(names = HELP, help = true, description = "Print usage.")
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

    @Parameter(names = {SELF_CHECK}, description = "Check buggy configuration without test run.")
    private static Boolean check = false;

    @Parameter(names = {V, VERSION}, description = "Print program version", validateValueWith = ValueValidator.class)
    private static Boolean version = false;

    @Parameter(names = {THREADS}, description = "The number of threads to run the test methods.")
    private static Integer threads = 50;

    @Parameter(names = {STATUS}, description = "Completion with the specified status.")
    private static Integer status;

    @Parameter(names = {LOG}, description = "Absolute path to the directory for test logs.")
    private static String logPath = "logs";

    @Parameter(names = {ARTIFACTS_URL}, description = "The storage address for the builds (artifacts).")
    private static String artifactsUrl;

    @Parameter(names = {T, TYPE}, description = "Type of tests to run.", validateWith = ParameterValidator.class)
    private static Type type = Type.INTEGRATION;

    @Parameter(names = {S, SERVICES}, description = "List of tested services in the format: NAME,NAME,NAME.",
            validateWith = ParameterValidator.class, listConverter = ServiceConverter.class)
    private static List<Service> services = BuggyUtils.findServices();

    @Parameter(names = {I, INTERFACE}, description = "List of tested interfaces in the format: NAME,NAME,NAME.",
            validateWith = ParameterValidator.class, listConverter = InterfaceConverter.class)
    private static List<Interface> interfaces = BuggyUtils.findInterfaces();

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

    public static Boolean getCheck() {
        return check;
    }

    public static void setCheck(Boolean check) {
        BuggyConfig.check = check;
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

    public static String getLogPath() {
        return logPath;
    }

    public static void setLogPath(String logPath) {
        BuggyConfig.logPath = logPath;
    }

    public static String getArtifactsUrl() {
        return artifactsUrl;
    }

    public static void setArtifactsUrl(String artifactsUrl) {
        BuggyConfig.artifactsUrl = artifactsUrl;
    }

    public static Type getType() {
        return type;
    }

    public static void setType(Type type) {
        BuggyConfig.type = type;
    }

    public static List<Service> getServices() {
        return services;
    }

    public static void setServices(List<Service> services) {
        BuggyConfig.services = services;
    }

    public static List<Interface> getInterfaces() {
        return interfaces;
    }

    public static void setInterfaces(List<Interface> interfaces) {
        BuggyConfig.interfaces = interfaces;
    }

}
