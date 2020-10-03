package org.touchbit.buggy.spring.boot.starter.example;

import org.springframework.boot.SpringApplication;
import org.touchbit.buggy.spring.boot.starter.BuggyRunnerBase;
import org.touchbit.buggy.spring.boot.starter.conf.BuggyConfig;

public class ExampleBuggyRunner extends BuggyRunnerBase {

    static {
        BuggyConfig.setForce(true);
    }

    public static void main(String[] args) {
        SpringApplication.run(ExampleBuggyRunner.class, args);
    }

}
