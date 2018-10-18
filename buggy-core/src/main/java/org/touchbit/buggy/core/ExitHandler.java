package org.touchbit.buggy.core;

import org.touchbit.buggy.core.utils.StringUtils;

import static org.touchbit.buggy.core.utils.BuggyUtils.CONSOLE_DELIMITER;

/**
 * Created by Oleg Shaburov on 16.10.2018
 * shaburov.o.a@gmail.com
 */
public interface ExitHandler {

    default void exitRunWithUsage(int status) {
        exitRunWithUsage(status, null);
    }

    void exitRunWithUsage(int status, String msg);

    default void exitRun(int status) {
        exitRun(status, null, null);
    }

    default void exitRun(int status, String msg) {
        exitRun(status, msg, null);
    }

    default void exitRun(int status, String msg, Throwable t) {
        if (msg !=null) {
            if (t != null) {
                StringUtils.println(msg, t);
            } else {
                StringUtils.println(msg);
            }
            StringUtils.println(CONSOLE_DELIMITER);
        }
        StringUtils.println(StringUtils.dotFiller("Exit code", 47, status));
        StringUtils.println(CONSOLE_DELIMITER);
        exit(status);
    }

    void exit(int status);

}
