package org.touchbit.buggy.spring.boot.starter.example;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.touchbit.buggy.spring.boot.starter.IBuggyConfig;


public class Command2 implements IBuggyConfig {

    @Parameter(names = {"-?", "--help"}, hidden = true, help = true, description = "Print usage.")
    private Boolean help = null;

    @Parameter(names = {"-ad"}, description = "FFFFFFFFFFFFFFFFFFFFF.")
    private Boolean a = null;

}
