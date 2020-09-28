package org.touchbit.buggy.spring.boot.starter;

import com.beust.jcommander.Parameter;
import org.atteo.classindex.IndexSubclasses;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.touchbit.buggy.core.config.BParameters.ALL;

@Order(1)
public class BuggyConfig implements IBuggyConfig {

    @Parameter(names = {"-f", "--force"}, description = "Running all tests, including those that fall.")
    public static Boolean force = false;

    @Parameter(names = {"-v", "--verbose"}, hidden = true, description = "Print all configuration parameters.")
    public static Boolean verbose = false;

    private static String absoluteLogPath;

    public static void setAbsoluteLogPath(String path) {
        absoluteLogPath = path;
    }

    public static String getAbsoluteLogPath() {
        return absoluteLogPath;
    }
}
