package org.touchbit.buggy.testng.listeners;

import com.beust.jcommander.Parameter;
import org.touchbit.buggy.core.config.JCommand;

public class ListenerConfiguration implements JCommand {

    @Parameter(names = {"--disable-execution-listener"}, hidden = true, description = "Disable ExecutionListener.")
    private static Boolean disableExecutionListener = false;

    @Parameter(names = {"--disable-logging-listener"}, hidden = true, description = "Disable LoggingListener.")
    private static Boolean disableLoggingListener = false;

    @Parameter(names = {"--disable-statistic-listener"}, hidden = true, description = "Disable TestStatisticListener.")
    private static Boolean disableTestStatisticListener = false;

    public static Boolean isDisableExecutionListener() {
        return disableExecutionListener;
    }

    public static void setDisableExecutionListener(Boolean disableExecutionListener) {
        ListenerConfiguration.disableExecutionListener = disableExecutionListener;
    }

    public static Boolean isDisableLoggingListener() {
        return disableLoggingListener;
    }

    public static void setDisableLoggingListener(Boolean disableLoggingListener) {
        ListenerConfiguration.disableLoggingListener = disableLoggingListener;
    }

    public static Boolean isDisableTestStatisticListener() {
        return disableTestStatisticListener;
    }

    public static void setDisableTestStatisticListener(Boolean disableTestStatisticListener) {
        ListenerConfiguration.disableTestStatisticListener = disableTestStatisticListener;
    }
}
