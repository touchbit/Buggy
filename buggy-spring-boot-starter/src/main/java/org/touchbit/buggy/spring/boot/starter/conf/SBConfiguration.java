package org.touchbit.buggy.spring.boot.starter.conf;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.touchbit.buggy.spring.boot.starter.log.ConfigurationLogger;

import java.util.Iterator;

/**
 * Interface for Buggy spring boot configurations
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
public interface SBConfiguration {

    default void exitRunWithErr(String message) {
        exitRunWithErr(message, null);
    }

    default void exitRunWithErr(String message, Exception e) {
        exitRun(1, message, e);
    }

    default void exitRun(int status) {
        exitRun(status, null, null);
    }

    default void exitRun(int status, String message, Exception e) {
        if (message != null) {
            ConfigurationLogger.blockDelimeter();
            String frameworkLogPath = "";
            if (status == 0) {
                ConfigurationLogger.print(message);
            } else {
                if (SBLogbackConfiguration.framework() == null) {
                    MDC.put("print.console.stacktrace", "true");
                } else {
                    String frameworkLogFilePath = SBLogbackConfiguration.getFrameworkLogFilePath();
                    if (frameworkLogFilePath != null && !frameworkLogFilePath.isEmpty()) {
                        frameworkLogPath = "\nFor more information see " + frameworkLogFilePath;
                    }
                }
                String eMsg = "\n" + e.getClass().getSimpleName() + ": " + e.getMessage();
                ConfigurationLogger.errPrint(message + eMsg + frameworkLogPath, e);
            }
        }
        ConfigurationLogger.blockDelimeter();
        ConfigurationLogger.dotPlaceholder("Exit code", status);
        ConfigurationLogger.blockDelimeter();
        System.exit(status);
    }

}
