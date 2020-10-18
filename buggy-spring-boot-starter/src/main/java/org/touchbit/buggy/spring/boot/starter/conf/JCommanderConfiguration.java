package org.touchbit.buggy.spring.boot.starter.conf;

import com.beust.jcommander.*;
import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.touchbit.buggy.core.config.BuggyConfigurationYML;
import org.touchbit.buggy.core.logback.ConfLogger;
import org.touchbit.buggy.core.utils.JUtils;
import org.touchbit.buggy.spring.boot.starter.BuggyRunner;
import org.touchbit.buggy.spring.boot.starter.jcommander.BuggyConfiguration;
import org.touchbit.buggy.core.config.JCommand;
import org.touchbit.buggy.spring.boot.starter.jcommander.config.EnvironmentUsageFormatter;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;

/**
 * {@link JCommander} spring boot configuration class.
 * {@link ComponentScan} searches for all implementations of the {@link JCommand} class.
 * The search is done for packages that contain a "buggy" package.
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
@Configuration
@ConditionalOnNotWebApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class JCommanderConfiguration implements IConfiguration {

    private final JCommander jCommander;
    private final Set<JCommand> jCommands = new LinkedHashSet<>();
    private final ApplicationProperties properties;
    private final Map<Class<? extends JCommand>, JCommand> buggyConfigurations = new HashMap<>();
    private final boolean isJCommanderConfigured;

    public JCommanderConfiguration(final ApplicationProperties properties, final ApplicationArguments appArgs) {
        try {
            beforeConfiguration();
            String[] runArgs = appArgs.getSourceArgs();
            this.properties = properties;
            this.jCommands.addAll(scanBuggyConfig());
            scanJCConfigurations().stream()
                    .filter(i -> !(i instanceof BuggyConfiguration))
                    .forEach(this.jCommands::add);
            this.jCommander = buildJCommander(this.jCommands, buggyConfigurations);
            parseEnvArguments(this.jCommander);
            parseRunArguments(this.jCommander, runArgs);
            isJCommanderConfigured = true;
        } catch (Exception e) {
            BuggyRunner.exit("Error initializing JCommander config", e);
            throw e;
        }
    }

    public void beforeConfiguration() {
        ConfLogger.blockDelimiter();
        ConfLogger.centerBold("JCommander configuration construction");
        ConfLogger.stepDelimiter();
    }

    @Bean
    public boolean isJCommanderConfigured() {
        return isJCommanderConfigured;
    }

    @PostConstruct
    public void postConstruct() {
        if (BuggyConfiguration.isHelp()) {
            ConfLogger.stepDelimiter();
            jCommander.usage();
            BuggyRunner.exit(0);
        }
        if (BuggyConfiguration.isVersion()) {
            ConfLogger.stepDelimiter();
            ConfLogger.centerBold("Version info");
            JUtils.getBuggyManifest().forEach(ConfLogger::dotPlaceholder);
            BuggyRunner.exit(0);
        }
        printConfigurationsParams(buggyConfigurations);
    }

    private Set<BuggyConfiguration> scanBuggyConfig() {
        final boolean useDefaultFilters = false;
        final String basePackage = "org.touchbit.buggy.spring.boot.starter.jcommander";
        final Set<BeanDefinition> defs = scanBeanDefinitions(useDefaultFilters, basePackage, BuggyConfiguration.class);
        return getBeanDefinitionInstances(defs, BuggyConfiguration.class);
    }

    public Set<JCommand> scanJCConfigurations() {
        final Set<BeanDefinition> defs = new LinkedHashSet<>();
        final List<String> basePackages = properties.getCommandsScannerBasePackages().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        for (String basePackage : basePackages) {
            defs.addAll(scanBeanDefinitions(false, basePackage, JCommand.class));
        }
        return getBeanDefinitionInstances(defs, JCommand.class);
    }

    private JCommander buildJCommander(Set<JCommand> jCommands, Map<Class<? extends JCommand>, JCommand> configMap) {
        JCommander jCommander = new JCommander();
        jCommander.setUsageFormatter(new EnvironmentUsageFormatter(jCommander));
        for (JCommand config : jCommands) {
            configMap.put(config.getClass(), config);
            checkConfiguration(config);
            try {
                if (config.getClass().isAnnotationPresent(Parameters.class) &&
                        config.getClass().getAnnotation(Parameters.class).commandNames().length > 0) {
                    jCommander.addCommand(config);
                } else {
                    jCommander.addObject(config);
                }
                ConfLogger.dotPlaceholder(config.getClass().getSimpleName(), "OK");
            } catch (Exception e) {
                ConfLogger.dotPlaceholder(config.getClass().getSimpleName(), "FAIL");
                BuggyRunner.exit(1, "Unexpected error while loading Jcommander configs", e);
            }
        }
        return jCommander;
    }

    public void parseEnvArguments(JCommander jCommander) {
        Map<String, String> buggyEnv = System.getenv().entrySet().stream()
                .filter(e -> e.getKey().toUpperCase().startsWith("BUGGY_"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        for (Map.Entry<String, String> entry : buggyEnv.entrySet()) {
            String[] envVar = new String[]{entry.getKey(), entry.getValue()};
            String key = UPPER_UNDERSCORE.to(LOWER_HYPHEN, entry.getKey().replace("BUGGY_", "__"));
            String[] runVar = new String[]{key, entry.getValue()};
            try {
                jCommander.parse(runVar);
            } catch (Exception e) {
                printConfigurationsParams(buggyConfigurations);
                BuggyRunner.exit("Error parsing environment variable: " + Arrays.toString(envVar), e);
            }
        }
    }

    public void parseRunArguments(JCommander jCommander, String[] args) {
        try {
            jCommander.parse(args);
            ConfLogger.stepDelimiter();
            ConfLogger.dotPlaceholder("Parsing arguments", "OK");
        } catch (Exception e) {
            ConfLogger.stepDelimiter();
            ConfLogger.dotPlaceholder("Parsing arguments", "FAIL");
            printConfigurationsParams(buggyConfigurations);
            BuggyRunner.exit(e.getMessage(), e);
        }
    }

    @Bean("isBuggyConfigured")
    public boolean isBuggyConfigured() {
        return buggyConfigurations.containsKey(BuggyConfigurationYML.class);
    }

    @Bean()
    public Map<Class<? extends JCommand>, JCommand> getBuggyConfigurations() {
        return buggyConfigurations;
    }

    public void checkConfiguration(JCommand config) {
        for (Field field : JUtils.getFields(config)) {
            Parameter parameter = field.getAnnotation(Parameter.class);
            if (parameter != null && !Modifier.isStatic(field.getModifiers())) {
                String[] names = parameter.names();
                ConfLogger.dotPlaceholder(config.getClass().getSimpleName(), "FAIL");
                printConfigurationParams(config);
                BuggyRunner.exit(1, "Field " + config.getClass().getSimpleName() + "#" + field.getName() +
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
                    ConfLogger.dotPlaceholder(config.getClass().getSimpleName(), "FAIL");
                    printConfigurationParams(config);
                    BuggyRunner.exit(1, error);
                }
            }
        }
    }

    public void printConfigurationsParams(Map<Class<? extends JCommand>, JCommand> configs) {
        for (JCommand autowiredConfig : jCommands) {
            JCommand config = configs.get(autowiredConfig.getClass());
            printConfigurationParams(config);
        }
    }

    public void printConfigurationParams(JCommand config) {
        Map<String, Object> params = config.configurationToMap();
        if (params != null && !params.isEmpty()) {
            ConfLogger.stepDelimiter();
            ConfLogger.center(config.getClass().getSimpleName());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                ConfLogger.dotPlaceholder(entry.getKey(), entry.getValue());
            }
        }
    }

}
