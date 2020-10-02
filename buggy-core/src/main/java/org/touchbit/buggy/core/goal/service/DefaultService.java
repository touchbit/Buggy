package org.touchbit.buggy.core.goal.service;

public final class DefaultService extends Service {

    @Override
    public String getName() {
        return "DEFAULT";
    }

    @Override
    public String getDescription() {
        return "Default service for test suites";
    }

}
