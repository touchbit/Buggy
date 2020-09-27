package org.touchbit.buggy.spring.boot.starter;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.touchbit.buggy.core.utils.StringUtils;

import java.util.*;

import static org.touchbit.buggy.core.utils.BuggyUtils.CONSOLE_DELIMITER;

//@Order(1)
@Configuration
@ConditionalOnNotWebApplication
@ComponentScan(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {IBuggyConfig.class}))
public class JCommanderConfiguration {

    private final List<IBuggyConfig> list;
    private final String[] args;

    public JCommanderConfiguration(List<IBuggyConfig> list, ApplicationArguments args) {
        this.list = list;
        this.args = args.getSourceArgs();
        StringUtils.println(CONSOLE_DELIMITER);
        StringUtils.println("Loading IBuggyConfig implementations");
    }

    @Bean("initBuggyConfiguration")
    public Map<Class<? extends IBuggyConfig>, IBuggyConfig> initBuggyConfiguration() {
        StringUtils.println(CONSOLE_DELIMITER);
        Map<Class<? extends IBuggyConfig>, IBuggyConfig> result = new HashMap<>();
        for (IBuggyConfig config : list) {
            result.put(config.getClass(), config);
            StringUtils.println(StringUtils
                    .dotFiller(config.getClass().getSimpleName(), 47, "OK"));
        }
        JCommander jc = new JCommander();
        addConfiguration(jc, result.values());
        try {
            jc.parse(args);
        } catch (ParameterException e) {
            exitRunWithUsage(e);
        }
        return result;
    }

    private void addConfiguration(JCommander jc, Collection<IBuggyConfig> configs) {
        for (IBuggyConfig c : configs) {
            if (c.getClass().isAnnotationPresent(Parameters.class) &&
                    c.getClass().getAnnotation(Parameters.class).commandNames().length > 0) {
                jc.addCommand(c);
            } else {
                jc.addObject(c);
            }
        }
    }

    public void exitRunWithUsage(ParameterException e) {
        StringUtils.println(CONSOLE_DELIMITER);
        e.usage();
        StringUtils.println(CONSOLE_DELIMITER);
        StringUtils.println(e.getMessage());
        StringUtils.println(CONSOLE_DELIMITER);
        StringUtils.println(StringUtils.dotFiller("Exit code", 47, 1));
        StringUtils.println(CONSOLE_DELIMITER);
        System.exit(1);
    }

}
