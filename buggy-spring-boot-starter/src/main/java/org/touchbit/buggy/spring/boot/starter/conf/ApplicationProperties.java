package org.touchbit.buggy.spring.boot.starter.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ApplicationProperties {

    @Value("${buggy.configuration.testng.scanner.suite.basePackage:**.buggy1}")
    private String testNGScannerSuiteBasePackage;

    @Value("${buggy.configuration.testng.scanner.suite.useDefaultFilters:false}")
    private boolean testNGScannerSuiteUseDefaultFilters;

    @Value("${buggy.configuration.testng.scanner.listener.basePackage:**.buggy1}")
    private String testNGScannerListenerBasePackage;

    @Value("${buggy.configuration.testng.scanner.listener.useDefaultFilters:false}")
    private boolean testNGScannerListenerUseDefaultFilters;

    public String getTestNGScannerSuiteBasePackage() {
        return testNGScannerSuiteBasePackage;
    }

    public void setTestNGScannerSuiteBasePackage(String testNGScannerSuiteBasePackage) {
        this.testNGScannerSuiteBasePackage = testNGScannerSuiteBasePackage;
    }

    public boolean isTestNGScannerSuiteUseDefaultFilters() {
        return testNGScannerSuiteUseDefaultFilters;
    }

    public void setTestNGScannerSuiteUseDefaultFilters(boolean testNGScannerSuiteUseDefaultFilters) {
        this.testNGScannerSuiteUseDefaultFilters = testNGScannerSuiteUseDefaultFilters;
    }

    public String getTestNGScannerListenerBasePackage() {
        return testNGScannerListenerBasePackage;
    }

    public void setTestNGScannerListenerBasePackage(String testNGScannerListenerBasePackage) {
        this.testNGScannerListenerBasePackage = testNGScannerListenerBasePackage;
    }

    public boolean isTestNGScannerListenerUseDefaultFilters() {
        return testNGScannerListenerUseDefaultFilters;
    }

    public void setTestNGScannerListenerUseDefaultFilters(boolean testNGScannerListenerUseDefaultFilters) {
        this.testNGScannerListenerUseDefaultFilters = testNGScannerListenerUseDefaultFilters;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
        c.setLocation(new ClassPathResource("application.yml"));
        c.setIgnoreUnresolvablePlaceholders(true);
        c.setIgnoreResourceNotFound(true);
        c.setTrimValues(true);
        c.setNullValue("");
        return c;
    }

}
