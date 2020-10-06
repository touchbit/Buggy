package org.touchbit.buggy.core.utils;

public enum ANSI {

    RESET("\u001B[0m"),
    BOLD("\u001B[1m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    ;

    String symbol;

    ANSI(String symbol) {
        this.symbol = symbol;
    }

    public static String unwrap(final String msg) {
        String resultMsg = msg;
        for (ANSI value : ANSI.values()) {
            resultMsg = resultMsg.replace(value.symbol, "");
        }
        return resultMsg;
    }

    public String wrap(final String msg) {
        return this.symbol + msg + RESET.symbol;
    }

}
