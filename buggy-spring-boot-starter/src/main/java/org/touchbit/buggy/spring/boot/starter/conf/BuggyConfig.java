package org.touchbit.buggy.spring.boot.starter.conf;

import com.beust.jcommander.Parameter;
import org.springframework.core.annotation.Order;

/**
 * Configuration class for customizing Buggy.
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
@Order(1)
public class BuggyConfig implements JCConfiguration {

    @Parameter(names = {"-f", "--force"}, description = "Running all tests, including those that fall.")
    private static Boolean force = false;

    @Parameter(names = {"-v", "--verbose"}, hidden = true, description = "Print all configuration parameters.")
    private static Boolean verbose = false;

    private static String absoluteLogPath;

    public static Boolean getForce() {
        return force;
    }

    public static void setForce(boolean force) {
        BuggyConfig.force = force;
    }

    public static Boolean getVerbose() {
        return verbose;
    }

    public static void setVerbose(Boolean verbose) {
        BuggyConfig.verbose = verbose;
    }

    public static String getAbsoluteLogPath() {
        return absoluteLogPath;
    }

    public static void setAbsoluteLogPath(String absoluteLogPath) {
        BuggyConfig.absoluteLogPath = absoluteLogPath;
    }

}
