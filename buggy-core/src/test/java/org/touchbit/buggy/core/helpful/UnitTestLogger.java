package org.touchbit.buggy.core.helpful;

import org.slf4j.helpers.SubstituteLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Oleg Shaburov on 16.10.2018
 * shaburov.o.a@gmail.com
 */
public class UnitTestLogger extends SubstituteLogger {

    private boolean isDebug = true;
    private boolean isError = true;
    private boolean isTrace = true;
    private boolean isWarn = true;
    private boolean isInfo = true;

    private List<String> loggedMessages = Collections.synchronizedList(new ArrayList<>());
    private List<String> loggedMessagesWithLevel = Collections.synchronizedList(new ArrayList<>());

    public UnitTestLogger() {
        super("UnitTestLogger", null, false);
//        setDelegate(new Log4jLogger(new UnitTestLog4jLogger(), "UnitTestLog4jLogger"));
    }

    public List<String> takeLoggedMessages() {
        List<String> tmp = new ArrayList<>(loggedMessages);
        loggedMessages.clear();
        return tmp;
    }

    public List<String> takeLoggedMessagesWithLevel() {
        List<String> tmp = new ArrayList<>(loggedMessagesWithLevel);
        loggedMessagesWithLevel.clear();
        return tmp;
    }

    public void reset() {
        this.loggedMessages.clear();
        this.loggedMessagesWithLevel.clear();
        this.isDebug = true;
        this.isError = true;
        this.isTrace = true;
        this.isWarn = true;
        this.isInfo = true;
    }

    @Override
    public boolean isDebugEnabled() {
        return this.isDebug;
    }

    public UnitTestLogger whenDebugEnabled(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    @Override
    public boolean isTraceEnabled() {
        return isTrace;
    }

    public UnitTestLogger whenTraceEnabled(boolean isTrace) {
        this.isTrace = isTrace;
        return this;
    }

    @Override
    public boolean isInfoEnabled() {
        return isInfo;
    }

    public UnitTestLogger whenInfoEnabled(boolean isInfo) {
        this.isInfo = isInfo;
        return this;
    }

    @Override
    public boolean isWarnEnabled() {
        return isWarn;
    }

    public UnitTestLogger whenWarnEnabled(boolean isWarn) {
        this.isWarn = isWarn;
        return this;
    }

    @Override
    public boolean isErrorEnabled() {
        return this.isError;
    }

    public UnitTestLogger whenErrorEnabled(boolean isError) {
        this.isError = isError;
        return this;
    }

}
