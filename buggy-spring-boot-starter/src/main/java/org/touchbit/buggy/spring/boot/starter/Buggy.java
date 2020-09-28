package org.touchbit.buggy.spring.boot.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.testng.TestNG;

import java.lang.reflect.Field;
import java.util.List;

@SpringBootApplication
public abstract class Buggy implements CommandLineRunner {

    @Autowired
    private TestNG testNG;

    @Override
    public void run(final String... args) throws Exception {
        System.out.println(" >>>> RUN");
    }

}
