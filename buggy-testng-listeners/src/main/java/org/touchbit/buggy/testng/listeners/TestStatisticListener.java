package org.touchbit.buggy.testng.listeners;

import org.touchbit.buggy.core.testng.TestStatisticListenerBase;

public class TestStatisticListener extends TestStatisticListenerBase {

    @Override
    public boolean isEnable() {
        return !ListenerConfiguration.isDisableTestStatisticListener();
    }

}
