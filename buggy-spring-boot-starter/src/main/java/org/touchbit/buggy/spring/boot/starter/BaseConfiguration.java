package org.touchbit.buggy.spring.boot.starter;

import org.touchbit.buggy.core.utils.StringUtils;

public abstract class BaseConfiguration {

    public static void exitRun(Exception e) {
        exitRun(e.getMessage());
    }

    public static void exitRun(String message) {
        StringUtils.bPrint();
        StringUtils.println(message);
        StringUtils.bPrint();
        StringUtils.println(StringUtils.dotFiller("Exit code", "1"));
        StringUtils.bPrint();
        System.exit(1);
    }

}
