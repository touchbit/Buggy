package org.touchbit.buggy.spring.boot.starter.log;

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

    public static String unwrap(String msg) {
        for (ANSI value : ANSI.values()) {
            msg = msg.replace(value.symbol, "");
        }
        return msg;
    }

    public String wrap(String msg) {
        return this.symbol + msg + RESET.symbol;
    }

}
