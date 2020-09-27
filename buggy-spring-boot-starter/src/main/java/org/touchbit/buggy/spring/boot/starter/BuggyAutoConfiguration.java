package org.touchbit.buggy.spring.boot.starter;

import com.beust.jcommander.JCommander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.testng.TestNG;
import org.touchbit.buggy.core.utils.StringUtils;

import java.util.Map;

import static org.touchbit.buggy.core.utils.BuggyUtils.CONSOLE_DELIMITER;

@Configuration
@DependsOn({"initBuggyConfiguration"})
@ConditionalOnNotWebApplication
@AutoConfigureBefore(JCommanderConfiguration.class)
public class BuggyAutoConfiguration {

    public BuggyAutoConfiguration() {
        StringUtils.println(CONSOLE_DELIMITER);
        StringUtils.println("Loading TestNG configuration");
    }

    @Bean
    public TestNG testNG() {
        StringUtils.println(CONSOLE_DELIMITER);
        return new TestNG();
    }

}
