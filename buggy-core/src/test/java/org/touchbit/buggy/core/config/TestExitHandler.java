package org.touchbit.buggy.core.config;

import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.utils.StringUtils;

import static org.touchbit.buggy.core.utils.BuggyUtils.CONSOLE_DELIMITER;

/**
 * Created by Oleg Shaburov on 16.10.2018
 * shaburov.o.a@gmail.com
 */
public class TestExitHandler extends Buggy.DefaultBuggyProcessor.DefaultBuggySystemExitHandler {

    private Throwable throwable = null;
    private Integer status = null;
    private String msg = null;

//    @Override
//    public void exitRunWithUsage(int status, String msg) {
//        exitRun(status, msg);
//    }

    public void realExitRunWithUsage(int status, String msg) {
        super.exitRunWithUsage(status, msg);
    }

//    @Override
//    public void exitRunWithUsage(int status) {
//        exitRun(status, null);
//    }

    @Override
    public void exitRun(int status) {
        exitRun(status, null, null);
    }

    @Override
    public void exitRun(int status, String msg) {
        exitRun(status, msg, null);
    }

    @Override
    public void exitRun(int status, String msg, Throwable t) {
        this.throwable = t;
        this.status = status;
        this.msg = msg;
    }

    @Override
    public void exit(int status) {
        this.status = status;
    }

    public void clean() {
        throwable = null;
        status = null;
        msg = null;
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