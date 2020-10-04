package org.touchbit.buggy.spring.boot.starter.example;

import org.springframework.boot.SpringApplication;
import org.touchbit.buggy.spring.boot.starter.BuggyRunner;
import org.touchbit.buggy.core.config.BuggyConfig;

public class ExampleBuggyRunner extends BuggyRunner {

    static {
        BuggyConfig.setForce(true);
        BuggyConfig.setVersion(true);
    }

    public static void main(String[] args) {
        SpringApplication.run(ExampleBuggyRunner.class, args);
    }

}
