package org.touchbit.buggy.spring.boot.starter.example.conf;

import com.beust.jcommander.Parameter;
import org.touchbit.buggy.core.config.JCConfiguration;

public class ExampleConfiguration implements JCConfiguration {

    @Parameter(names = {"--example-host"}, description = "Example host.")
//    @Parameter(names = {"-f"}, description = "Example host.")
    private static String exampleHost = "https://exmaole.com";

    @Parameter(names = {"--example-pass"}, password = true, description = "Example password.")
    private static String examplePass = "pass";

    public static String getExampleHost() {
        return exampleHost;
    }

    public static void setExampleHost(String exampleHost) {
        ExampleConfiguration.exampleHost = exampleHost;
    }

    public static String getExamplePass() {
        return examplePass;
    }

    public static void setExamplePass(String examplePass) {
        ExampleConfiguration.examplePass = examplePass;
    }

}
