package org.touchbit.buggy.core.logback.layout;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

import static ch.qos.logback.classic.Level.*;
import static org.touchbit.buggy.core.utils.ANSI.*;

/**
 * Console logger layout for dividing logging levels by color.
 * Used to color-code the status of completed tests and framework events.
 * ERROR - red
 * WARN - purple
 * INFO - none
 * DEBUG - green
 * TRACE - blue
 * <p>
 * Created by Oleg Shaburov on 30.09.2020
 * shaburov.o.a@gmail.com
 */
public class ConsoleLoggerColorLayout<E extends ILoggingEvent> extends LayoutBase<E> {

    /**
     * Log format: message\n
     */
    @Override
    public String doLayout(final ILoggingEvent event) {
        final StringBuilder stringBuilder = new StringBuilder(128);
        final String msg = event.getFormattedMessage();
        switch (event.getLevel().levelInt) {
            case ERROR_INT:
                stringBuilder.append(RED.wrap(msg));
                break;
            case WARN_INT:
                stringBuilder.append(PURPLE.wrap(msg));
                break;
            case INFO_INT:
                stringBuilder.append(msg);
                break;
            case DEBUG_INT:
                stringBuilder.append(GREEN.wrap(msg));
                break;
            case TRACE_INT:
                stringBuilder.append(BLUE.wrap(msg));
                break;
            default:
                return "";
        }
        stringBuilder.append(CoreConstants.LINE_SEPARATOR);
        return stringBuilder.toString();
    }
}
