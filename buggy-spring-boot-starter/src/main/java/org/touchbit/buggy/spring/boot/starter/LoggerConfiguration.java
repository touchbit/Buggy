package org.touchbit.buggy.spring.boot.starter;

import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.*;
import org.touchbit.buggy.core.utils.IOHelper;
import org.touchbit.buggy.core.utils.StringUtils;
import org.touchbit.buggy.core.utils.log.XMLLogConfigurator;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration()
@ConditionalOnNotWebApplication
@DependsOn({"initAndGetBuggyConfigurations", "isBuggyConfigured"})
public class LoggerConfiguration extends BaseConfiguration {

    private static final String CONFIG = "log4j2.xml";
    private static final String DEFAULT_CONFIG = "buggy-log4j2.xml";
    public static final String LOG_FILE_NAME = "log.file.name";
    public static final String LOG_DIRECTORY = "log.directory";

    @Autowired
    private static boolean isBuggyConfigured;

    private static Logger console;
    private static Logger framework;
    private static Logger test;
    private static String logPath;
    private static boolean isBuggyLoggerInitialized;

    public LoggerConfiguration() {
        loadDefaultConfig();
        isBuggyLoggerInitialized = true;
    }

    @Bean("isBuggyLoggerInitialized")
    public boolean isBuggyLoggerInitialized() {
        return isBuggyLoggerInitialized;
    }

    public static String getLogsDirPath() {
        return logPath;
    }

    public static void setLogsDirPath(String path) {
        if (path == null) {
            exitRun("The path to the log directory can not be empty");
        }
        logPath = path;
        System.setProperty(LOG_DIRECTORY, logPath);
    }

    public static void setTestLogFileName(String logName) {
        MDC.put(LOG_FILE_NAME, logName);
    }

    public static synchronized void loadDefaultConfig() {
        checkBuggyConfiguration();
        Exception exception = null;
        org.apache.logging.log4j.core.config.Configuration configuration = null;
        try {
            configuration = XMLLogConfigurator.reloadXMLConfiguration(CONFIG);
        } catch (Exception e) {
            exception = e;
        }
        StringUtils.println(StringUtils
                .dotFiller("Load " + CONFIG + " configuration", 47, exception != null ? "FAIL" : "OK"));
        if (exception != null) {
            exception = null;
            try {
                configuration = XMLLogConfigurator.reloadXMLConfiguration(DEFAULT_CONFIG);
            } catch (Exception e) {
                exception = e;
            }
            StringUtils.println(StringUtils
                    .dotFiller("Load " + DEFAULT_CONFIG + " configuration", 47,
                            exception != null ? "FAIL" : "OK"));
        }
//        if (exception == null) { todo
//        }
        configuration.getLoggerContext().reconfigure();
        setTestLog(LoggerFactory.getLogger("Routing"));
        setConsoleLog(LoggerFactory.getLogger("Console"));
        setFrameworkLog(LoggerFactory.getLogger("Framework"));
        StringUtils.sPrint();
        StringUtils.fPrint("Logger", " ", "Level");
        StringUtils.sPrint();
        if (configuration != null) {
            configuration.getLoggers()
                    .forEach((k, v) -> {
                        String name = v.getName();
                        // Short Class name
                        if (name.contains(".") && name.lastIndexOf('.') < name.length() - 1) {
                            name = name.substring(name.lastIndexOf('.') + 1);
                        }
                        StringUtils
                                .println(StringUtils.dotFiller(name, 47, v.getLevel()));
                    });
            framework.info("Log4j2 appenders: {}", configuration.getLoggerContext().getConfiguration().getAppenders());
            framework.info("Log4j2 loggers: {}", configuration.getLoggers());
            framework.info("Log4j2 configuration location: {}", configuration.getLoggerContext().getConfigLocation());
            String conf = IOHelper.readFile(new File(configuration.getLoggerContext().getConfigLocation()));
            framework.trace("Log4j2 configuration:\n{}", conf);
        } else {
            LoggerConfig root = XMLLogConfigurator.getRootLoggerConfig();
            StringUtils.println(StringUtils.dotFiller(root, 47, root.getLevel()));
        }
    }

    public static Logger console() {
        return console;
    }

    public static Logger test() {
        return test;
    }

    public static Logger framework() {
        return framework;
    }

    public static void setConsoleLog(Logger console) {
        LoggerConfiguration.console = console;
    }

    public static void setFrameworkLog(Logger framework) {
        LoggerConfiguration.framework = framework;
    }

    public static void setTestLog(Logger test) {
        LoggerConfiguration.test = test;
    }

    private static void checkBuggyConfiguration() {
        if (isBuggyConfigured) {
             exitRun("The logger cannot be initialized before the Buggy configuration.");
        }
        if (logPath == null) {
            setLogsDirPath("./");
        }
    }

}
