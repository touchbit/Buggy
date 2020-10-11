package org.touchbit.buggy.spring.boot.starter.example;

import org.springframework.boot.SpringApplication;
import org.touchbit.buggy.core.goal.interfaze.API;
import org.touchbit.buggy.spring.boot.starter.BuggyRunner;
import org.touchbit.buggy.spring.boot.starter.jcommander.BuggyConfiguration;

import static org.touchbit.buggy.core.model.Type.REGRESSION;

public class ExampleBuggyRunner extends BuggyRunner {

    public static void main(String[] args) {
        BuggyConfiguration.setForce(true);
        BuggyConfiguration.setInterfaces(API.class);
        BuggyConfiguration.setTypes(REGRESSION);
        BuggyConfiguration.setIssuesUrl("https://jira.com/issues/");
        SpringApplication.run(ExampleBuggyRunner.class, args);
    }

}
