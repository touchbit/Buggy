package org.touchbit.buggy.core.tests.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.touchbit.buggy.core.tests.BaseUnitTest;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.utils.IOHelper;
import org.touchbit.buggy.core.utils.log.BuggyLog;
import org.touchbit.buggy.core.utils.log.XMLLogConfigurator;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.touchbit.buggy.core.utils.log.BuggyLog.LOG_DIRECTORY;
import static org.touchbit.buggy.core.utils.log.BuggyLog.LOG_FILE_NAME;

/**
 * Created by Oleg Shaburov on 16.09.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
@DisplayName("BuggyLog Tests")
class BuggyLogTests extends BaseUnitTest {



    @Test
    @DisplayName("Check LogManager")
    void unitTest_20180916003840() {
        BuggyLog.getLogsDirPath();
        assertThat(System.getProperty("java.util.logging.manager"),
                is("org.apache.logging.log4j.jul.LogManager"));
    }

    @Test
    @DisplayName("Check setLogsDirPath(logPath)")
    void unitTest_20180916011731() {
        String logPath = WASTE + "/logs";
        BuggyLog.setLogsDirPath(logPath);
        assertThat(BuggyLog.getLogsDirPath(), is(logPath));
        assertThat(System.getProperty(LOG_DIRECTORY), is(logPath));
    }

    @Test
    @DisplayName("Check setLogsDirPath(null)")
    void unitTest_20180916012313() {
        BuggyConfigurationException e = execute(() -> BuggyLog.setLogsDirPath(null), BuggyConfigurationException.class);
        assertThat(e.getMessage(), is("The path to the log directory can not be empty"));
    }

    @Test
    @DisplayName("Check setTestLogFileName(String logName)")
    void unitTest_20180916015450() {
        BuggyLog.setTestLogFileName("unitTest_20180916015450");
        assertThat(MDC.get(LOG_FILE_NAME), is("unitTest_20180916015450"));
    }

    @Test
    @DisplayName("Check loadDefaultConfig() with root logger")
    void unitTest_20180916015619() throws IOException {
        BuggyLog.setLogsDirPath(WASTE);
        File testResourcesWaste = new File(WASTE, "log4j2.xml");
        File srcResourcesWaste = new File(WASTE, "buggy-log4j2.xml");
        File testResources = new File(TEST_CLASSES, "log4j2.xml");
        File srcResources = new File(CLASSES, "buggy-log4j2.xml");
        try {
            IOHelper.moveFile(testResources, testResourcesWaste);
            IOHelper.moveFile(srcResources, srcResourcesWaste);
            Buggy.prepare();
            BuggyLog.loadDefaultConfig();
        } finally {
            IOHelper.moveFile(testResourcesWaste, testResources);
            IOHelper.moveFile(srcResourcesWaste, srcResources);
        }
    }

    @Test
    @DisplayName("Check loadDefaultConfig() with log4j2.xml")
    void unitTest_20180916212753() {
        BuggyLog.setLogsDirPath(WASTE);
        BuggyLog.loadDefaultConfig();
    }

    @Test
    @DisplayName("Check loadDefaultConfig() with buggy-log4j2.xml")
    void unitTest_20180916212008() throws IOException {
        BuggyLog.setLogsDirPath(WASTE);
        File waste = new File(WASTE, "log4j2.xml");
        File resources = new File(TEST_CLASSES, "log4j2.xml");
        try {
            IOHelper.moveFile(resources, waste);
            BuggyLog.loadDefaultConfig();
        } finally {
            IOHelper.moveFile(waste, resources);
        }
    }

    @Test
    @DisplayName("Check BuggyLog constructor")
    void unitTest_20180916025621() {
        new BuggyLog(null, null, null);
        assertThat(BuggyLog.console(), is(nullValue()));
        assertThat(BuggyLog.framework(), is(nullValue()));
        assertThat(BuggyLog.test(), is(nullValue()));
        Logger logger = LoggerFactory.getLogger("test");
        new BuggyLog(logger, logger, logger);
        assertThat(BuggyLog.console(), is(logger));
        assertThat(BuggyLog.framework(), is(logger));
        assertThat(BuggyLog.test(), is(logger));
    }

    @Test
    @DisplayName("GIVEN BuggyLog WHEN BuggyLog() THEN no errors")
    void unitTest_20181021171040() {
        new BuggyLog();
        assertThat(BuggyLog.console(), is(not(nullValue())));
        assertThat(BuggyLog.framework(), is(not(nullValue())));
        assertThat(BuggyLog.test(), is(not(nullValue())));
        assertThat(BuggyLog.console(), is(instanceOf(Logger.class)));
        assertThat(BuggyLog.framework(), is(instanceOf(Logger.class)));
        assertThat(BuggyLog.test(), is(instanceOf(Logger.class)));
    }

    @Test
    @DisplayName("Check XMLLogConfigurator constructor")
    void unitTest_20180916214825() throws NoSuchMethodException {
        checkUtilityClassConstructor(XMLLogConfigurator.class);
    }

    @Test
    @DisplayName("Check init logger before Buggy")
    void unitTest_20181017010157() {
        Buggy.reset();
        new BuggyLog(null, null, null);
        assertExitCode(1, "The logger cannot be initialized before the Buggy configuration.");
    }

}
