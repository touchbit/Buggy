package org.touchbit.buggy.spring.boot.starter.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;

import static java.util.Objects.isNull;
import static org.touchbit.buggy.spring.boot.starter.log.ANSI.BOLD;
import static org.touchbit.buggy.spring.boot.starter.log.ANSI.RED;

/**
 * Utility class for logging configuration events in TestNG format
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
public class ConfigurationLogger {

    public static final int STRING_LEN = 47;
    public static final String BLOCK = "=";
    public static final String STEP = "\u2014";
    public static final String DOT = ".";

    private static final Logger CONSOLE = LoggerFactory.getLogger(ConfigurationLogger.class);

    private ConfigurationLogger() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }

    public static String underscoreFiller(Object prefix, int i) {
        return underscoreFiller(prefix, i, null);
    }

    public static String underscoreFiller(Object prefix, int i, Object suffix) {
        return filler(prefix, "_", i, suffix);
    }

    public static String dotFiller(Object prefix) {
        return dotFiller(prefix, STRING_LEN, null);
    }

    public static String dotFiller(Object prefix, int i, Object suffix) {
        return filler(prefix, DOT, i, suffix);
    }

    public static String dotFiller(Object prefix, Object suffix) {
        return filler(prefix, ".", STRING_LEN, suffix);
    }

    public static String filler(Object prefix, String symbol, int i) {
        return filler(prefix, symbol, i, null);
    }

    public static String filler(String symbol) {
        return filler(null, symbol, STRING_LEN, null);
    }

    public static String filler(Object prefix, String symbol) {
        return filler(prefix, symbol, STRING_LEN, null);
    }

    public static String filler(Object prefix, String symbol, Object postfix) {
        return filler(prefix, symbol, STRING_LEN, postfix);
    }

    public static String filler(Object prefix, String symbol, int length, Object postfix) {
        if (isNull(symbol) || symbol.isEmpty()) {
            symbol = ".";
        }
        if (isNull(prefix)) {
            prefix = "";
        }
        if (isNull(postfix)) {
            postfix = "";
        }
        StringBuilder sb = new StringBuilder();
        int msgLen = (prefix.toString() + postfix.toString()).length();
        sb.append(prefix);
        if (msgLen >= length) {
            sb.append(symbol).append(symbol).append(symbol);
        }
        if (msgLen < length) {
            for (int j = 0; j < (length - msgLen); j++) {
                sb.append(symbol);

            }
        }
        sb.append(postfix);
        return sb.toString();
    }

    public static void stepDelimeter() {
        print(filler(STEP));
    }

    public static void blockDelimeter() {
        print(filler(BLOCK));
    }

    public static void dotPlaceholder(Object prefix, Object postfix) {
        print(filler(prefix, DOT, postfix));
    }

    public static void fPrint(Object prefix, String symbol, Object postfix) {
        print(filler(prefix, symbol, postfix));
    }

    public static void centerBold(String msg) {
        print(BOLD.wrap(centerMsg(msg)));
    }

    public static void center(String msg) {
        print(centerMsg(msg));
    }

    private static String centerMsg(String msg) {
        if (msg != null && !msg.isEmpty()) {
            int diff = STRING_LEN - msg.length();
            if (diff > 0) {
                int indent = diff / 2;
                return filler("", " ", indent, "") + msg;
            }
        }
        return msg;
    }

    public static void print(String msg) {
        CONSOLE.info(msg);
    }

    public static void errPrint(String msg, Exception t) {
        String breakMsg = lineBreak(msg);
        CONSOLE.error(RED.wrap(breakMsg), t);
    }

    public static void errPrint(String msg) {
        String breakMsg = lineBreak(msg);
        CONSOLE.error(RED.wrap(breakMsg));
    }

    public static String lineBreak(String msg) {
        StringJoiner sj = new StringJoiner("\n");
        StringBuilder line = new StringBuilder();
        for (String s : msg.split(" ")) {
            String tmp = line + " " + s;
            if (tmp.length() < 47) {
                line.append(s).append(" ");
            }
            if (tmp.length() == 47) {
                sj.add(tmp);
                line = new StringBuilder();
            }
            if (tmp.length() > 47) {
                if (line.length() == 0) {
                    sj.add(s);
                } else {
                    sj.add(line.toString());
                    line = new StringBuilder(s + " ");
                }
            }
        }
        sj.add(line);
        return sj.toString();
    }

}
