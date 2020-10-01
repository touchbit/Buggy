package org.touchbit.buggy.spring.boot.starter.conf;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.touchbit.buggy.spring.boot.starter.log.ConfigurationLogger;
import org.touchbit.buggy.spring.boot.starter.utils.JUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

/**
 * {@link JCommander} spring boot configuration class.
 * {@link ComponentScan} searches for all implementations of the {@link JCConfiguration} class.
 * The search is done for packages that contain a "buggy" package.
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
@Configuration
@ConditionalOnNotWebApplication
@ComponentScan(
        basePackages = "**.buggy",
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(type = ASSIGNABLE_TYPE, classes = JCConfiguration.class))
public class SBJCommanderConfiguration implements SBConfiguration {

    private final List<JCConfiguration> list;
    private final String[] args;
    private Map<Class<? extends JCConfiguration>, JCConfiguration> buggyConfigurations;

    public SBJCommanderConfiguration(List<JCConfiguration> list, ApplicationArguments args) {
        this.list = list;
        this.args = args.getSourceArgs();
        init();
    }

    @Bean("initAndGetBuggyConfigurations")
    public Map<Class<? extends JCConfiguration>, JCConfiguration> getBuggyConfigurations() {
        return buggyConfigurations;
    }

    @Bean("isBuggyConfigured")
    public boolean isBuggyConfigured() {
        return buggyConfigurations.containsKey(BuggyConfig.class);
    }

    @PostConstruct
    public void postConstruct() {
        printConfigurationsParams(buggyConfigurations);
    }

    public void init() {
        if (buggyConfigurations == null) {
            ConfigurationLogger.bPrint();
            ConfigurationLogger.cbPrint("JCommander configuration construction");
            ConfigurationLogger.sPrint();
            buggyConfigurations = new HashMap<>();
            JCommander jc = new JCommander();
            for (JCConfiguration config : list) {
                buggyConfigurations.put(config.getClass(), config);
                checkConfiguration(config);
                if (config.getClass().isAnnotationPresent(Parameters.class) &&
                        config.getClass().getAnnotation(Parameters.class).commandNames().length > 0) {
                    jc.addCommand(config);
                } else {
                    jc.addObject(config);
                }
                ConfigurationLogger.fdPrint(config.getClass().getSimpleName(), "OK");
            }
            try {
                jc.parse(args);
                ConfigurationLogger.sPrint();
                ConfigurationLogger.fdPrint("Parsing arguments", "OK");
            } catch (Exception e) {
                ConfigurationLogger.sPrint();
                ConfigurationLogger.fdPrint("Parsing arguments", "FAIL");
                printConfigurationsParams(buggyConfigurations);
                if (!(e instanceof ParameterException)) {
                    e.printStackTrace();
                }
                exitRunWithErr(e);
            }
        }
    }

    public void checkConfiguration(JCConfiguration config) {
        for (Field field : JUtils.getFields(config)) {
            Parameter parameter = field.getAnnotation(Parameter.class);
            if (parameter != null && !Modifier.isStatic(field.getModifiers())) {
                String[] names = parameter.names();
                ConfigurationLogger.fdPrint(config.getClass().getSimpleName(), "FAIL");
                printConfigurationParams(config);
                exitRunWithErr("Field " + config.getClass().getSimpleName() + "#" + field.getName() +
                        " marked with @Parameter " + Arrays.toString(names) + " must be static.");
            }
        }
        for (Method method : JUtils.getMethods(config)) {
            String error = null;
            Parameter parameter = method.getAnnotation(Parameter.class);
            if (parameter != null) {
                String[] names = parameter.names();
                StringJoiner parameters = new StringJoiner(", ");
                for (Class<?> parameterType : method.getParameterTypes()) {
                    parameters.add(parameterType.getSimpleName());
                }
                if (!Modifier.isStatic(method.getModifiers())) {
                    error = "Method " + config.getClass().getSimpleName() + "#" + method.getName() +
                            "(" + parameters.toString() + ")" +
                            " marked with @Parameter " + Arrays.toString(names) + " must be static.";

                }
                if (!method.getName().startsWith("is") & !method.getName().startsWith("set")) {
                    error = "Method " + config.getClass().getSimpleName() + "#" + method.getName() +
                            "(" + parameters.toString() + ")" +
                            " marked with @Parameter " + Arrays.toString(names) + " must be а setter " +
                            "(method name must start with 'is' or 'set').";
                }
                if (error != null) {
                    ConfigurationLogger.fdPrint(config.getClass().getSimpleName(), "FAIL");
                    printConfigurationParams(config);
                    exitRunWithErr(error);
                }
            }
        }
    }

    public void printConfigurationsParams(Map<Class<? extends JCConfiguration>, JCConfiguration> configs) {
        for (JCConfiguration autowiredConfig : list) {
            JCConfiguration config = configs.get(autowiredConfig.getClass());
            printConfigurationParams(config);
        }
    }

    public void printConfigurationParams(JCConfiguration config) {
        Map<String, Object> params = config.configurationToMap();
        if (params != null && !params.isEmpty()) {
            ConfigurationLogger.sPrint();
            ConfigurationLogger.cPrint(config.getClass().getSimpleName());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                ConfigurationLogger.fdPrint(entry.getKey(), entry.getValue());
            }
        }
    }

}
