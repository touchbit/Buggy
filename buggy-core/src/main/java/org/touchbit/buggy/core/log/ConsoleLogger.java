package org.touchbit.buggy.core.log;

public class ConsoleLogger extends BaseLogbackWrapper {

    public ConsoleLogger() {
        super(CONSOLE_LOGGER_NAME);
    }

}
