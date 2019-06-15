package org.touchbit.buggy.core.test;

import static org.touchbit.buggy.core.test.TRProperty.RUN_ID;

/**
 * Created by Oleg Shaburov on 12.01.2019
 * shaburov.o.a@gmail.com
 */
public interface TestRailTest {

    long getRunId();

    default void setRunId() {
        if (getRunId() > 0) {
            System.setProperty(RUN_ID.toString(), String.valueOf(getRunId()));
        }
    }

}
