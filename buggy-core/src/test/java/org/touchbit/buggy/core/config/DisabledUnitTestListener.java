package org.touchbit.buggy.core.config;

import org.touchbit.buggy.core.testng.BuggyListener;

/**
 * Created by Oleg Shaburov on 22.10.2018
 * shaburov.o.a@gmail.com
 */
public class DisabledUnitTestListener implements BuggyListener {

    @Override
    public boolean isEnable() {
        return false;
    }

}
