package org.touchbit.buggy.core.tests.common;

import org.apache.logging.slf4j.Log4jLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.helpful.UnitTestLogger;
import org.touchbit.buggy.core.tests.BaseUnitTest;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.config.*;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.testng.listeners.BuggyExecutionListener;
import org.touchbit.buggy.core.utils.log.BuggyLog;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * Created by Oleg Shaburov on 16.10.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("Buggy class test")
class BuggyTests extends BaseUnitTest {

    @BeforeEach
    void beforeRunBuggy() {
        Buggy.setBuggyLogClass(TestBuggyLog.class);
        Buggy.setPrimaryConfigClass(UnitTestPrimaryConfig.class);
        BuggyExecutionListener.setSteps(new ArrayList<>());
    }

    @Test
    @DisplayName("Check test log")
    void unitTest_20181016141402() {
        Buggy.main(new String[]{});
        Buggy.run();
        assertThat(UNIT_TEST_LOGGER.takeLoggedMessages(),
                is(hasItems("test_20181016172050.....................SUCCESS")));
    }

    @Test
    @DisplayName("Check test log with --print-log = true")
    void unitTest_20181016212852() {
        Buggy.main(new String[]{"--print-log"});
        Buggy.run();
        assertThat(UNIT_TEST_LOGGER.takeLoggedMessages(),
                is(hasItems("test_20181016172050.....................SUCCESS \u2B9E " +
                        "file://" + Buggy.getPrimaryConfig().getAbsoluteLogPath() + "/tests/test_20181016172050.log")));
    }

    @Suite(component = TestComponent.class, service = TestService.class, interfaze = TestInterface.class, task = "task")
    public static class BuggyTestsClass {

        @org.testng.annotations.Test(description = "test_20181016172050 description")
        @Details
        public void test_20181016172050() {}

    }

    public static class TestBuggyLog extends BuggyLog {

        public TestBuggyLog() {
            super(UNIT_TEST_LOGGER, UNIT_TEST_LOGGER, UNIT_TEST_LOGGER);
        }

    }

}
