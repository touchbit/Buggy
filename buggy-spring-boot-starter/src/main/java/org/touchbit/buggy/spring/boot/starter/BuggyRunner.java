package org.touchbit.buggy.spring.boot.starter;

import org.slf4j.MDC;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.touchbit.buggy.core.utils.log.ConfigurationLogger;
import org.touchbit.buggy.core.utils.log.BuggyLoggers;

@SpringBootApplication
public class BuggyRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        ConfigurationLogger.blockDelimiter();
        System.out.println("Hello World from Buggy Runner");
        exit(0);
    }

    public static void exit(String message, Exception e) {
        if (e != null) {
            exit(1, message, e);
        } else {
            exit(0, message);
        }
    }

    public static void exit(int status) {
        exit(status, null, null);
    }

    public static void exit(int status, String message) {
        exit(status, message, null);
    }

    public static void exit(int status, String message, Exception e) {
        if (message != null) {
            ConfigurationLogger.blockDelimiter();
            String frameworkLogPath = "";
            if (status == 0) {
                ConfigurationLogger.info(message);
            } else {
                if (BuggyLoggers.F_LOG == null) {
                    MDC.put("print.console.stacktrace", "true");
                } else {
                    String frameworkLogFilePath = BuggyLoggers.getFrameworkLogFilePath();
                    if (frameworkLogFilePath != null && !frameworkLogFilePath.isEmpty()) {
                        frameworkLogPath = "\nFor more information see " + frameworkLogFilePath;
                    }
                }
                String eMsg = "\n" + e.getClass().getSimpleName() + ": " + e.getMessage();
                ConfigurationLogger.error(message + eMsg + frameworkLogPath, e);
            }
        }
        ConfigurationLogger.blockDelimiter();
        ConfigurationLogger.dotPlaceholder("Exit code", status);
        ConfigurationLogger.blockDelimiter();
        System.exit(status);
    }

}
