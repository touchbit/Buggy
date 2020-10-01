package org.touchbit.buggy.spring.boot.starter.conf;

import org.touchbit.buggy.spring.boot.starter.log.ConfigurationLogger;

/**
 * Interface for Buggy spring boot configurations
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
public interface SBConfiguration {

    default void exitRunWithErr(Exception e) {
        exitRunWithErr(e.getMessage());
    }

    default void exitRunWithErr(String message) {
        exitRun(1, message);
    }

    default void exitRun(int status) {
        exitRun(status, null);
    }

    default void exitRun(int status, String message) {
        if (message != null) {
            ConfigurationLogger.blockDelimeter();
            if (status == 0) {
                ConfigurationLogger.print(message);
            } else {
                ConfigurationLogger.errPrint(message);
            }
        }
        ConfigurationLogger.blockDelimeter();
        ConfigurationLogger.dotPlaceholder("Exit code", status);
        ConfigurationLogger.blockDelimeter();
        System.exit(status);
    }

}
