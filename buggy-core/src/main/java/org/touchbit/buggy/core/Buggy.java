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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusLogger;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;
import org.touchbit.buggy.core.config.PrimaryConfig;
import org.touchbit.buggy.core.config.SecondaryConfig;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.testng.TestSuite;
import org.touchbit.buggy.core.testng.listeners.BuggyListener;
import org.touchbit.buggy.core.testng.listeners.IntellijIdeaTestNgPluginListener;
import org.touchbit.buggy.core.utils.BuggyUtils;
import org.touchbit.buggy.core.utils.StringUtils;
import org.touchbit.buggy.core.utils.log.BuggyLog;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.testng.xml.XmlSuite.FailurePolicy.CONTINUE;
import static org.touchbit.buggy.core.utils.BuggyUtils.CONSOLE_DELIMITER;

/**
 * Created by Oleg Shaburov on 08.09.2018
 * shaburov.o.a@gmail.com
 */
public abstract class Buggy {

    static {
        StatusLogger.getLogger().setLevel(Level.OFF);
    }

    private static final String UNABLE_CREATE_CLASS = "Unable to create a new instance of ";
    private static AtomicInteger buggyErrors = new AtomicInteger();
    private static AtomicInteger buggyWarns = new AtomicInteger();
    private static JCommander jCommander;
    private static Class<? extends BuggyLog> buggyLogClass;
    private static String programName = "Buggy";
    private static PrimaryConfig primaryConfig;
    private static Class<? extends PrimaryConfig> primaryConfigClass;
    private static List<Class<? extends SecondaryConfig>> secondaryConfigClasses = new ArrayList<>();
    private static List<SecondaryConfig> secondaryConfigs = new ArrayList<>();
    private static BuggyProcessor processor = new DefaultBuggyProcessor();
    private static boolean primaryConfigInitialized = false;
    private static boolean testRun = false;
    private static TestNG testNG = new TestNG();

    public static void main(String[] args) {
        delegate(new TestNG(), args);
    }

    @SuppressWarnings("WeakerAccess")
    public static void delegate(TestNG delegate, String... args) {
        reset();
        testNG = delegate;
        prepare(args);
        checkPrimaryConfig();
        run();
    }

    public static void run() {
        testRun = true;
        int status = processor.runTestNG(testNG);
        if (status == 60) {
            processor.getExitHandler().exitRun(60, "TestNG safely died.");
        } else {
            processor.getExitHandler().exitRun(status);
        }
        testRun = false;
    }

    public static void checkPrimaryConfig() {
        if (primaryConfig.isCheck()) {
            processor.getExitHandler().exitRun(0, "Buggy configuration is correct.");
        }
    }

    public static boolean isTestRun() {
        return testRun;
    }

    public static void reset() {
        testNG = new TestNG();
        jCommander = null;
        primaryConfigInitialized = false;
        testRun = false;
        buggyErrors.set(0);
        buggyWarns.set(0);
    }

    public static void prepare(String... args) {
        StringUtils.println(CONSOLE_DELIMITER);
        primaryConfig = processor.getPrimaryConfig(primaryConfigClass);
        primaryConfigClass = primaryConfig.getClass();
        secondaryConfigs = processor.getSecondaryConfigList(secondaryConfigClasses);
        jCommander = processor.prepareJCommander(primaryConfig, secondaryConfigs, programName);
        try {
            jCommander.parse(args);
        } catch (Exception e) {
            getExitHandler().exitRunWithUsage(1, e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        primaryConfigInitialized = processor.preparePrimaryConfig(primaryConfig);
        processor.prepareBuggyLog(buggyLogClass);
        StringUtils.println(CONSOLE_DELIMITER);
        processor.printConfig(primaryConfig);
        StringUtils.println(CONSOLE_DELIMITER);
        processor.prepareTestNG(testNG);
        StringUtils.println(CONSOLE_DELIMITER);
    }

    public static class DefaultBuggyProcessor implements BuggyProcessor {

        @Override
        public PrimaryConfig getPrimaryConfig(Class<? extends PrimaryConfig> primaryConfigClass) {
            PrimaryConfig primaryConfig = null;
            if (primaryConfigClass == null) {
                primaryConfigClass = getPrimaryConfigClass(BuggyUtils.findInstantiatedSubclasses(PrimaryConfig.class));
                Buggy.setPrimaryConfigClass(primaryConfigClass);
            }
            try {
                primaryConfig = primaryConfigClass.newInstance();
            } catch (Exception e) {
                this.getExitHandler().exitRun(1, UNABLE_CREATE_CLASS + primaryConfigClass, e);
            }
            return primaryConfig;
        }

        public Class<? extends PrimaryConfig> getPrimaryConfigClass(List<Class<? extends PrimaryConfig>> classes) {
            if (classes == null || !classes.iterator().hasNext()) {
                getExitHandler().exitRun(1, "Primary config implementation not found.");
            } else {
                if (classes.size() > 1) {
                    StringJoiner sj = new StringJoiner("\n");
                    classes.forEach(c -> sj.add(c.toString()));
                    getExitHandler().exitRun(1, "Found more than 1 inherited class from PrimaryConfig.class:\n" + sj);
                }
                return classes.get(0);
            }
            return null;
        }

        @Override
        public List<SecondaryConfig> getSecondaryConfigList(List<Class<? extends SecondaryConfig>> classList) {
            List<SecondaryConfig> secondaryConfigs = new ArrayList<>();
            if (classList == null || classList.isEmpty()) {
                classList = BuggyUtils.findInstantiatedSubclasses(SecondaryConfig.class);
            }
            classList.forEach(c -> {
                try {
                    secondaryConfigs.add(c.newInstance());
                } catch (Exception e) {
                    processor.getExitHandler().exitRun(1, UNABLE_CREATE_CLASS + c, e);
                }
            });
            return secondaryConfigs;
        }

        @Override
        public JCommander prepareJCommander(PrimaryConfig primary,
                                            List<SecondaryConfig> secondary,
                                            String name) {
            if (primary == null || secondary == null) {
                this.getExitHandler().exitRun(1,
                        "An invalid 'null' value was received for one of the parameters:" +
                                "\nPrimaryConfig = " + primary +
                                "\nSecondaryConfigs list = " + secondary);
            } else {
                JCommander jCommander = new JCommander(primary);
                jCommander.setProgramName(name);
                secondary.forEach(jCommander::addCommand);
                return jCommander;
            }
            return null;
        }

        @Override
        public boolean preparePrimaryConfig(PrimaryConfig primaryConfig) {
            primaryConfig.setRunDir(this.getRunDirectory(primaryConfig.getClass()));
            File absoluteLogDir;
            if (primaryConfig.getLogPath().startsWith("/")) {
                absoluteLogDir = new File(primaryConfig.getLogPath());
            } else {
                absoluteLogDir = new File(primaryConfig.getRunDir(), primaryConfig.getLogPath());
            }
            //noinspection ResultOfMethodCallIgnored
            absoluteLogDir.mkdirs();
            if (!absoluteLogDir.exists() || !absoluteLogDir.canWrite()) {
                Buggy.getExitHandler().exitRun(1, "No write permission for the specified log directory: '" +
                        absoluteLogDir.getAbsolutePath() + "'");
                return false;
            }
            primaryConfig.setAbsoluteLogPath(absoluteLogDir.getAbsolutePath());
            return true;
        }

        @Override
        public void prepareBuggyLog(Class<? extends BuggyLog> buggyLogClass) {
            if (buggyLogClass == null) {
                buggyLogClass = getBuggyLogSubClass(BuggyUtils.findInstantiatedSubclasses(BuggyLog.class));
            }
            try {
                buggyLogClass.newInstance();
            } catch (Exception e) {
                this.getExitHandler().exitRun(1, UNABLE_CREATE_CLASS + buggyLogClass.getTypeName(), e);
            }
        }

        public Class<? extends BuggyLog> getBuggyLogSubClass(List<Class<? extends BuggyLog>> list) {
            if (list == null) {
                return BuggyLog.class;
            }
            if (list.size() > 1) {
                StringJoiner sj = new StringJoiner("\n");
                list.forEach(c -> sj.add(c.toString()));
                this.getExitHandler().exitRun(1, "Found more than 1 inherited class from BaseLog.class:\n" + sj);
            }
            return list.size() == 1 ? list.get(0) : BuggyLog.class;
        }

        @Override
        public List<TestSuite> getTestSuites() {
            List<TestSuite> subTestSuites = getSubTestSuites(BuggyUtils.findInstantiatedSubclasses(TestSuite.class));
            List<Class<?>> annotatedTestClasses = findAnnotatedSuiteTestClasses();
            return mergeTestSuites(annotatedTestClasses, subTestSuites);
        }

        public List<TestSuite> getSubTestSuites(List<Class<? extends TestSuite>> subclasses) {
            List<TestSuite> testSuites = new ArrayList<>();
            if (subclasses != null) {
                for (Class<? extends TestSuite> testSuiteClass : subclasses) {
                    try {
                        if (!testSuiteClass.isAnnotationPresent(Suite.class)) {
                            processor.getExitHandler().exitRun(1, "The " + testSuiteClass + " " +
                                    "does not contain the annotation @Suite");
                        }
                        testSuites.add(testSuiteClass.newInstance());
                    } catch (Exception e) {
                        processor.getExitHandler().exitRun(1, UNABLE_CREATE_CLASS + testSuiteClass, e);
                    }
                }
            }
            return testSuites;
        }

        public List<Class<?>> findAnnotatedSuiteTestClasses() {
            return BuggyUtils.findAnnotatedInstantiatedClasses(Suite.class).stream()
                    .filter(s -> !BuggyUtils.isAssignableFrom(s, TestSuite.class))
                    .collect(Collectors.toList());
        }

        public List<TestSuite> mergeTestSuites(final List<Class<?>> annotated,
                                               final List<TestSuite> subTestSuites) {
            List<TestSuite> testClassSuites = new ArrayList<>();
            for (Class<?> testClass : annotated) {
                Suite testClassSuite = testClass.getAnnotation(Suite.class);
                if (BuggyUtils.isListBaseSuiteContainsClass(subTestSuites, testClass) ||
                        BuggyUtils.isListBaseSuiteContainsClass(testClassSuites, testClass)) {
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
                    s.addTestPackage("default", testClass);
                    testClassSuites.add(s);
                } else {
                    testSuite.addTestPackage("default", testClass);
                }
            }
            subTestSuites.addAll(testClassSuites);
            return subTestSuites;
        }

        @Override
        public List<XmlSuite> getXmlSuitesForConfiguration(PrimaryConfig config, List<TestSuite> testSuites) {
            List<XmlSuite> xmlSuites = new ArrayList<>();
            testSuites.forEach(suite -> config.getServices().forEach(service -> {
                if (suite.getService().getName().equals(service.getName())) {
                    config.getInterfaces().forEach(i -> {
                        if (suite.getInterface().getName().equals(i.getName())) {
                            xmlSuites.add(suite);
                        }
                    });
                }
            }));
            if (xmlSuites.isEmpty()) {
                getExitHandler().exitRun(1, "There are no test suites for the current configuration.");
            }
            return xmlSuites;
        }

        @Override
        public void prepareTestNG(TestNG testNG) {
            List<TestSuite> testSuites = getTestSuites();
            List<XmlSuite> xmlSuites = getXmlSuitesForConfiguration(primaryConfig, testSuites);
            testNG.setXmlSuites(xmlSuites);
            testNG.setSuiteThreadPoolSize(xmlSuites.size());
            testNG.setUseDefaultListeners(true);
            testNG.setConfigFailurePolicy(CONTINUE);
            testNG.setOutputDirectory(getReportsOutputDirectory());
            List<BuggyListener> listeners = getBuggyListeners(BuggyUtils
                    .findInstantiatedSubclasses(BuggyListener.class));
            listeners.forEach(testNG::addListener);
        }

        public List<BuggyListener> getBuggyListeners(List<Class<? extends BuggyListener>> subclasses) {
            List<BuggyListener> listeners = new ArrayList<>();
            if (IntellijIdeaTestNgPluginListener.isIntellijIdeaTestRun()) {
                StringUtils.println(StringUtils
                        .dotFiller(IntellijIdeaTestNgPluginListener.class.getSimpleName(), 47, "ENABLED"));
            } else {
                StringUtils.println(StringUtils
                        .dotFiller(IntellijIdeaTestNgPluginListener.class.getSimpleName(), 47, "DISABLED"));
                subclasses.remove(IntellijIdeaTestNgPluginListener.class);
                subclasses.forEach(l -> {
                    try {
                        BuggyListener listener = l.newInstance();
                        if (listener.isEnable()) {
                            listeners.add(listener);
                            StringUtils.println(StringUtils.dotFiller(l.getSimpleName(), 47, "ENABLED"));
                        } else {
                            StringUtils.println(StringUtils.dotFiller(l.getSimpleName(), 47, "DISABLED"));
                        }
                    } catch (Exception e) {
                        this.getExitHandler().exitRun(1, UNABLE_CREATE_CLASS + l, e);
                    }
                });
            }
            return listeners;
        }

        @Override
        @SuppressWarnings("squid:S1148")
        public int runTestNG(TestNG testNG) {
            try {
                testNG.run();
                if (primaryConfig.getStatus() != null) {
                    return primaryConfig.getStatus();
                }
                return testNG.getStatus();
            } catch (Throwable t) {
                t.printStackTrace();
                return 60;
            }
        }

        @Override
        public <T extends PrimaryConfig> String getRunDirectory(Class<T> clazz) {
            return new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath())
                    .getParentFile().getAbsolutePath();
        }

        @Override
        public String getReportsOutputDirectory() {
            return new File(this.getRunDirectory(primaryConfig.getClass()), "reports").getAbsolutePath() ;
        }

        @Override
        public ExitHandler getExitHandler() {
            return new DefaultBuggySystemExitHandler();
        }

        @Override
        public void printConfig(PrimaryConfig primaryConfig) {
            StringUtils.println(PrimaryConfig.configurationToString(primaryConfig));
        }

        public static class DefaultBuggySystemExitHandler implements ExitHandler {

            @Override
            public void exitRunWithUsage(int status, String msg) {
                if(jCommander != null) {
                    jCommander.usage();
                    StringUtils.println(CONSOLE_DELIMITER);
                    exitRun(status, msg);
                } else {
                    StringUtils.println(CONSOLE_DELIMITER);
                    exitRun(1, "JCommander not initialized.");
                }
            }

            @Override
            public void exit(int status) {
                java.lang.System.exit(status);
            }

        }

    }

    public static void setBuggyProcessor(BuggyProcessor processor) {
        Buggy.processor = processor;
    }

    public static ExitHandler getExitHandler() {
        return processor.getExitHandler();
    }

    public static boolean isPrimaryConfigInitialized() {
        return primaryConfigInitialized;
    }

    public static PrimaryConfig getPrimaryConfig() {
        return primaryConfig;
    }

    public static <T extends PrimaryConfig> void setPrimaryConfigClass(Class<T> c) {
        primaryConfigClass = c;
    }

    public static void setSecondaryConfigClasses(List<Class<? extends SecondaryConfig>> secondary) {
        secondaryConfigClasses = secondary;
    }

    public static void setProgramName(String name) {
        programName = name;
    }

    public static String getProgramName() {
        return programName;
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

    public static JCommander getJCommander() {
        return jCommander;
    }

    public static List<SecondaryConfig> getSecondaryConfigs() {
        return secondaryConfigs;
    }
}
