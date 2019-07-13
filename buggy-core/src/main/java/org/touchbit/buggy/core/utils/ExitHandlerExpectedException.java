package org.touchbit.buggy.core.utils;

public class ExitHandlerExpectedException extends RuntimeException {

    public ExitHandlerExpectedException() {
        super("ExitHandler.exitRun() call expected.");
    }

}
