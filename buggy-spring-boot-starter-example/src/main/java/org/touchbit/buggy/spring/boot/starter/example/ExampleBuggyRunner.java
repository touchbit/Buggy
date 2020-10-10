package org.touchbit.buggy.spring.boot.starter.example;

import org.springframework.boot.SpringApplication;
import org.touchbit.buggy.core.goal.interfaze.API;
import org.touchbit.buggy.spring.boot.starter.BuggyRunner;
import org.touchbit.buggy.spring.boot.starter.jcommander.BuggyConfiguration;

public class ExampleBuggyRunner extends BuggyRunner {

    public static void main(String[] args) {
        BuggyConfiguration.setForce(true);
        BuggyConfiguration.setPrintLog(true);
        BuggyConfiguration.setPrintSuite(true);
        BuggyConfiguration.setPrintCause(true);
        BuggyConfiguration.setInterfaces(API.class);
        BuggyConfiguration.setTaskTrackerIssueUrl("https://jira.com/issues/");

        SpringApplication.run(ExampleBuggyRunner.class, args);
    }

}
