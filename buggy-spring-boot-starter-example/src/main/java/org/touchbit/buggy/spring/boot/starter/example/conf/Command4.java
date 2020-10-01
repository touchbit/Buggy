package org.touchbit.buggy.spring.boot.starter.example.conf;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.touchbit.buggy.spring.boot.starter.conf.JCConfiguration;

@Parameters(commandNames = "c2")
public class Command4 implements JCConfiguration {

    @Parameter(names = {"---?"}, hidden = true, help = true, description = "Print usage.")
    private static Boolean help = null;

    private static Boolean a = null;

//    @Parameter(names = {"-a"}, description = "FFFFFFFFFFFFFFFFFFFFF.")
    public static Boolean getA() {
        return a;
    }

    @Parameter(names = {"-a"}, description = "FFFFFFFFFFFFFFFFFFFFF.")
    public static void setA(Boolean a) {
        Command4.a = a;
    }

}
