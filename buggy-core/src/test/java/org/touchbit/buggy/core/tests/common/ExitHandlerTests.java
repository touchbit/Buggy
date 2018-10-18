package org.touchbit.buggy.core.tests.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.ExitHandler;
import org.touchbit.buggy.core.exceptions.AssertionException;
import org.touchbit.buggy.core.tests.BaseUnitTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by Oleg Shaburov on 16.10.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("ExitHandler interface tests")
class ExitHandlerTests extends BaseUnitTest {

    @Test
    @DisplayName("Check exitRun(int status)")
    void unitTest_20181016214800() {
        TestExitHandler handler = new TestExitHandler();
        handler.exitRun(0);
        assertThat(SYSTEM_OUT_LOGGER.takeLoggedMessages(), contains(
                "Exit code.....................................0",
                "==============================================="
        ));
    }

    @Test
    @DisplayName("Check exitRun(int status, String msg)")
    void unitTest_20181016215145() {
        TestExitHandler handler = new TestExitHandler();
        handler.exitRun(0, "msg");
        assertThat(SYSTEM_OUT_LOGGER.takeLoggedMessages(), contains(
                "msg",
                "===============================================",
                "Exit code.....................................0",
                "==============================================="
        ));
    }

    @Test
    @DisplayName("Check exitRun(int status, String msg, Throwable t)")
    void unitTest_20181016215216() {
        TestExitHandler handler = new TestExitHandler();
        handler.exitRun(1, "unitTest_20181016215258", new AssertionException("BigBadaBum"));
        assertThat(SYSTEM_OUT_LOGGER.takeLoggedMessages(), contains(
                "unitTest_20181016215258",
                "org.touchbit.buggy.core.exceptions.AssertionException: BigBadaBum",
                "===============================================",
                "Exit code.....................................1",
                "==============================================="
        ));
    }

    @Test
    @DisplayName("Check exitRunWithUsage(int status)")
    void unitTest_20181016215242() {
        TestExitHandler handler = new TestExitHandler();
        handler.exitRunWithUsage(0);
        assertThat(SYSTEM_OUT_LOGGER.takeLoggedMessages(), contains(
                "Exit code.....................................0",
                "==============================================="
        ));
    }

    @Test
    @DisplayName("Check exitRunWithUsage(int status, String msg)")
    void unitTest_20181016215258() {
        TestExitHandler handler = new TestExitHandler();
        handler.exitRunWithUsage(0, "msg");
        assertThat(SYSTEM_OUT_LOGGER.takeLoggedMessages(), contains(
                "msg",
                "===============================================",
                "Exit code.....................................0",
                "==============================================="
        ));
    }

    private static class TestExitHandler implements ExitHandler {

        @Override
        public void exitRunWithUsage(int status, String msg) {
            exitRun(status, msg);
        }

        @Override
        public void exit(int status) {
            // do nothing
        }

    }

}
