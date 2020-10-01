package org.touchbit.buggy.spring.boot.starter.example;

import org.springframework.boot.SpringApplication;
import org.touchbit.buggy.spring.boot.starter.Buggy;
import org.touchbit.buggy.spring.boot.starter.conf.BuggyConfig;

public class SBBuggy extends Buggy {

    static {
        BuggyConfig.setForce(true);
    }

    public static void main(String[] args) {
//        args = new String[]{"--force", "-v"};
        SpringApplication.run(SBBuggy.class, args);
    }

}
