package org.touchbit.buggy.spring.boot.starter;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.testng.TestNG;
import org.touchbit.buggy.core.config.SecondaryConfig;
import org.touchbit.buggy.core.utils.BuggyUtils;

import java.util.ArrayList;
import java.util.List;


public class SBBuggy {

    private static int javaVersion = getVersion();

    public static void main(String[] args) {
//        args = new String[]{"-?"};
        JCommander jc = new JCommander();
//        jc.addObject();
        List<Class<? extends IBuggyConfig>> instantiatedSubclasses = BuggyUtils
                .findInstantiatedSubclasses(IBuggyConfig.class);
        List<IBuggyConfig> configs = new ArrayList<>();
        for (Class<? extends IBuggyConfig> instantiatedSubclass : instantiatedSubclasses) {
            try {
                configs.add(instantiatedSubclass.newInstance());
                JCommander tempJc = new JCommander();
                for (IBuggyConfig config : configs) {
                    if (config.getClass().isAnnotationPresent(Parameters.class)) {
                        tempJc.addCommand(config);
                    } else {
                        tempJc.addObject(config);
                    }
                }
                try {
                    tempJc.parse(args);
                    if (javaVersion > 8) {
                        System.out.println(instantiatedSubclass.getSimpleName() + "......OK");
                    } else {
                        System.out.println(instantiatedSubclass.getClass().getSimpleName() + "......OK");
                    }
                } catch (ParameterException e) {
                    if (javaVersion > 8) {
                        System.out.println(instantiatedSubclass.getSimpleName() + "....FAIL");
                    } else {
                        System.out.println(instantiatedSubclass.getClass().getSimpleName() + "....FAIL");
                    }
                    e.usage();
                    throw (ParameterException) e.fillInStackTrace();
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        jc.setProgramName("name");
        jc.parse(args);
        jc.usage();
    }

    private static int getVersion() {
        String version = System.getProperty("java.version");
        System.out.println(" >>>> " + version);
        if(version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if(dot != -1) { version = version.substring(0, dot); }
        } return Integer.parseInt(version);
    }
}
