package org.touchbit.buggy.spring.boot.starter.example;

import org.springframework.boot.SpringApplication;
import org.touchbit.buggy.spring.boot.starter.SBBuggy;
import org.touchbit.buggy.spring.boot.starter.conf.BuggyConfig;

public class ExampleBuggyImpl extends SBBuggy {

    static {
        BuggyConfig.setForce(true);
    }

    public static void main(String[] args) {
        SpringApplication.run(ExampleBuggyImpl.class, args);
    }

}
