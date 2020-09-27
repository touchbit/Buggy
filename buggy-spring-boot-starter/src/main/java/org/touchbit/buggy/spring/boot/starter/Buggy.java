package org.touchbit.buggy.spring.boot.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.testng.TestNG;

@SpringBootApplication
public abstract class Buggy implements CommandLineRunner {

    @Autowired
    private TestNG testNG;

    @Override
    public void run(final String... args) throws Exception {
        System.out.println(" >>>> RUN");
    }

}
