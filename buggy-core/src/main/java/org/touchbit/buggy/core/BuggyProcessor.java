package org.touchbit.buggy.core;

import com.beust.jcommander.JCommander;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;
import org.touchbit.buggy.core.config.PrimaryConfig;
import org.touchbit.buggy.core.config.SecondaryConfig;
import org.touchbit.buggy.core.testng.TestSuite;
import org.touchbit.buggy.core.utils.log.BuggyLog;

import java.util.List;

/**
 * Created by Oleg Shaburov on 20.10.2018
 * shaburov.o.a@gmail.com
 */
public interface BuggyProcessor {

    PrimaryConfig getPrimaryConfig(Class<? extends PrimaryConfig> primaryConfigClass);

    List<SecondaryConfig> getSecondaryConfigList(List<Class<? extends SecondaryConfig>> secondaryConfigClasses);

    JCommander getJCommander(PrimaryConfig primary, List<SecondaryConfig> secondary, String name, String... args);

    boolean preparePrimaryConfig(PrimaryConfig primaryConfig);

    void prepareBuggyLog(Class<? extends BuggyLog> buggyLogClass);

    List<TestSuite> getTestSuites();

    List<XmlSuite> getXmlSuitesForConfiguration(PrimaryConfig config, List<TestSuite> testSuites);

    void prepareTestNG(TestNG testNG);

    int runTestNG(TestNG testNG);

    <T extends PrimaryConfig> String getRunDirectory(Class<T> clazz);

    String getReportsOutputDirectory();

    ExitHandler getExitHandler();

    void printConfig(PrimaryConfig primaryConfig);

}
