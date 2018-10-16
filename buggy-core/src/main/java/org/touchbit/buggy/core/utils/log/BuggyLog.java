/*
 * Copyright © 2018 Shaburov Oleg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.touchbit.buggy.core.utils.log;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.atteo.classindex.IndexSubclasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.utils.IOHelper;
import org.touchbit.buggy.core.utils.StringUtils;

import java.io.File;

/**
 * Basic implementation of the test logger.
 * <p>
 * Created by Oleg Shaburov on 17.05.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings({"WeakerAccess", "unused", "squid:S1118"})
@IndexSubclasses
public class BuggyLog {

    private static final String CONFIG = "log4j2.xml";
    private static final String DEFAULT_CONFIG = "buggy-log4j2.xml";

    public static final String LOG_FILE_NAME = "log.file.name";
    public static final String LOG_DIRECTORY = "log.directory";

    protected static Logger console;
    protected static Logger framework;
    protected static Logger test;
    private static String logPath;
    private static Configuration configuration;

    static {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
    }

    public BuggyLog(Logger consoleLogger, Logger frameworkLogger, Logger testLogger) {
        checkBuggyConfiguration();
        setConsoleLog(consoleLogger);
        setFrameworkLog(frameworkLogger);
        setTestLog(testLogger);
    }

    public static String getLogsDirPath() {
        return logPath;
    }

    public static void setLogsDirPath(String path) {
        if (path == null) {
            throw new BuggyConfigurationException("The path to the log directory can not be empty");
        }
        logPath = path;
        System.setProperty(LOG_DIRECTORY, logPath);
    }

    public static void setTestLogFileName(String logName) {
        MDC.put(LOG_FILE_NAME, logName);
    }

    public static synchronized void reloadConfig() {
        checkBuggyConfiguration();
        Exception exception = null;
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
        if (exception == null) {
            init();
        }
        StringUtils.println(StringUtils.filler(null, "\u2014", 47, null));
        StringUtils.println(StringUtils.filler("Logger", " ", 47, "Level"));
        StringUtils.println(StringUtils.filler(null, "\u2014", 47, null));
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
            framework.info("Log4j2 loggers: {}", configuration.getLoggerContext().getLoggers());
            framework.info("Log4j2 configuration location: {}", configuration.getLoggerContext().getConfigLocation());
            String conf = IOHelper.readFile(new File(configuration.getLoggerContext().getConfigLocation()));
            framework.debug("Log4j2 configuration:\n{}", conf);
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
        BuggyLog.console = console;
    }

    public static void setFrameworkLog(Logger framework) {
        BuggyLog.framework = framework;
    }

    public static void setTestLog(Logger test) {
        BuggyLog.test = test;
    }

    private static void checkBuggyConfiguration() {
        if (!Buggy.isPrimaryConfigInitialized()) {
            Buggy.getExitHandler().exitRun(1, "The logger cannot be initialized before the Buggy configuration.");
        }
    }

    private static void init() {
        setConsoleLog(LoggerFactory.getLogger("Console"));
        setFrameworkLog(LoggerFactory.getLogger("Framework"));
        setTestLog(LoggerFactory.getLogger("Routing"));
    }

}
