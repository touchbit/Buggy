package org.touchbit.buggy.spring.boot.starter;

import com.beust.jcommander.Parameter;
import org.atteo.classindex.IndexSubclasses;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

public class BuggyConfig implements IBuggyConfig {

    @Parameter(names = {"-f", "--force"}, description = "Running all tests, including those that fall.")
    public static Boolean force = false;

}
