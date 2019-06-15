package org.touchbit.buggy.example.min.listeners;

import org.touchbit.buggy.testrail.listeners.DefaultTestRailListener;

/**
 * Created by Oleg Shaburov on 12.01.2019
 * shaburov.o.a@gmail.com
 */
public class TestRailListener extends DefaultTestRailListener {

    public TestRailListener() {
        super(new StatusMap());
    }

}
