package org.touchbit.buggy.core.common;

import mockit.Expectations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.ExitHandler;
import org.touchbit.buggy.core.indirect.SuppressException;

/**
 * Created by Oleg Shaburov on 16.10.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("ExitHandler interface tests")
class ExitHandlerTests {

    @Test
    @DisplayName("Check exitRun(int status)")
    void unitTest_20181016214800() {
        suppressSystemExit(0);
        TestExitHandler handler = new TestExitHandler();
        handler.exitRun(0);
    }

    @Test
    @DisplayName("Check exitRun(int status, String msg)")
    void unitTest_20181016215145() {
        suppressSystemExit(0);
        TestExitHandler handler = new TestExitHandler();
        handler.exitRun(0, "");
    }

    @Test
    @DisplayName("Check exitRun(int status, String msg, Throwable t)")
    void unitTest_20181016215216() {
        suppressSystemExit(0);
        TestExitHandler handler = new TestExitHandler();
        handler.exitRun(0, "unitTest_20181016215258", new Exception("unitTest_20181016215258"));
    }

    @Test
    @DisplayName("Check exitRunWithUsage(int status)")
    void unitTest_20181016215242() {
        suppressSystemExit(0);
        TestExitHandler handler = new TestExitHandler();
        handler.exitRunWithUsage(0);
    }

    @Test
    @DisplayName("Check exitRunWithUsage(int status, String msg)")
    void unitTest_20181016215258() {
        suppressSystemExit(0);
        TestExitHandler handler = new TestExitHandler();
        handler.exitRunWithUsage(0, "");
    }

    private void suppressSystemExit(int status) {
        suppressSystemExit(status, false);
    }

    @SuppressWarnings("WeakerAccess")
    private void suppressSystemExit(int status, boolean withSuppressException) {
        new Expectations(System.class) {{
            System.exit(status);
            if (withSuppressException) {
                result = new SuppressException();
            }
        }};
    }

    private static class TestExitHandler implements ExitHandler {

        @Override
        public void exitRunWithUsage(int status, String msg) {
            exitRun(status, msg);
        }

    }

}
