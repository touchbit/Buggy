/*
 * Copyright © 2018 Shaburov Oleg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.touchbit.buggy.core.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.isNull;

/**
 * Created by Oleg Shaburov on 08.09.2018
 * shaburov.o.a@gmail.com
 */
public class StringUtils {

    public static final int STRING_LEN = 47;
    public static final String BLOCK = "=";
    public static final String STEP = "\u2014";
    public static final String DOT = ".";

    public static String underscoreFiller(Object prefix, int i) {
        return underscoreFiller(prefix, i, null);
    }

    public static String underscoreFiller(Object prefix, int i, Object suffix) {
        return filler(prefix, "_", i, suffix);
    }

    public static String dotFiller(Object prefix, int i) {
        return dotFiller(prefix, i, null);
    }

    public static String dotFiller(Object prefix) {
        return dotFiller(prefix, STRING_LEN, null);
    }

    public static String dotFiller(Object prefix, int i, Object suffix) {
        return filler(prefix, ".", i, suffix);
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

    /**
     * Converts a string to an encoded URL format (using the UTF-8 encoding scheme).
     *
     * @param value string to be converted
     * @return encoded URL string
     */
    public static String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (Exception ignore) {
            return value;
        }
    }

    /**
     * Converts a string encoded in the URL format into its original representation (using UTF-8 encoding scheme).
     *
     * @param value coded string
     * @return original string representation
     */
    public static String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
        } catch (Exception ignore) {
            return value;
        }
    }

    public static void sPrint() {
        println(filler(STEP));
    }

    public static void bPrint() {
        println(filler(BLOCK));
    }

    public static void fPrint(String symbol) {
        println(filler(symbol));
    }

    public static void fPrint(Object prefix, String symbol, Object postfix) {
        println(filler(prefix, symbol, postfix));
    }

    public static void cPrint(String msg) {
        if (msg != null && !msg.isEmpty()) {
            int diff = STRING_LEN - msg.length();
            if (diff > 0) {
                int indent = diff / 2;
                String result = filler("", " ", indent, "") + msg;
                println(result);
            }
        }
    }

    public static void println(String msg) {
        println(msg, null);
    }

    public static void println(String msg, Throwable t) {
        if (msg != null) {
            System.out.println(msg);
        }
        if (t != null) {
            t.printStackTrace();
        }
    }

    private StringUtils() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }
}
