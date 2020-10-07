package org.touchbit.buggy.core.logback;

public class ConsoleLogger extends BaseLogbackWrapper {

    public ConsoleLogger() {
        super(CONSOLE_LOGGER_NAME);
    }

}
