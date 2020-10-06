package org.touchbit.buggy.core.log.layout;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.util.CachingDateFormatter;
import org.touchbit.buggy.core.utils.ANSI;

import java.util.Date;

/**
 * File logger layout for framework events (without ANSI colors)
 * <p>
 * Created by Oleg Shaburov on 30.09.2020
 * shaburov.o.a@gmail.com
 */
public class FrameworkLoggerLayout extends LayoutBase<ILoggingEvent> {

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
     * Log format: 17:28:15.142 INFO - message\n
     */
    @Override
    public String doLayout(final ILoggingEvent event) {
        final long timestamp = event.getTimeStamp();
        final String message = event.getFormattedMessage();
        final String finalMessage = ANSI.unwrap(message);
        final String time = TIME_FORMATTER.format(timestamp);
        final Level level = event.getLevel();
        final IThrowableProxy throwableProxy = event.getThrowableProxy();
        String tMsg = "";
        if (throwableProxy != null) {
            tMsg = CoreConstants.LINE_SEPARATOR + ThrowableProxyUtil.asString(throwableProxy);
        }
        return time + " " + level + " - " + finalMessage + tMsg + CoreConstants.LINE_SEPARATOR;
    }

}
