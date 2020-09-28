package org.touchbit.buggy.spring.boot.starter.example;

import com.beust.jcommander.Parameter;
import org.touchbit.buggy.spring.boot.starter.IBuggyConfig;

public class Command1 implements IBuggyConfig {

    @Parameter(names = {"--f"}, description = "Running all tests, including those that fall.")
    private Boolean force =null;

    @Parameter(names = {"-a"}, description = "FFFFFFFFFFFFFFFFFFFFF.")
    private Boolean a = null;
}
