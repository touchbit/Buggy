package org.touchbit.buggy.core.helpful;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.slf4j.Log4jLogger;
import org.slf4j.helpers.SubstituteLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.logging.log4j.Level.ALL;

/**
 * Created by Oleg Shaburov on 16.10.2018
 * shaburov.o.a@gmail.com
 */
public class UnitTestLogger extends SubstituteLogger {

    private List<String> loggedMessages = Collections.synchronizedList(new ArrayList<>());

    public UnitTestLogger() {
        super("UnitTestLogger", null, false);
        setDelegate(new Log4jLogger(new UnitTestLog4jLogger(), "UnitTestLog4jLogger"));
    }

    public List<String> takeLoggedMessages() {
        List<String> tmp = new ArrayList<>(loggedMessages);
        loggedMessages.clear();
        return tmp;
    }

    public void clear() {
        loggedMessages.clear();
    }

    private class UnitTestLog4jLogger extends AbstractLogger {

        @Override
        public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
            loggedMessages.add(message.getFormattedMessage());
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable t) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
            return true;
        }

        @Override
        public Level getLevel() {
            return ALL;
        }
    }

}
