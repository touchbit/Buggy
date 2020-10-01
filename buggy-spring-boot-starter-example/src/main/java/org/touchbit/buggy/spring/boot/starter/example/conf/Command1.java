package org.touchbit.buggy.spring.boot.starter.example.conf;

import com.beust.jcommander.Parameter;
import org.touchbit.buggy.spring.boot.starter.conf.JCConfiguration;

public class Command1 implements JCConfiguration {

    @Parameter(names = {"--f"}, description = "Running all tests, including those that fall.")
    private static Boolean force =null;

    @Parameter(names = {"-a"}, description = "FFFFFFFFFFFFFFFFFFFFF.")
    private static Boolean a = null;
}
