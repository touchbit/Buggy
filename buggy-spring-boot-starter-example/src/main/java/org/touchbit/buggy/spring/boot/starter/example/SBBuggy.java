package org.touchbit.buggy.spring.boot.starter.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.touchbit.buggy.spring.boot.starter.Buggy;

public class SBBuggy extends Buggy {

    public static void main(String[] args) {
        args = new String[]{"--force", "-v"};
        SpringApplication.run(SBBuggy.class, args);
    }

}
