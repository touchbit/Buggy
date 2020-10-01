package org.touchbit.buggy.spring.boot.starter.example.conf;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.touchbit.buggy.spring.boot.starter.conf.JCConfiguration;

@Parameters(commandNames = {"c1"}, commandDescription = "secondary config")
public class Command3 implements JCConfiguration {

    @Parameter(names = {"----?"}, hidden = true, help = true, description = "Print usage.")
    private static Boolean help = null;

    @Parameter(names = {"-a"}, description = "FFFFFFFFFFFFFFFFFFFFF.")
    private static Boolean a = null;

}
