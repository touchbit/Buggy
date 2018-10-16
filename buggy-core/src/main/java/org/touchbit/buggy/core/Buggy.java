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

package org.touchbit.buggy.core;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusLogger;
import org.atteo.classindex.ClassIndex;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;
import org.touchbit.buggy.core.config.PrimaryConfig;
import org.touchbit.buggy.core.config.SecondaryConfig;
import org.touchbit.buggy.core.exceptions.BuggyException;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.testng.TestSuite;
import org.touchbit.buggy.core.testng.listeners.BuggyListener;
import org.touchbit.buggy.core.testng.listeners.IntellijIdeaTestNgPluginListener;
import org.touchbit.buggy.core.utils.BuggyUtils;
import org.touchbit.buggy.core.utils.StringUtils;
import org.touchbit.buggy.core.utils.log.BuggyLog;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.touchbit.buggy.core.utils.BuggyUtils.CONSOLE_DELIMITER;

/**
 * Created by Oleg Shaburov on 08.09.2018
 * shaburov.o.a@gmail.com
 */
public abstract class Buggy {

    private static final String UNABLE_CREATE_CLASS = "Unable to create a new instance of class: ";

    private static AtomicInteger buggyErrors = new AtomicInteger(0);
    private static AtomicInteger buggyWarns = new AtomicInteger(0);

    private static TestNG testNG;
    private static JCommander jCommander;
    private static Class<? extends BuggyLog> buggyLogClass;
    private static String programName;
    private static String runDir;
    private static String reportsOutputDirectory;
    private static PrimaryConfig primaryConfig;
    private static Class<? extends PrimaryConfig> primaryConfigClass;
    private static ExitHandler exitHandler = new BuggyExitHandler();

    private static boolean configurationInitialized = false;
    private static boolean running = false;

    static {
        StatusLogger.getLogger().setLevel(Level.OFF);
    }

    public static void main(String[] args) {
        main(args, new TestNG());
        initConfiguration(args);
        run();
    }

    public static void main(String[] args, TestNG tng) {
        testNG = tng;
        initConfiguration(args);
        run();
    }

    public static boolean isRunning() {
        return running;
    }

    public static void initConfiguration(String... args) {
        StringUtils.println(CONSOLE_DELIMITER);
        initBuggyConfiguration();
        initJCommander(args);
        initLogs();
        addBuggyListeners();
        printConfig();
        configurationInitialized = true;
    }

    public static void initBuggyConfiguration() {
        if (primaryConfigClass == null) {
            primaryConfigClass = getPrimaryConfigClass();
        }
        runDir = new File(primaryConfigClass.getProtectionDomain().getCodeSource()
                .getLocation().getPath()).getParentFile().getAbsolutePath();
        reportsOutputDirectory = runDir + "/reports";
    }

    public static void initJCommander(String... args) {
        try {
            primaryConfig = primaryConfigClass.newInstance();
        } catch (Exception e) {
            exitHandler.exitRun(1, "Unable to create a new instance of PrimaryConfig class: " + primaryConfig, e);
        }
        jCommander = new JCommander(primaryConfig);
        jCommander.setProgramName(programName);
        getSecondaryConfigList().forEach(c -> {
            try {
                jCommander.addCommand(c.newInstance());
            } catch (Exception e) {
                exitHandler.exitRun(1, UNABLE_CREATE_CLASS + c, e);
            }
        });
        jCommander.setDefaultProvider(primaryConfig.getDefaultValueProvider());
        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            exitHandler.exitRunWithUsage(1, e.getMessage());
        }
    }

    public static void run() {
        List<XmlSuite> xmlSuites = getXmlSuites();
        if (xmlSuites.isEmpty()) {
            exitHandler.exitRun(1, "There are no test suites for the current configuration.");
        }
        try {
            testNG.setXmlSuites(xmlSuites);
            testNG.setSuiteThreadPoolSize(xmlSuites.size());
            testNG.setUseDefaultListeners(true);
            testNG.setOutputDirectory(reportsOutputDirectory);
            running = true;
            testNG.run();
            if (primaryConfig.getStatus() != null) {
                exitHandler.exitRun(primaryConfig.getStatus());
            }
            exitHandler.exitRun(testNG.getStatus());
        } catch (Throwable t) {
            exitHandler.exitRun(60, "TestNG safely died.", t);
        } finally {
            running = false;
        }
    }

    public static void initLogs() {
        if (primaryConfig.getLogPath().startsWith("/")) {
            primaryConfig.setAbsoluteLogPath(primaryConfig.getLogPath());
        } else {
            primaryConfig.setAbsoluteLogPath(runDir.endsWith("/") ? "" : runDir + "/" + primaryConfig.getLogPath());
        }
        File absoluteLogDir = new File(primaryConfig.getAbsoluteLogPath());
        if (!absoluteLogDir.mkdirs() && !absoluteLogDir.exists() && !absoluteLogDir.canWrite()) {
            exitHandler.exitRun(1, "There is no write permission for the log directory or the path to the file is specified: " +
                    absoluteLogDir.getAbsolutePath());
        }
        BuggyLog.setLogPath(primaryConfig.getAbsoluteLogPath());
        Iterable<Class<? extends BuggyLog>> subclasses = ClassIndex.getSubclasses(BuggyLog.class);
        List<Class<? extends BuggyLog>> logList = new ArrayList<>();
        for (Class<? extends BuggyLog> c : subclasses) {
            if (!Modifier.isAbstract(c.getModifiers())) {
                logList.add(c);
            }
        }
        if (logList.isEmpty()) {
            BuggyLog.reloadConfig();
            StringUtils.println(CONSOLE_DELIMITER);
            return;
        }
        if (logList.size() > 1 && buggyLogClass == null) {
            StringJoiner sj = new StringJoiner("\n");
            logList.forEach(c -> sj.add(c.toString()));
            exitHandler.exitRun(1, "Found more than 1 inherited class from BaseLog.class: " + logList);
        }
        if (buggyLogClass == null) {
            buggyLogClass = logList.get(0);
        }
        try {
            buggyLogClass.newInstance();
        } catch (Exception e) {
            exitHandler.exitRun(1, UNABLE_CREATE_CLASS + logList.get(0), e);
        }
    }

    public static List<XmlSuite> getXmlSuites() {
        if (!configurationInitialized) {
            throw new BuggyException("Configuration not initialized");
        }
        List<XmlSuite> xmlSuites = new ArrayList<>();
        getSuites().forEach(suite -> primaryConfig.getServices().forEach(service -> {
            if (suite.getService().getName().equals(service.getName())) {
                primaryConfig.getInterfaces().forEach(i -> {
                    if (suite.getInterface().getName().equals(i.getName())) {
                        xmlSuites.add(suite);
                    }
                });
            }
        }));
        return xmlSuites;
    }

    private static void addBuggyListeners() {
        final List<Class<? extends BuggyListener>> buggyListenerImpl = StreamSupport
                .stream(ClassIndex.getSubclasses(BuggyListener.class).spliterator(), false)
                .filter(l -> !Modifier.isAbstract(l.getModifiers()))
                .collect(Collectors.toList());
        if (IntellijIdeaTestNgPluginListener.isIntellijIdeaTestRun()) {
            List<Class<? extends BuggyListener>> intellijIdeaTestNgPluginListener = buggyListenerImpl.stream()
                    .filter(l -> isAssignableFrom(l, IntellijIdeaTestNgPluginListener.class))
                    .collect(Collectors.toList());
            intellijIdeaTestNgPluginListener.forEach(buggyListenerImpl::remove);
            StringUtils.println(StringUtils
                    .dotFiller(IntellijIdeaTestNgPluginListener.class.getSimpleName(), 47, "ENABLED"));
            StringUtils.println(CONSOLE_DELIMITER);
            return;
        } else {
            buggyListenerImpl.remove(IntellijIdeaTestNgPluginListener.class);
            StringUtils.println(StringUtils
                    .dotFiller(IntellijIdeaTestNgPluginListener.class.getSimpleName(), 47, "DISABLED"));
            buggyListenerImpl.forEach(l -> {
                try {
                    if (Modifier.isPublic(l.getModifiers()) && !Modifier.isAbstract(l.getModifiers())) {
                        BuggyListener listener = l.newInstance();
                        if (listener.isEnable()) {
                            testNG.addListener(listener);
                            StringUtils.println(StringUtils
                                    .dotFiller(l.getSimpleName(), 47, "ENABLED"));
                        } else {
                            StringUtils.println(StringUtils
                                    .dotFiller(l.getSimpleName(), 47, "DISABLED"));
                        }
                    }
                } catch (Exception e) {
                    throw new BuggyException(UNABLE_CREATE_CLASS + l, e);
                }
            });
        }
        StringUtils.println(CONSOLE_DELIMITER);
    }

    private static boolean isAssignableFrom(Class<?> checkedClass, Class<? extends BuggyListener> assignableClass) {
        if (checkedClass == null || checkedClass.isInstance(Object.class)) {
            return false;
        }
        if (checkedClass == assignableClass) {
            return true;
        } else {
            return isAssignableFrom(checkedClass.getSuperclass(), assignableClass);
        }
    }

    public static void printConfig() {
        StringUtils.println(PrimaryConfig.configurationToString(primaryConfig));
        StringUtils.println(CONSOLE_DELIMITER);
    }

    private static Class<? extends PrimaryConfig> getPrimaryConfigClass() {
        Iterable<Class<? extends PrimaryConfig>> subclasses = ClassIndex.getSubclasses(PrimaryConfig.class);
        List<Class<? extends PrimaryConfig>> primaryConfigList = new ArrayList<>();
        for (Class<? extends PrimaryConfig> c : subclasses) {
            if (!Modifier.isAbstract(c.getModifiers())) {
                primaryConfigList.add(c);
            }
        }
        if (primaryConfigList.isEmpty()) {
            exitHandler.exitRun(1, "PrimaryConfig implementation not found. See: \n" + PrimaryConfig.class);
        }
        if (primaryConfigList.size() > 1 && primaryConfig == null) {
            StringJoiner sj = new StringJoiner("\n");
            primaryConfigList.forEach(c -> sj.add(c.toString()));
            exitHandler.exitRun(1, "Found more than 1 inherited class from BaseConfig.class: " + primaryConfigList);
        }
        return primaryConfigList.get(0);
    }

    private static List<Class<? extends SecondaryConfig>> getSecondaryConfigList() {
        Iterable<Class<? extends SecondaryConfig>> subclasses = ClassIndex.getSubclasses(SecondaryConfig.class);
        List<Class<? extends SecondaryConfig>> secondaryConfigs = new ArrayList<>();
        for (Class<? extends SecondaryConfig> c : subclasses) {
            if (!Modifier.isAbstract(c.getModifiers())) {
                secondaryConfigs.add(c);
            }
        }
        return secondaryConfigs;
    }

    public static List<TestSuite> getSuites() {
        List<TestSuite> testSuites = new ArrayList<>();
        Iterable<Class<? extends TestSuite>> subclasses = ClassIndex.getSubclasses(TestSuite.class);
        for (Class<? extends TestSuite> c : subclasses) {
            if (!c.isAnnotationPresent(Suite.class)) {
                exitHandler.exitRun(1, "The " + c + " does not contain the annotation @Suite");
            }
            if (!Modifier.isAbstract(c.getModifiers())) {
                try {
                    testSuites.add(c.newInstance());
                } catch (Exception e) {
                    exitHandler.exitRun(1, "Unable to create a new instance of TestSuite class: " + c, e);
                }
            }
        }
        addTestClassSuites(testSuites);
        return testSuites;
    }

    private static void addTestClassSuites(List<TestSuite> testSuites) {
        List<TestSuite> testClassSuites = new ArrayList<>();
        Iterable<Class<?>> tmp = ClassIndex.getAnnotated(Suite.class);
        List<Class<?>> annotated = new ArrayList<>();
        tmp.forEach(c -> {if (!TestSuite.class.isAssignableFrom(c)) annotated.add(c);});
        for (Class<?> aClass : annotated) {
            Suite testClassSuite = aClass.getAnnotation(Suite.class);
            if (BuggyUtils.isListBaseSuiteContainsClass(testSuites, aClass) ||
                    BuggyUtils.isListBaseSuiteContainsClass(testClassSuites, aClass)) {
                continue;
            }
            TestSuite testSuite = null;
            for (TestSuite s : testClassSuites) {
                if (BuggyUtils.equalsSuites(s.getSuite(), testClassSuite)) {
                    testSuite = s;
                }
            }
            if (testSuite == null) {
                TestSuite s = new TestSuite(testClassSuite);
                s.addTestPackage("default", aClass);
                testClassSuites.add(s);
            } else {
                testSuite.addTestPackage("default", aClass);
            }
        }
        testSuites.addAll(testClassSuites);
    }

    public static ExitHandler getExitHandler() {
        return exitHandler;
    }

    public static void setExitHandler(ExitHandler exitHandler) {
        Buggy.exitHandler = exitHandler;
    }

    public static PrimaryConfig getPrimaryConfig() {
        return primaryConfig;
    }

    public static String getRunDir() {
        return runDir;
    }

    public static <T extends PrimaryConfig> void setPrimaryConfigClass(Class<T> c) {
        if (c != null) {
            primaryConfigClass = c;
        }
    }

    public static void setProgramName(String name) {
        programName = name;
    }

    public static String getProgramName() {
        return programName;
    }

    protected static void setReportsOutputDirectory(String path) {
        reportsOutputDirectory = path;
    }

    public static void incrementBuggyWarns() {
        buggyWarns.incrementAndGet();
    }

    public static void incrementBuggyErrors() {
        buggyErrors.incrementAndGet();
    }

    public static int getBuggyWarns() {
        return buggyWarns.get();
    }

    public static int getBuggyErrors() {
        return buggyErrors.get();
    }

    public static void setBuggyLogClass(Class<? extends BuggyLog> logClass) {
        buggyLogClass = logClass;
    }

    private static final class BuggyExitHandler implements ExitHandler {

        @Override
        public void exitRunWithUsage(int status, String msg) {
            if(jCommander != null) {
                jCommander.usage();
            } else {
                exitRun(1, "JCommander not initialized.");
            }
            exitRun(status, msg);
        }

    }

}
