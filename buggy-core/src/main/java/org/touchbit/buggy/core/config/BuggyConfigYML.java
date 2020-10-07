package org.touchbit.buggy.core.config;

import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.model.ParallelMode;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.utils.JUtils;

import java.util.List;
import java.util.stream.Collectors;

public final class BuggyConfigYML {

    public void setHelp(Boolean help) {
        BuggyConfig.setHelp(help);
    }

    public void setForce(Boolean force) {
        BuggyConfig.setForce(force);
    }

    public void setPrintSuite(Boolean printSuite) {
        BuggyConfig.setPrintSuite(printSuite);
    }

    public void setPrintCause(Boolean printCause) {
        BuggyConfig.setPrintCause(printCause);
    }

    public void setPrintLog(Boolean printLog) {
        BuggyConfig.setPrintLog(printLog);
    }

    public void setPrintLogOnlyFail(Boolean printLogOnlyFail) {
        BuggyConfig.setPrintLogOnlyFail(printLogOnlyFail);
    }

    public void setVersion(Boolean version) {
        BuggyConfig.setVersion(version);
    }

    public void setThreads(Integer threads) {
        BuggyConfig.setThreads(threads);
    }

    public void setArtifactsUrl(String artifactsUrl) {
        BuggyConfig.setArtifactsUrl(artifactsUrl);
    }

    public void setParallelMode(ParallelMode parallelMode) {
        BuggyConfig.setParallelMode(parallelMode);
    }

    public void setComponents(List<String> components) {
        BuggyConfig.setComponents(components.stream()
                .map(c -> (Component) JUtils.newInstance(c, BuggyConfigurationException::new))
                .collect(Collectors.toList()));
    }

    public void setServices(List<String> services) {
        BuggyConfig.setServices(services.stream()
                .map(s -> (Service) JUtils.newInstance(s, BuggyConfigurationException::new))
                .collect(Collectors.toList()));
    }

    public void setInterfaces(List<String> interfaces) {
        BuggyConfig.setInterfaces(interfaces.stream()
                .map(i -> (Interface) JUtils.newInstance(i, BuggyConfigurationException::new))
                .collect(Collectors.toList()));
    }

    public void setTypes(List<Type> types) {
        BuggyConfig.setTypes(types);
    }

    public void setProgramName(String programName) {
        BuggyConfig.setProgramName(programName);
    }

    public String getTaskTrackerIssueUrl() {
        return BuggyConfig.getTaskTrackerIssueUrl();
    }

    public void setTaskTrackerIssueUrl(String taskTrackerIssueUrl) {
        BuggyConfig.setTaskTrackerIssueUrl(taskTrackerIssueUrl);
    }
}
