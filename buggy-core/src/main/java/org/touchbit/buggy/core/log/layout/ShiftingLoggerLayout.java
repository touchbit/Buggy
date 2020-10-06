package org.touchbit.buggy.core.log.layout;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.util.CachingDateFormatter;

import java.util.Date;

/**
 * Shofting logger layout for framework events
 * <p>
 * Created by Oleg Shaburov on 30.09.2020
 * shaburov.o.a@gmail.com
 */
public class ShiftingLoggerLayout extends LayoutBase<ILoggingEvent> {

    private static final CachingDateFormatter TIME_FORMATTER = new CachingDateFormatter("HH:mm:ss.SSS");
    private static final CachingDateFormatter DATE_FORMATTER = new CachingDateFormatter("dd:MM:YYYY");

    /**
     * Print welcome message in the format: 17:28:14.985 INFO - Launch date: 30:09:2020
     */
    @Override
    public String getPresentationHeader() {
        return getPresentation();
    }

    @Override
    public String getPresentationFooter() {
        return getPresentation();
    }

    public String getPresentation() {
        final long timestamp = new Date().getTime();
        final String time = TIME_FORMATTER.format(timestamp);
        final String date = DATE_FORMATTER.format(timestamp);
        return time + " INFO - Launch date: " + date;
    }

    /**
     * Log format: %d{HH:mm:ss:SSS} %-5level - %msg%n%rEx
     * Example: 19:37:00.595 ERROR - Could not find beans of class JCConfiguration in package [null]
     * java.lang.IllegalArgumentException: 'value' must not be null
     * at org.springframework.util.Assert.notNull(Assert.java:201)
     */
    @Override
    public String doLayout(final ILoggingEvent event) {
        final long timestamp = event.getTimeStamp();
        final String message = event.getFormattedMessage();
        final IThrowableProxy tProxy = event.getThrowableProxy();
        return TIME_FORMATTER.format(timestamp) + " " + event.getLevel() + " - " + message +
                (tProxy != null ? CoreConstants.LINE_SEPARATOR + ThrowableProxyUtil.asString(tProxy) : "") +
                CoreConstants.LINE_SEPARATOR;
    }

}
