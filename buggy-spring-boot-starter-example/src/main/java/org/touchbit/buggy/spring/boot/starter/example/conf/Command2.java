package org.touchbit.buggy.spring.boot.starter.example.conf;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.touchbit.buggy.spring.boot.starter.conf.JCConfiguration;

@Parameters()
public class Command2 implements JCConfiguration {

    @Parameter(names = {"-?", "--help"}, hidden = true, help = true, description = "Print usage.")
    private static Boolean help = null;

    @Parameter(names = {"-----f"}, description = "FFFFFFFFFFFFFFFFFFFFF.")
    private static Boolean a = null;

}
