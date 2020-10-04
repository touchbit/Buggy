package org.touchbit.buggy.spring.boot.starter.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("buggy")
public class ApplicationProperties {
    
    private static final String BASE_BUGGY_SCAN_PACKAGE = "**.buggy";
    private List<String> commandsScannerBasePackages = new ArrayList<>();
    private List<String> goalsScannerBasePackages = new ArrayList<>();
    private List<String> suitesScannerBasePackages = new ArrayList<>();
    private List<String> listenersScannerBasePackages = new ArrayList<>();

    public ApplicationProperties() {
        commandsScannerBasePackages.add(BASE_BUGGY_SCAN_PACKAGE);
        goalsScannerBasePackages.add(BASE_BUGGY_SCAN_PACKAGE);
        suitesScannerBasePackages.add(BASE_BUGGY_SCAN_PACKAGE);
        listenersScannerBasePackages.add(BASE_BUGGY_SCAN_PACKAGE);
    }

    public List<String> getCommandsScannerBasePackages() {
        return commandsScannerBasePackages;
    }

    public void setCommandsScannerBasePackages(List<String> commandsScannerBasePackages) {
        this.commandsScannerBasePackages = commandsScannerBasePackages;
    }

    public List<String> getGoalsScannerBasePackages() {
        return goalsScannerBasePackages;
    }

    public void setGoalsScannerBasePackages(List<String> goalsScannerBasePackages) {
        this.goalsScannerBasePackages = goalsScannerBasePackages;
    }

    public List<String> getSuitesScannerBasePackages() {
        return suitesScannerBasePackages;
    }

    public void setSuitesScannerBasePackages(List<String> suitesScannerBasePackages) {
        this.suitesScannerBasePackages = suitesScannerBasePackages;
    }

    public List<String> getListenersScannerBasePackages() {
        return listenersScannerBasePackages;
    }

    public void setListenersScannerBasePackages(List<String> listenersScannerBasePackages) {
        this.listenersScannerBasePackages = listenersScannerBasePackages;
    }

}
