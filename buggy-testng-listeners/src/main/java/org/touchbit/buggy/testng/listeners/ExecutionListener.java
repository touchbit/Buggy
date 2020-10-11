package org.touchbit.buggy.testng.listeners;

import org.touchbit.buggy.core.testng.ExecutionListenerBase;

public class ExecutionListener extends ExecutionListenerBase {

    @Override
    public boolean isEnable() {
        return !ListenerConfiguration.isDisableExecutionListener();
    }

}
