package org.touchbit.buggy.spring.boot.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.testng.TestNG;

@SpringBootApplication
public abstract class SBBuggy implements ApplicationRunner {

    @Autowired
    private TestNG testNG;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Hello World from Application Runner");
    }

}
