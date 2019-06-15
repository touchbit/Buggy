package org.touchbit.buggy.core.config;

import org.touchbit.buggy.core.Buggy;

/**
 * Created by Oleg Shaburov on 16.10.2018
 * shaburov.o.a@gmail.com
 */
public class TestExitHandler extends Buggy.DefaultBuggyProcessor.DefaultBuggySystemExitHandler {

    private Throwable throwable = null;
    private Integer status = null;
    private String msg = null;

    private Boolean isWorked = false;

    public void realExitRunWithUsage(int status, String msg) {
        super.exitRunWithUsage(status, msg);
    }

    @Override
    public void exitRun(int status) {
        exitRun(status, null, null);
    }

    @Override
    public void exitRun(int status, String msg) {
        exitRun(status, msg, null);
    }

    @Override
    public synchronized void exitRun(int status, String msg, Throwable t) {
        if (isWorked) {
            return;
        }
        this.throwable = t;
        this.status = status;
        this.msg = msg;
        isWorked = true;
    }

    @Override
    public void exit(int status) {
        exitRun(status, null, null);
    }

    public void clean() {
        throwable = null;
        status = null;
        msg = null;
        isWorked = false;
    }

    public Throwable getThrowable() {
        Throwable throwable = this.throwable;
        this.throwable = null;
        return throwable;
    }

    public Integer getStatus() {
        Integer status = this.status;
        this.status = null;
        return status;
    }

    public String getMsg() {
        String msg = this.msg;
        this.msg = null;
        return msg;
    }

}