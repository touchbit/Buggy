package org.touchbit.buggy.spring.boot.starter.conf;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.Loader;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.touchbit.buggy.spring.boot.starter.log.ConfigurationLogger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;

/**
 * Logback loggers configuration
 * <p>
 * Created by Oleg Shaburov on 30.09.2020
 * shaburov.o.a@gmail.com
 */
@Configuration()
@ConditionalOnNotWebApplication
@DependsOn({"initAndGetBuggyConfigurations", "isBuggyConfigured"})
public class SBLogbackConfiguration implements SBConfiguration {

    public static final String LOG_FILE_NAME = "log.file.name";
    public static final String LOG_DIRECTORY = "log.directory";
    protected static final String LOGBACK_XML = "logback.xml";
    protected static final String BUGGY_LOGBACK_XML = "buggy-logback.xml";
    protected static final String DEFAULT_LOGGING_CONFIG_VALUE = "classpath:" + BUGGY_LOGBACK_XML;
    private static Logger console;
    private static Logger framework;
    private static Logger test;
    private final boolean isBuggyLoggerInitialized;
    private final String loggingConfigFile;

    public SBLogbackConfiguration(Environment env) {
        loggingConfigFile = env.getProperty("logging.config");
        ConfigurationLogger.blockDelimeter();
        ConfigurationLogger.centerBold("Loading logback configuration");
        ConfigurationLogger.stepDelimeter();
        URL logbackXml = getResource(LOGBACK_XML, this.getClass());
        URL buggyLogbackXml = getResource("buggy-logback.xml", this.getClass());
        LoggerContext context = null;
        if (logbackXml != null) {
            ConfigurationLogger.fPrint(LOGBACK_XML, ".", "OK");
            context = reloadLogger(logbackXml);
        } else {
            ConfigurationLogger.fPrint(LOGBACK_XML, ".", "FAIL");
        }

        if (logbackXml == null && buggyLogbackXml != null) {
            ConfigurationLogger.fPrint(BUGGY_LOGBACK_XML, ".", "OK");
            context = reloadLogger(buggyLogbackXml);
        } else {
            ConfigurationLogger.fPrint(BUGGY_LOGBACK_XML, ".", "FAIL");
            exitRunWithErr("Logback configuration not found");
        }
        if (context != null) {
            setTestLog(LoggerFactory.getLogger("Routing"));
            setConsoleLog(LoggerFactory.getLogger("Console"));
            setFrameworkLog(LoggerFactory.getLogger("Framework"));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(stream);
            StatusPrinter.setPrintStream(printStream);
            StatusPrinter.printInCaseOfErrorsOrWarnings(context);
            framework.info(stream.toString());
            for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
                if (logger.getLevel() != null) {
                    String name = logger.getName();
                    try {
                        Class<?> c = this.getClass().getClassLoader().loadClass(name);
                        name = c.getSimpleName() + ".class";
                    } catch (ClassNotFoundException ignore) {
                    }
                    console.info(ConfigurationLogger.filler(name, ".", logger.getLevel()));
                }
            }
        }
        isBuggyLoggerInitialized = true;
    }

    @Bean("isBuggyLoggerInitialized")
    public boolean isBuggyLoggerInitialized() {
        return isBuggyLoggerInitialized;
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

    public LoggerContext reloadLogger(URL config) {
        ConfigurationLogger.stepDelimeter();
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        // Already loaded. Return current context.
        if (DEFAULT_LOGGING_CONFIG_VALUE.equals(loggingConfigFile)) {
            return loggerContext;
        }
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(loggerContext);
            loggerContext.reset();
            configurator.doConfigure(config);
        } catch (JoranException ignore) {
        }
        return loggerContext;
    }

    public URL getResource(String filename, Class<?> c) {
        try {
            ClassLoader myClassLoader = Loader.getClassLoaderOfObject(c);
            return Loader.getResource(filename, myClassLoader);
        } catch (Exception ignore) {
            return null;
        }
    }

    public void setConsoleLog(Logger console) {
        SBLogbackConfiguration.console = console;
    }

    public void setFrameworkLog(Logger framework) {
        SBLogbackConfiguration.framework = framework;
    }

    public void setTestLog(Logger test) {
        SBLogbackConfiguration.test = test;
    }

}
