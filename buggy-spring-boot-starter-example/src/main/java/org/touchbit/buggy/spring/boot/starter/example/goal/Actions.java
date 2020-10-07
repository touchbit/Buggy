package org.touchbit.buggy.spring.boot.starter.example.goal;

import org.touchbit.buggy.core.goal.service.Service;

public class Actions extends Service {

    @Override
    public String getDescription() {
        return "Github CI/CD actions service.";
    }

}
