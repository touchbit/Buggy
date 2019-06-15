package org.touchbit.buggy.core.exceptions;

import org.touchbit.buggy.core.model.Details;

import java.util.Arrays;

/**
 * Created by Oleg Shaburov on 13.01.2019
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseBuggyTestException extends RuntimeException {

    public BaseBuggyTestException() {
        super();
    }

    public BaseBuggyTestException(String msg) {
        super(msg);
    }

    public BaseBuggyTestException(String msg, Throwable e) {
        super(msg, e);
    }

    public BaseBuggyTestException(Throwable e) {
        super(e);
    }

    protected static String getMsg(String brief, Details details) {
        StringBuilder sb = new StringBuilder();
        sb.append(brief);
        if (details != null) {
            String[] tmp;
            tmp = details.issue();
            if (tmp.length > 0) {
                sb.append(". Related issues: ").append(Arrays.toString(tmp));
            }
            tmp = details.bug();
            if (tmp.length > 0) {
                sb.append(". Related bugs: ").append(Arrays.toString(tmp));
            }
        }
        sb.append(".");
        return sb.toString();
    }

}
