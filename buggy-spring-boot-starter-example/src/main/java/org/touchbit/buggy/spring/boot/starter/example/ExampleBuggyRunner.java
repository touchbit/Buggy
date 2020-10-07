package org.touchbit.buggy.spring.boot.starter.example;

import org.springframework.boot.SpringApplication;
import org.touchbit.buggy.core.goal.component.DefaultComponent;
import org.touchbit.buggy.core.goal.interfaze.API;
import org.touchbit.buggy.spring.boot.starter.BuggyRunner;
import org.touchbit.buggy.spring.boot.starter.jcommander.BuggyJCommand;

public class ExampleBuggyRunner extends BuggyRunner {

    static {
        BuggyJCommand.setForce(true);
        BuggyJCommand.setInterfaces(API.class);
        BuggyJCommand.setPrintLog(true);
        BuggyJCommand.setPrintSuite(true);
        BuggyJCommand.setPrintCause(true);
    }

    public static void main(String[] args) {
        SpringApplication.run(ExampleBuggyRunner.class, args);
    }

}
