package org.touchbit.buggy.spring.boot.starter;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public abstract class BuggyRunnerBase implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Hello World from Application Runner");
    }

}
