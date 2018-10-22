package org.touchbit.buggy.core.tests.common;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;
import org.touchbit.buggy.core.BuggyProcessor;
import org.touchbit.buggy.core.ExitHandler;
import org.touchbit.buggy.core.testng.TestSuite;
import org.touchbit.buggy.core.testng.listeners.BuggyListener;
import org.touchbit.buggy.core.tests.BaseUnitTest;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.config.*;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.utils.log.BuggyLog;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.xml.XmlSuite.ParallelMode.METHODS;

/**
 * Created by Oleg Shaburov on 16.10.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("Buggy class test")
class BuggyTests extends BaseUnitTest {

    @Nested
    @DisplayName("DefaultBuggyProcessor class tests")
    class DefaultBuggyProcessorTests extends BaseUnitTest {

        private final UnitTestBuggyProcessor processor = new UnitTestBuggyProcessor();

        @Test
        @DisplayName("GIVEN UnitTestPrimaryConfig.class WHEN getPrimaryConfig THEN PrimaryConfig")
        void unitTest_20181021190453() {
            PrimaryConfig result = processor.getPrimaryConfig(UnitTestPrimaryConfig.class);
            assertThat(result, is(notNullValue()));
            assertThat(result, is(instanceOf(UnitTestPrimaryConfig.class)));
        }

        @Test
        @DisplayName("GIVEN null WHEN getPrimaryConfig THEN PrimaryConfig")
        void unitTest_20181021190947() {
            PrimaryConfig result = processor.getPrimaryConfig(null);
            assertThat(result, is(notNullValue()));
            assertThat(EXIT_HANDLER.getMsg(), is(containsString("Found more than 1 inherited " +
                    "class from PrimaryConfig.class:")));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
        }

        @Test
        @DisplayName("GIVEN PrivatePrimaryConfig WHEN getPrimaryConfig THEN exit 1")
        void unitTest_20181021191129() {
            PrimaryConfig result = processor.getPrimaryConfig(PrivatePrimaryConfig.class);
            assertThat(result, is(nullValue()));
            assertThat(EXIT_HANDLER.getMsg(), is("Unable to create a new instance of " +
                    PrivatePrimaryConfig.class));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getThrowable().getMessage(),
                    is("Class org.touchbit.buggy.core.Buggy$DefaultBuggyProcessor can not access a member of " +
                            PrivatePrimaryConfig.class + " with modifiers \"private\""));
        }

        @Test
        @DisplayName("GIVEN PrimaryConfigList(1) WHEN getPrimaryConfigClass THEN PrimaryConfig")
        void unitTest_20181021192009() {
            List<Class<? extends PrimaryConfig>> list = new ArrayList<Class<? extends PrimaryConfig>>() {{
                add(UnitTestPrimaryConfig.class);
            }};
            Class<? extends PrimaryConfig> result = processor.getPrimaryConfigClass(list);
            assertThat(result, is(notNullValue()));
            assertThat(result, is(sameInstance(UnitTestPrimaryConfig.class)));
            assertThat(EXIT_HANDLER.getMsg(), is(nullValue()));
            assertThat(EXIT_HANDLER.getStatus(), is(nullValue()));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN PrimaryConfigList(2) WHEN getPrimaryConfigClass THEN PrimaryConfig")
        void unitTest_20181021192919() {
            List<Class<? extends PrimaryConfig>> list = new ArrayList<Class<? extends PrimaryConfig>>() {{
                add(UnitTestPrimaryConfig.class);
                add(UnitTestPrimaryConfig.class);
            }};
            Class<? extends PrimaryConfig> result = processor.getPrimaryConfigClass(list);
            assertThat(result, is(notNullValue()));
            assertThat(result, is(sameInstance(UnitTestPrimaryConfig.class)));
            assertThat(EXIT_HANDLER.getMsg(),
                    is(containsString("Found more than 1 inherited class from PrimaryConfig.class:")));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN PrimaryConfigList(0) WHEN getPrimaryConfigClass THEN PrimaryConfig")
        void unitTest_20181021193116() {
            List<Class<? extends PrimaryConfig>> list = new ArrayList<>();
            Class<? extends PrimaryConfig> result = processor.getPrimaryConfigClass(list);
            assertThat(result, is(nullValue()));
            assertThat(EXIT_HANDLER.getMsg(), is("Primary config implementation not found."));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN null WHEN getPrimaryConfigClass THEN PrimaryConfig")
        void unitTest_20181021193322() {
            Class<? extends PrimaryConfig> result = processor.getPrimaryConfigClass(null);
            assertThat(result, is(nullValue()));
            assertThat(EXIT_HANDLER.getMsg(), is("Primary config implementation not found."));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN SecondaryConfigClassList(1) WHEN getSecondaryConfigList THEN SecondaryConfigList(1)")
        void unitTest_20181021194144() {
            List<Class<? extends SecondaryConfig>> list = new ArrayList<>();
            list.add(UnitTestSecondaryConfig.class);
            List<SecondaryConfig> result = processor.getSecondaryConfigList(list);
            assertThat(result.size(), is(1));
            assertThat(result, is(contains(instanceOf(UnitTestSecondaryConfig.class))));
            assertThat(EXIT_HANDLER.getMsg(), is(nullValue()));
            assertThat(EXIT_HANDLER.getStatus(), is(nullValue()));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN SecondaryConfigClassList(2) WHEN getSecondaryConfigList THEN SecondaryConfigList(2)")
        void unitTest_20181021194443() {
            List<Class<? extends SecondaryConfig>> list = new ArrayList<>();
            list.add(UnitTestSecondaryConfig.class);
            list.add(UnitTestSecondaryConfig.class);
            List<SecondaryConfig> result = processor.getSecondaryConfigList(list);
            assertThat(result.size(), is(2));
            assertThat(result, is(contains(
                    instanceOf(UnitTestSecondaryConfig.class),
                    instanceOf(UnitTestSecondaryConfig.class)
            )));
            assertThat(EXIT_HANDLER.getMsg(), is(nullValue()));
            assertThat(EXIT_HANDLER.getStatus(), is(nullValue()));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN PrivateSecondaryConfig WHEN getSecondaryConfigList THEN exit 1")
        void unitTest_20181021194948() {
            List<Class<? extends SecondaryConfig>> list = new ArrayList<>();
            list.add(UnitTestSecondaryConfig.class);
            list.add(PrivateSecondaryConfig.class);
            processor.getSecondaryConfigList(list);
            assertThat(EXIT_HANDLER.getMsg(), is("Unable to create a new instance of " +
                    PrivateSecondaryConfig.class));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getThrowable().getMessage(),
                    is("Class org.touchbit.buggy.core.Buggy$DefaultBuggyProcessor can not access a member of " +
                            PrivateSecondaryConfig.class + " with modifiers \"private\""));
        }

        @Test
        @DisplayName("GIVEN SecondaryConfigClassList(0) WHEN getSecondaryConfigList THEN UnitTestSecondaryConfig")
        void unitTest_20181021195615() {
            List<SecondaryConfig> result = processor.getSecondaryConfigList(new ArrayList<>());
            assertThat(result, is(hasItem(instanceOf(UnitTestSecondaryConfig.class))));
            assertThat(EXIT_HANDLER.getMsg(), is(nullValue()));
            assertThat(EXIT_HANDLER.getStatus(), is(nullValue()));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN null WHEN getSecondaryConfigList THEN UnitTestSecondaryConfig")
        void unitTest_20181021195736() {
            List<SecondaryConfig> result = processor.getSecondaryConfigList(null);
            assertThat(result, is(hasItem(instanceOf(UnitTestSecondaryConfig.class))));
            assertThat(EXIT_HANDLER.getMsg(), is(nullValue()));
            assertThat(EXIT_HANDLER.getStatus(), is(nullValue()));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN empty args WHEN getJCommander THEN return JCommander")
        void unitTest_20181021200419() {
            JCommander result = processor.getJCommander(new UnitTestPrimaryConfig(), new ArrayList<>(), "");
            assertThat(result, is(notNullValue()));
            assertThat(EXIT_HANDLER.getMsg(), is(nullValue()));
            assertThat(EXIT_HANDLER.getStatus(), is(nullValue()));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN name==null WHEN getJCommander THEN return JCommander")
        void unitTest_20181021201229() {
            JCommander result = processor.getJCommander(new UnitTestPrimaryConfig(), new ArrayList<>(), null);
            assertThat(result, is(notNullValue()));
            assertThat(EXIT_HANDLER.getMsg(), is(nullValue()));
            assertThat(EXIT_HANDLER.getStatus(), is(nullValue()));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN SecondaryConfigList = null WHEN getJCommander THEN return JCommander")
        void unitTest_20181021201615() {
            JCommander result = processor.getJCommander(new UnitTestPrimaryConfig(), null, "name");
            assertThat(result, is(nullValue()));
            String msg = EXIT_HANDLER.getMsg();
            assertThat(msg, is(containsString("An invalid 'null' value was received for one of the parameters:")));
            assertThat(msg, is(containsString("SecondaryConfigs list = null")));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN PrimaryConfig = null WHEN getJCommander THEN return JCommander")
        void unitTest_20181021202628() {
            JCommander result = processor.getJCommander(null, new ArrayList<>(), "name");
            assertThat(result, is(nullValue()));
            String msg = EXIT_HANDLER.getMsg();
            assertThat(msg, is(containsString("An invalid 'null' value was received for one of the parameters:")));
            assertThat(msg, is(containsString("PrimaryConfig = null")));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN args = null WHEN getJCommander THEN return JCommander")
        void unitTest_20181021202801() {
            JCommander result = processor.getJCommander(new UnitTestPrimaryConfig(), new ArrayList<>(), "name", null);
            assertThat(result, is(nullValue()));
            String msg = EXIT_HANDLER.getMsg();
            assertThat(msg, is(containsString("An invalid 'null' value was received for one of the parameters:")));
            assertThat(msg, is(containsString("args = null")));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN args =  WHEN getJCommander THEN return JCommander")
        void unitTest_20181021202838() {
            JCommander result = processor
                    .getJCommander(new UnitTestPrimaryConfig(), new ArrayList<>(), "name", "--invalid-parameter");
            assertThat(result, is(nullValue()));
            assertThat(EXIT_HANDLER.getMsg(), is("ParameterException: Was passed main parameter '--invalid-parameter' " +
                    "but no main parameter was defined in your arg class"));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN relative logPath WHEN preparePrimaryConfig THEN true")
        void unitTest_20181021203059() {
            PrimaryConfig config = new PrimaryConfig() {
            };
            String logPath = config.getLogPath();
            String runDir = config.getRunDir();
            try {
                config.setLogPath("waste-unit-tests/LogPath_20181021203059");
                boolean result = processor.preparePrimaryConfig(config);
                assertThat(result, is(true));
                assertThat(config.getLogPath(), is("waste-unit-tests/LogPath_20181021203059"));
            } finally {
                config.setLogPath(logPath);
                config.setRunDir(runDir);
            }
        }

        @Test
        @DisplayName("GIVEN full logPath WHEN preparePrimaryConfig THEN true")
        void unitTest_20181021204652() {
            PrimaryConfig config = new PrimaryConfig() {
            };
            String logPath = config.getLogPath();
            String runDir = config.getRunDir();
            try {
                config.setLogPath(WASTE + "/unitTest_20181021204652");
                boolean result = processor.preparePrimaryConfig(config);
                assertThat(result, is(true));
                assertThat(config.getLogPath(), is(WASTE + "/unitTest_20181021204652"));
            } finally {
                config.setLogPath(logPath);
                config.setRunDir(runDir);
            }
        }

        @Test
        @DisplayName("GIVEN full logPath not exists WHEN preparePrimaryConfig THEN false")
        void unitTest_20181021204855() {
            PrimaryConfig config = new PrimaryConfig() {
            };
            String logPath = config.getLogPath();
            String runDir = config.getRunDir();
            try {
                config.setLogPath("/unitTest_20181021204652");
                boolean result = processor.preparePrimaryConfig(config);
                assertThat(result, is(false));
                assertThat(EXIT_HANDLER.getMsg(),
                        is("No write permission for the specified log directory: '/unitTest_20181021204652'"));
                assertThat(EXIT_HANDLER.getStatus(), is(1));
                assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
            } finally {
                config.setLogPath(logPath);
                config.setRunDir(runDir);
            }
        }

        @Test
        @DisplayName("GIVEN full logPath can not write WHEN preparePrimaryConfig THEN false")
        void unitTest_20181021205050() {
            PrimaryConfig config = new PrimaryConfig() {
            };
            String logPath = config.getLogPath();
            String runDir = config.getRunDir();
            try {
                config.setLogPath("/");
                boolean result = processor.preparePrimaryConfig(config);
                assertThat(result, is(false));
                assertThat(EXIT_HANDLER.getMsg(),
                        is("No write permission for the specified log directory: '/'"));
                assertThat(EXIT_HANDLER.getStatus(), is(1));
                assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
            } finally {
                config.setLogPath(logPath);
                config.setRunDir(runDir);
            }
        }

        @Test
        @DisplayName("GIVEN BuggyTestsBuggyLog WHEN prepareBuggyLog THEN no errors")
        void unitTest_20181021210611() {
            Buggy.setBuggyLogClass(BuggyTestsBuggyLog.class);
            Buggy.prepare();
            assertThat(EXIT_HANDLER.getMsg(), is(nullValue()));
            assertThat(EXIT_HANDLER.getStatus(), is(nullValue()));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN PrivateBuggyTestsBuggyLog WHEN prepareBuggyLog THEN exit 1")
        void unitTest_20181021211210() {
            processor.prepareBuggyLog(PrivateBuggyTestsBuggyLog.class);
            assertThat(EXIT_HANDLER.getMsg(), is("Unable to create a new instance of " +
                    PrivateBuggyTestsBuggyLog.class.getTypeName()));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getThrowable().getMessage(),
                    is("Class org.touchbit.buggy.core.Buggy$DefaultBuggyProcessor can not access a member of " +
                            PrivateBuggyTestsBuggyLog.class + " with modifiers \"private\""));
        }

        @Test
        @DisplayName("GIVEN null WHEN prepareBuggyLog THEN exit 1")
        void unitTest_20181021211708() {
            Buggy.prepare();
            processor.prepareBuggyLog(null);
            assertThat(EXIT_HANDLER.getMsg(), is(containsString("Found more than 1 inherited class from BaseLog.class:")));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN List(0) WHEN getBuggyLogSubClass THEN return BuggyLog")
        void unitTest_20181021213302() {
            Class<? extends BuggyLog> result = processor.getBuggyLogSubClass(new ArrayList<>());
            assertThat(result, is(sameInstance(BuggyLog.class)));
            assertThat(EXIT_HANDLER.getMsg(), is(nullValue()));
            assertThat(EXIT_HANDLER.getStatus(), is(nullValue()));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN null WHEN getBuggyLogSubClass THEN return BuggyLog")
        void unitTest_20181021213515() {
            Class<? extends BuggyLog> result = processor.getBuggyLogSubClass(null);
            assertThat(result, is(sameInstance(BuggyLog.class)));
            assertThat(EXIT_HANDLER.getMsg(), is(nullValue()));
            assertThat(EXIT_HANDLER.getStatus(), is(nullValue()));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN List(BuggyTestsBuggyLog) WHEN getBuggyLogSubClass THEN BuggyTestsBuggyLog")
        void unitTest_20181021213551() {
            List<Class<? extends BuggyLog>> logClasses = new ArrayList<>();
            logClasses.add(BuggyTestsBuggyLog.class);
            Class<? extends BuggyLog> result = processor.getBuggyLogSubClass(logClasses);
            assertThat(result, is(sameInstance(BuggyTestsBuggyLog.class)));
            assertThat(EXIT_HANDLER.getMsg(), is(nullValue()));
            assertThat(EXIT_HANDLER.getStatus(), is(nullValue()));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN null WHEN getSubTestSuites THEN List(0)")
        void unitTest_20181021221012() {
            List<TestSuite> result = processor.getSubTestSuites(null);
            assertThat(result, is(empty()));
            assertThat(EXIT_HANDLER.getMsg(), is(nullValue()));
            assertThat(EXIT_HANDLER.getStatus(), is(nullValue()));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN List(null) WHEN getSubTestSuites THEN exit 1")
        void unitTest_20181021221222() {
            List<Class<? extends TestSuite>> subclasses = new ArrayList<>();
            subclasses.add(null);
            List<TestSuite> result = processor.getSubTestSuites(subclasses);
            assertThat(result, is(empty()));
            assertThat(EXIT_HANDLER.getMsg(), is("Unable to create a new instance of null"));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getThrowable().getMessage(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN List(PrivateTestSuite) WHEN getSubTestSuites THEN exit 1")
        void unitTest_20181021221526() {
            List<Class<? extends TestSuite>> subclasses = new ArrayList<>();
            subclasses.add(PrivateTestSuite.class);
            List<TestSuite> result = processor.getSubTestSuites(subclasses);
            assertThat(result, is(empty()));
            assertThat(EXIT_HANDLER.getMsg(), is("Unable to create a new instance of " +
                    PrivateTestSuite.class));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getThrowable().getMessage(),
                    is("Class org.touchbit.buggy.core.Buggy$DefaultBuggyProcessor can not access a member of " +
                            PrivateTestSuite.class + " with modifiers \"private\""));
        }

        @Test
        @DisplayName("GIVEN TestSuite List contains Annotated testClass WHEN mergeTestSuites THEN return TestSuite(1)")
        void unitTest_20181022003106() {
            TestSuite suite = new TestSuite("name_20181022003106", 50, METHODS, getSuite());
            suite.addTestPackage("unitTest_20181022003106", TestNGTestClassWithSuite.class);
            List<TestSuite> list = new ArrayList<>();
            list.add(suite);
            List<Class<?>> annotated = new ArrayList<>();
            annotated.add(TestNGTestClassWithSuite.class);
            List<TestSuite> result = processor.mergeTestSuites(annotated, list);
            assertThat(result, is(contains(suite)));
        }

        @Test
        @DisplayName("GIVEN TestSuite List not contains Annotated testClass WHEN mergeTestSuites THEN return default TestSuite(1)")
        void unitTest_20181022005810() {
            List<TestSuite> list = new ArrayList<>();
            List<Class<?>> annotated = new ArrayList<>();
            annotated.add(TestNGTestClassWithSuite.class);
            List<TestSuite> result = processor.mergeTestSuites(annotated, list);
            assertThat(result.size(), is(1));
            assertThat(result.get(0).getName(), is("DEFAULT TESTSERVICE TESTINTERFACE suite"));
            assertThat(result.get(0).getTests().size(), is(1));
            assertThat(result.get(0).getTests().get(0).getXmlClasses().size(), is(1));
            assertThat(result.get(0).getTests().get(0).getXmlClasses().get(0).getName(),
                    is(TestNGTestClassWithSuite.class.getTypeName()));
        }

        @Test
        @DisplayName("GIVEN duplicates Annotated testClass WHEN mergeTestSuites THEN return default TestSuite(1)")
        void unitTest_20181022011006() {
            List<TestSuite> list = new ArrayList<>();
            List<Class<?>> annotated = new ArrayList<>();
            annotated.add(TestNGTestClassWithSuite.class);
            annotated.add(TestNGTestClassWithSuite.class);
            annotated.add(TestNGTestClassWithSuite.class);
            annotated.add(TestNGTestClassWithSuite.class);
            List<TestSuite> result = processor.mergeTestSuites(annotated, list);
            assertThat(result.size(), is(1));
            assertThat(result.get(0).getName(), is("DEFAULT TESTSERVICE TESTINTERFACE suite"));
            assertThat(result.get(0).getTests().size(), is(1));
            assertThat(result.get(0).getTests().get(0).getXmlClasses().size(), is(1));
            assertThat(result.get(0).getTests().get(0).getXmlClasses().get(0).getName(),
                    is(TestNGTestClassWithSuite.class.getTypeName()));
        }

        @Test
        @DisplayName("GIVEN no test suites WHEN getXmlSuitesForConfiguration THEN exit 1")
        void unitTest_20181022012052() {
            List<XmlSuite> xmlSuites =
                    processor.getXmlSuitesForConfiguration(Buggy.getPrimaryConfig(), new ArrayList<>());
            assertThat(xmlSuites, is(empty()));
            assertThat(EXIT_HANDLER.getMsg(), is("There are no test suites for the current configuration."));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getThrowable(), is(nullValue()));
        }

        @Test
        @DisplayName("GIVEN AbstractBuggyListener WHEN getBuggyListeners THEN exit 1")
        void unitTest_20181022012947() {
            List<Class<? extends BuggyListener>> subclasses = new ArrayList<>();
            subclasses.add(AbstractBuggyListener.class);
            List<BuggyListener> listeners = processor.getBuggyListeners(subclasses);
            assertThat(listeners, is(empty()));
            assertThat(EXIT_HANDLER.getMsg(), is("Unable to create a new instance of " + AbstractBuggyListener.class));
            assertThat(EXIT_HANDLER.getStatus(), is(1));
        }

        @Test
        @DisplayName("GIVEN status 0 WHEN jar run with errors THEN exit 0")
        void unitTest_20181022014502() {
            try {
                Buggy.getPrimaryConfig().setStatus(0);
                TestNG testNG = new TestNG();
                testNG.setUseDefaultListeners(false);
                int status = processor.runTestNG(testNG);
                assertThat(status, is(0));
            } finally {
                Buggy.getPrimaryConfig().setStatus(null);
            }
        }

        @Test
        @DisplayName("GIVEN new TestNG WHEN runTestNG THEN status 8")
        void unitTest_20181022015017() {
            TestNG testNG = new TestNG();
            testNG.setUseDefaultListeners(false);
            int status = processor.runTestNG(testNG);
            assertThat(status, is(8));
        }

        @Test
        @DisplayName("GIVEN null WHEN runTestNG THEN status 60")
        void unitTest_20181022015030() {
            int status = processor.runTestNG(null);
            assertThat(status, is(60));
        }

        @Test
        @DisplayName("WHEN getReportsOutputDirectory THEN ends with reports")
        void unitTest_20181022015230() {
            assertThat(processor.getRealReportsOutputDirectory(), is(endsWith("reports")));
        }

        @Test
        @DisplayName("WHEN getExitHandler THEN DefaultBuggySystemExitHandler")
        void unitTest_20181022015527() {
            assertThat(processor.getRealExitHandler(), is(not(nullValue())));
            assertThat(processor.getRealExitHandler().getClass().getSimpleName(), is("DefaultBuggySystemExitHandler"));
        }

        @Test
        @DisplayName("GIVEN ExitHandler WHEN exitRunWithUsage(10) THEN exit 10")
        void unitTest_20181022025250() {
            Buggy.prepare();
            processor.getExitHandler().realExitRunWithUsage(10, "unitTest_20181022025250");
            assertThat(EXIT_HANDLER.getStatus(), is(10));
            assertThat(EXIT_HANDLER.getMsg(), is("unitTest_20181022025250"));
        }

        @Test
        @DisplayName("GIVEN  WHEN  THEN")
        void unitTest_20181022030541() {
            new UnitTestBuggyProcessor().getExitHandler().realExitRunWithUsage(10, "unitTest_20181022025250");
            assertThat(EXIT_HANDLER.getStatus(), is(1));
            assertThat(EXIT_HANDLER.getMsg(), is("JCommander not initialized."));
        }
    }

    @Test
    @DisplayName("GIVEN Buggy.prepare() WHEN getJCommander() THEN not null")
    void unitTest_20181022020129() {
        Buggy.prepare();
        assertThat(Buggy.getJCommander(), is(notNullValue()));
    }

    @Test
    @DisplayName("GIVEN Buggy.prepare() WHEN getJCommander() THEN not null")
    void unitTest_20181022020247() {
        Buggy.setPrimaryConfigClass(null);
        Buggy.prepare();
    }

    @Test
    @DisplayName("GIVEN --print-log WHEN jar run THEN console log contains test log path")
    void unitTest_20181022014156() {
        Buggy.main(new String[]{"--print-log"});
        assertThat(TEST_LOGGER.takeLoggedMessages(),
                is(hasItems("test_20181016172050.....................SUCCESS \u2B9E " +
                        "file://" + Buggy.getPrimaryConfig().getAbsoluteLogPath() + "/tests/test_20181016172050.log")));
    }

    @Test
    @DisplayName("GIVEN empty args WHEN jar run THEN console log does not test log path")
    void unitTest_20181022014322() {
        Buggy.main(new String[]{});
        assertThat(TEST_LOGGER.takeLoggedMessages(),
                is(hasItems("test_20181016172050.....................SUCCESS")));
    }

    @Test
    @DisplayName("GIVEN isCheck = true WHEN delegate THEN exit 0")
    void unitTest_20181022022949() {
        Buggy.prepare();
        TestNG testNG = new TestNG();
        testNG.setUseDefaultListeners(false);
        Buggy.getPrimaryConfig().setCheck(true);
        Buggy.delegate(testNG);
        assertThat(EXIT_HANDLER.getMsg(), is("Buggy configuration is correct."));
        assertThat(EXIT_HANDLER.getStatus(), is(0));
    }

    @Test
    @DisplayName("GIVEN isCheck = false WHEN delegate THEN exit 0")
    void unitTest_20181022023246() {
        Buggy.prepare();
        TestNG testNG = new TestNG();
        testNG.setUseDefaultListeners(false);
        Buggy.getPrimaryConfig().setCheck(false);
        Buggy.delegate(testNG);
        assertThat(EXIT_HANDLER.getMsg(), is(nullValue()));
        assertThat(EXIT_HANDLER.getStatus(), is(0));
    }

    @Test
    @DisplayName("GIVEN broken TestNG WHEN delegate THEN exit 60")
    void unitTest_20181022024731() {
        Buggy.setBuggyProcessor(new TestNGDieProcessor());
        Buggy.prepare();
        TestNG testNG = new TestNG();
        testNG.setUseDefaultListeners(false);
        Buggy.delegate(testNG);
        assertThat(EXIT_HANDLER.getMsg(), is("TestNG safely died."));
        assertThat(EXIT_HANDLER.getStatus(), is(60));
    }

    public static class TestNGDieProcessor extends UnitTestBuggyProcessor {

        @Override
        public int runTestNG(TestNG testNG) {
            return 60;
        }

    }

    private abstract class AbstractBuggyListener implements BuggyListener {}

    private static class PrivateTestSuite extends TestSuite { }

    private static class PrivateBuggyTestsBuggyLog extends BuggyLog { }

    public static class BuggyTestsBuggyLog extends BuggyLog {
        public BuggyTestsBuggyLog() {
            super(TEST_LOGGER, TEST_LOGGER, TEST_LOGGER);
        }
    }

    private static class PrivatePrimaryConfig implements PrimaryConfig { }

    @Parameters(commandNames = "junit")
    private static class PrivateSecondaryConfig implements SecondaryConfig { }

    @Suite(component = TestComponent.class, service = TestService.class, interfaze = TestInterface.class, task = "task")
    public static class BuggyTestsClass {
        @org.testng.annotations.Test(description = "test_20181016172050 description")
        @Details
        public void test_20181016172050() {}
    }

}
