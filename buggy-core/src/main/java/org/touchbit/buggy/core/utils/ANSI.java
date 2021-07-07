package org.touchbit.buggy.core.utils;

public enum ANSI {

    RESET("\u001B[0m"),
    BOLD("\u001B[1m"),
    RED("\u001B[31m"),
    BR_RED("\u001B[31;1m"),
    GREEN("\u001B[32m"),
    BR_GREEN("\u001B[32;1m"),
    YELLOW("\u001b[33m"),
    BR_YELLOW("\u001b[33;1m"),
    BLUE("\u001B[34m"),
    BR_BLUE("\u001B[34;1m"),
    PURPLE("\u001B[35m"),
    BR_PURPLE("\u001B[35;1m"),
    NONE(""),
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
        if (this.equals(NONE)) {
            return msg;
        }
        return this.symbol + msg + RESET.symbol;
    }

}
