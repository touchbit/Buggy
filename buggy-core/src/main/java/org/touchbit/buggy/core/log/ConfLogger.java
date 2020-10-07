package org.touchbit.buggy.core.log;

import org.touchbit.buggy.core.utils.ANSI;

import java.util.function.BooleanSupplier;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static org.touchbit.buggy.core.utils.ANSI.BOLD;
import static org.touchbit.buggy.core.utils.ANSI.RED;

/**
 * Utility class for logging configuration events in TestNG format
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
public class ConfLogger {

    public static final int CONF_STRING_LEN = 47;
    public static final String CONF_BLOCK = "=";
    public static final String CONF_STEP = "\u2014";
    public static final String DOT = ".";
    public static final String UNDERSCORE = "_";

    private static final ConsoleLogger CONSOLE = new ConsoleLogger();

    private ConfLogger() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }

    public static void stepDelimiter() {
        info(filler(CONF_STEP));
    }

    public static void blockDelimiter() {
        info(filler(CONF_BLOCK));
    }

    public static void dotPlaceholder(Object prefix, Object postfix, ANSI ansi, boolean isWrapCondition) {
        String msg = filler(prefix, DOT, postfix);
        if (isWrapCondition) {
            msg = ansi.wrap(msg);
        }
        info(msg);
    }

    public static void dotPlaceholder(Object prefix, Object postfix) {
        info(filler(prefix, DOT, postfix));
    }

    public static void underscorePlaceholder(Object prefix, Object postfix) {
        info(filler(prefix, UNDERSCORE, postfix));
    }

    public static String filler(String symbol) {
        return filler(null, symbol, CONF_STRING_LEN, null);
    }

    public static String filler(Object prefix, String symbol) {
        return filler(prefix, symbol, CONF_STRING_LEN, null);
    }

    public static String filler(Object prefix, String symbol, Object postfix) {
        return filler(prefix, symbol, CONF_STRING_LEN, postfix);
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

    public static void centerBold(String msg) {
        info(BOLD.wrap(centerMsg(msg)));
    }

    public static void center(String msg) {
        info(centerMsg(msg));
    }

    private static String centerMsg(String msg) {
        if (msg != null && !msg.isEmpty()) {
            int diff = CONF_STRING_LEN - msg.length();
            if (diff > 0) {
                int indent = diff / 2;
                return filler("", " ", indent, "") + msg;
            }
        }
        return msg;
    }

    public static void info(String msg) {
        CONSOLE.info(msg);
    }

    public static void error(String msg, Throwable t) {
        CONSOLE.error(msg, t);
    }

    public static void error(String msg) {
        CONSOLE.error(msg);
    }

}
