package org.touchbit.buggy.spring.boot.starter.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

import static ch.qos.logback.classic.Level.*;
import static org.touchbit.buggy.spring.boot.starter.log.ANSI.*;

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
public class ConsoleLoggerColorLayout extends LayoutBase<ILoggingEvent> {

    /**
     * Log format: message\n
     */
    @Override
    public String doLayout(ILoggingEvent event) {
        StringBuilder stringBuilder = new StringBuilder(128);
        String msg = event.getFormattedMessage();
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
