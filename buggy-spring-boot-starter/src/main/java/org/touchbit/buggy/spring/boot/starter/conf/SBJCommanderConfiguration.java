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
import org.touchbit.buggy.core.goal.Goal;
import org.touchbit.buggy.core.goal.component.AllComponents;
import org.touchbit.buggy.core.goal.interfaze.AllInterfaces;
import org.touchbit.buggy.core.goal.service.AllServices;
import org.touchbit.buggy.core.model.Type;
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
        includeFilters = {
                @ComponentScan.Filter(type = ASSIGNABLE_TYPE, classes = JCConfiguration.class),
                @ComponentScan.Filter(type = ASSIGNABLE_TYPE, classes = Goal.class),
        })
public class SBJCommanderConfiguration implements SBConfiguration {

    private static final JCommander JC = new JCommander();
    private final List<JCConfiguration> jcConfigurations;
    private final List<Goal> goals;
    private final String[] args;
    private Map<Class<? extends JCConfiguration>, JCConfiguration> buggyConfigurations;

    public SBJCommanderConfiguration(List<JCConfiguration> list, List<Goal> goals, ApplicationArguments args) {
        this.goals = goals;
        this.jcConfigurations = list;
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
        if (BuggyConfig.getHelp()) {
            ConfigurationLogger.stepDelimeter();
            JC.usage();
            exitRun(0);
        }
        printConfigurationsParams(buggyConfigurations);
    }

    public void init() {
        if (buggyConfigurations == null) {
            ConfigurationLogger.blockDelimeter();
            ConfigurationLogger.centerBold("JCommander configuration construction");
            ConfigurationLogger.stepDelimeter();
            buggyConfigurations = new HashMap<>();

            for (JCConfiguration config : jcConfigurations) {
                buggyConfigurations.put(config.getClass(), config);
                checkConfiguration(config);
                if (config.getClass().isAnnotationPresent(Parameters.class) &&
                        config.getClass().getAnnotation(Parameters.class).commandNames().length > 0) {
                    JC.addCommand(config);
                } else {
                    JC.addObject(config);
                }
                ConfigurationLogger.dotPlaceholder(config.getClass().getSimpleName(), "OK");
            }
            try {
                JC.parse(args);
                ConfigurationLogger.stepDelimeter();
                ConfigurationLogger.dotPlaceholder("Parsing arguments", "OK");
            } catch (Exception e) {
                ConfigurationLogger.stepDelimeter();
                ConfigurationLogger.dotPlaceholder("Parsing arguments", "FAIL");
                printConfigurationsParams(buggyConfigurations);
                exitRunWithErr(e.getMessage(), e);
            }
        }
    }

    public void checkConfiguration(JCConfiguration config) {
        for (Field field : JUtils.getFields(config)) {
            Parameter parameter = field.getAnnotation(Parameter.class);
            if (parameter != null && !Modifier.isStatic(field.getModifiers())) {
                String[] names = parameter.names();
                ConfigurationLogger.dotPlaceholder(config.getClass().getSimpleName(), "FAIL");
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
                    ConfigurationLogger.dotPlaceholder(config.getClass().getSimpleName(), "FAIL");
                    printConfigurationParams(config);
                    exitRunWithErr(error);
                }
            }
        }
    }

    public void printConfigurationsParams(Map<Class<? extends JCConfiguration>, JCConfiguration> configs) {
        for (JCConfiguration autowiredConfig : jcConfigurations) {
            JCConfiguration config = configs.get(autowiredConfig.getClass());
            printConfigurationParams(config);
        }
    }

    public void printConfigurationParams(JCConfiguration config) {
        Map<String, Object> params = config.configurationToMap();
        if (params != null && !params.isEmpty()) {
            ConfigurationLogger.stepDelimeter();
            ConfigurationLogger.center(config.getClass().getSimpleName());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                ConfigurationLogger.dotPlaceholder(entry.getKey(), entry.getValue());
            }
        }
    }

}
