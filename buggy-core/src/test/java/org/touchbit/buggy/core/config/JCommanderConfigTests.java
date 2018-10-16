/*
 * Copyright © 2018 Shaburov Oleg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.touchbit.buggy.core.config;

import com.beust.jcommander.Parameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.BaseUnitTest;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.indirect.TestInterface;
import org.touchbit.buggy.core.indirect.TestService;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.process.Interface;
import org.touchbit.buggy.core.process.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.touchbit.buggy.core.model.Type.INTEGRATION;
import static org.touchbit.buggy.core.model.Type.SYSTEM;

/**
 * Created by Oleg Shaburov on 19.09.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("ALL")
@DisplayName("JCommander configuration tests")
class JCommanderConfigTests extends BaseUnitTest {

    @Test
    @DisplayName("Check parameters constructor")
    void unitTest_20180919162214() throws NoSuchMethodException {
        checkUtilityClassConstructor(Parameters.class);
    }

    @Test
    @DisplayName("Check PrimaryConfig.getDefaultValueProvider()")
    void unitTest_20180919192728() {
        assertThat(PRIMARY_CONFIG.getDefaultValueProvider(), instanceOf(PrimaryConfig.DefaultValueProvider.class));
    }

    @Test
    @DisplayName("Check PrimaryConfig.isHelp()")
    void unitTest_20180919193201() {
        assertThat(PRIMARY_CONFIG.isHelp(), is(false));
        PRIMARY_CONFIG.setHelp(true);
        assertThat(PRIMARY_CONFIG.isHelp(), is(false));
    }

    @Test
    @DisplayName("Check PrimaryConfig.isSmoke()")
    void unitTest_20180919193410() {
        boolean isSmoke = PRIMARY_CONFIG.isSmoke();
        try {
            assertThat(isSmoke, is(false));
            PRIMARY_CONFIG.setSmoke(true);
            assertThat(PRIMARY_CONFIG.isSmoke(), is(true));
        } finally {
            PRIMARY_CONFIG.setSmoke(isSmoke);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setPrintAllParameters(true)")
    void unitTest_20180919193619() {
        boolean isPrintAllParameters = PRIMARY_CONFIG.isPrintAllParameters();
        try {
            assertThat(isPrintAllParameters, is(false));
            PRIMARY_CONFIG.setPrintAllParameters(true);
            assertThat(PRIMARY_CONFIG.isPrintAllParameters(), is(true));
        } finally {
            PRIMARY_CONFIG.setPrintAllParameters(isPrintAllParameters);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setForceRun(true)")
    void unitTest_20180919193913() {
        boolean isForceRun = PRIMARY_CONFIG.isForceRun();
        try {
            assertThat(isForceRun, is(false));
            PRIMARY_CONFIG.setForceRun(true);
            assertThat(PRIMARY_CONFIG.isForceRun(), is(true));
        } finally {
            PRIMARY_CONFIG.setForceRun(isForceRun);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setThreads(10)")
    void unitTest_20180919194007() {
        Integer threads = PRIMARY_CONFIG.getThreads();
        try {
            assertThat(threads, is(50));
            PRIMARY_CONFIG.setThreads(10);
            assertThat(PRIMARY_CONFIG.getThreads(), is(10));
        } finally {
            PRIMARY_CONFIG.setThreads(threads);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setLogPath(\"testLogs\")")
    void unitTest_20180919194154() {
        String log = PRIMARY_CONFIG.getLogPath();
        try {
            assertThat(log, is("logs"));
            PRIMARY_CONFIG.setLogPath("testLogs");
            assertThat(PRIMARY_CONFIG.getLogPath(), is("testLogs"));
        } finally {
            PRIMARY_CONFIG.setLogPath(log);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setServices(List<Service> services)")
    void unitTest_20180919194549() {
        List<Service> services = PRIMARY_CONFIG.getServices();
        try {
            List<Service> newServices = new ArrayList<Service>() {{ add(new TestService()); }};
            PRIMARY_CONFIG.setServices(newServices);
            assertThat(PRIMARY_CONFIG.getServices(), is(newServices));
        } finally {
            PRIMARY_CONFIG.setServices(services);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setInterfaces(List<Interface> interfaces)")
    void unitTest_20180919195248() {
        List<Interface> interfaces = PRIMARY_CONFIG.getInterfaces();
        try {
            List<Interface> newInterfaces = new ArrayList<Interface>() {{ add(new TestInterface()); }};
            PRIMARY_CONFIG.setInterfaces(newInterfaces);
            assertThat(PRIMARY_CONFIG.getInterfaces(), is(newInterfaces));
        } finally {
            PRIMARY_CONFIG.setInterfaces(interfaces);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setType(Type type)")
    void unitTest_20180919195515() {
        Type type = PRIMARY_CONFIG.getType();
        try {
            assertThat(type, is(INTEGRATION));
            PRIMARY_CONFIG.setType(SYSTEM);
            assertThat(PRIMARY_CONFIG.getType(), is(SYSTEM));
        } finally {
            PRIMARY_CONFIG.setType(type);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setStatus(Integer status)")
    void unitTest_20180919195706() {
        Integer status = PRIMARY_CONFIG.getStatus();
        try {
            assertThat(status, is(nullValue()));
            PRIMARY_CONFIG.setStatus(10);
            assertThat(PRIMARY_CONFIG.getStatus(), is(10));
        } finally {
            PRIMARY_CONFIG.setStatus(status);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setNotify(Boolean notify)")
    void unitTest_20180919201358() {
        boolean notify = PRIMARY_CONFIG.isNotify();
        try {
            assertThat(notify, is(false));
            PRIMARY_CONFIG.setNotify(true);
            assertThat(PRIMARY_CONFIG.isNotify(), is(true));
        } finally {
            PRIMARY_CONFIG.setNotify(notify);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setArtifactsUrl(String buildsUrl)")
    void unitTest_20180919201516() {
        String buildLogUrl = PRIMARY_CONFIG.getArtifactsUrl();
        try {
            assertThat(buildLogUrl, is("null"));
            PRIMARY_CONFIG.setArtifactsUrl("BuildUrl");
            assertThat(PRIMARY_CONFIG.getArtifactsUrl(), is("BuildUrl"));
        } finally {
            PRIMARY_CONFIG.setArtifactsUrl(buildLogUrl);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setPrintSuite(Boolean printSuite)")
    void unitTest_20180919201621() {
        boolean printSuite = PRIMARY_CONFIG.isPrintSuite();
        try {
            assertThat(printSuite, is(false));
            PRIMARY_CONFIG.setPrintSuite(true);
            assertThat(PRIMARY_CONFIG.isPrintSuite(), is(true));
        } finally {
            PRIMARY_CONFIG.setPrintSuite(printSuite);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setPrintCause(Boolean printCause)")
    void unitTest_20180919201754() {
        boolean printCause = PRIMARY_CONFIG.isPrintCause();
        try {
            assertThat(printCause, is(false));
            PRIMARY_CONFIG.setPrintCause(true);
            assertThat(PRIMARY_CONFIG.isPrintCause(), is(true));
        } finally {
            PRIMARY_CONFIG.setPrintCause(printCause);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setPrintLogFile(Boolean printLog)")
    void unitTest_20180919201844() {
        boolean printLogFile = PRIMARY_CONFIG.isPrintLogFile();
        try {
            assertThat(printLogFile, is(false));
            PRIMARY_CONFIG.setPrintLogFile(true);
            assertThat(PRIMARY_CONFIG.isPrintLogFile(), is(true));
        } finally {
            PRIMARY_CONFIG.setPrintLogFile(printLogFile);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setVersion(Boolean version)")
    void unitTest_20180919201933() {
        assertThat(PRIMARY_CONFIG.isVersion(), is(false));
        PRIMARY_CONFIG.setVersion(true);
        assertThat(PRIMARY_CONFIG.isVersion(), is(false));
    }

    @Test
    @DisplayName("Check PrimaryConfig.setAbsoluteLogPath(String path)")
    void unitTest_20180919202117() {
        String absolutePath = PRIMARY_CONFIG.getAbsoluteLogPath();
        try {
            assertThat(absolutePath, is(WASTE));
            PRIMARY_CONFIG.setAbsoluteLogPath("AbsolutePath");
            assertThat(PRIMARY_CONFIG.getAbsoluteLogPath(), is("AbsolutePath"));
        } finally {
            PRIMARY_CONFIG.setAbsoluteLogPath(absolutePath);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.getErrorLogDir()")
    void unitTest_20180919202645() {
        assertThat(PRIMARY_CONFIG.getErrorLogDir(), is(new File(WASTE, "errors")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getTestLogDir()")
    void unitTest_20180919202812() {
        assertThat(PRIMARY_CONFIG.getTestLogDir(), is(new File(WASTE, "tests")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getFixedLogDir()")
    void unitTest_20180919202835() {
        assertThat(PRIMARY_CONFIG.getFixedLogDir(), is(new File(WASTE, "fixed")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getImplementedLogDir()")
    void unitTest_20180919202856() {
        assertThat(PRIMARY_CONFIG.getImplementedLogDir(), is(new File(WASTE, "implemented")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getNewErrorLogDir()")
    void unitTest_20180919202909() {
        assertThat(PRIMARY_CONFIG.getNewErrorLogDir(), is(new File(WASTE, "errors/new")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getExpFixErrorLogDir()")
    void unitTest_20180919202937() {
        assertThat(PRIMARY_CONFIG.getExpFixErrorLogDir(), is(new File(WASTE, "errors/exp_fix")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getExpImplErrorLogDir()")
    void unitTest_20180919202959() {
        assertThat(PRIMARY_CONFIG.getExpImplErrorLogDir(), is(new File(WASTE, "errors/exp_impl")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getCorruptedErrorLogDir()")
    void unitTest_20180919203022() {
        assertThat(PRIMARY_CONFIG.getCorruptedErrorLogDir(), is(new File(WASTE, "errors/corrupted")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getBlockedErrorLogDir()")
    void unitTest_20180919203048() {
        assertThat(PRIMARY_CONFIG.getBlockedErrorLogDir(), is(new File(WASTE, "errors/blocked")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.")
    void unitTest_20180919204139() {
        String p = PrimaryConfig.configurationToString(PRIMARY_CONFIG);
        assertThat(p.contains("[--all]"), is(false));
    }

    @Test
    @DisplayName("Check PrimaryConfig.configurationToString(config) with PrintAllParameters(false)")
    void unitTest_20180919204525() {
        boolean printAllParameters = PRIMARY_CONFIG.isPrintAllParameters();
        try {
            PRIMARY_CONFIG.setPrintAllParameters(true);
            String p = PrimaryConfig.configurationToString(PRIMARY_CONFIG);
            assertThat(p.contains("[--all]"), is(true));
        } finally {
            PRIMARY_CONFIG.setPrintAllParameters(printAllParameters);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.configurationToString(config) with PrintAllParameters(true)")
    void unitTest_20180919205544() {
        JCommanderPrimaryConfig config = new JCommanderPrimaryConfig();
        config.setPrintAllParameters(true);
        String p = PrimaryConfig.configurationToString(config);
        assertThat(p.contains("[--phm]...................getPublicHiddenMethod"), is(true));
        assertThat(p.contains("[--php]....................publcHiddenParameter"), is(true));
        assertThat(p.contains("[--pm]..........................setPublicMethod"), is(true));
        assertThat(p.contains("[--pp]...........................publcParameter"), is(true));
        assertThat(p.contains("[--ppm]................................********"), is(true));
        assertThat(p.contains("[--ppp]................................********"), is(true));
    }

    @Test
    @DisplayName("Check PrimaryConfig.configurationToString(config) with private parameter")
    void unitTest_20180919211539() {
        JCommanderPrimaryConfigFailGetField config = new JCommanderPrimaryConfigFailGetField();
        config.setPrintAllParameters(true);
        BuggyConfigurationException exception = execute(() -> PrimaryConfig.configurationToString(config),
                BuggyConfigurationException.class);
        assertThat(exception.getMessage(), is("Unable to get privateParameter value"));
    }

    @Test
    @DisplayName("Check PrimaryConfig.")
    void unitTest_20180919212056() {
        JCommanderPrimaryConfigFailGetMethod config = new JCommanderPrimaryConfigFailGetMethod();
        config.setPrintAllParameters(true);
        BuggyConfigurationException exception = execute(() -> PrimaryConfig.configurationToString(config),
                BuggyConfigurationException.class);
        assertThat(exception.getMessage(), is("Unable to get getPrivateHiddenMethod method value"));
    }

    public static class JCommanderPrimaryConfigFailGetField implements PrimaryConfig {

        private static final String FF = "--ff";
        @Parameter(names = {FF})
        private static String privateParameter = "privateParameter";

    }

    public static class JCommanderPrimaryConfigFailGetMethod implements PrimaryConfig {

        private static final String FF = "--ff";

        @Parameter(names = {FF})
        private void setPrivateHiddenMethod(String s) {

        }

        private String getPrivateHiddenMethod() {
            return "getPrivateHiddenMethod";
        }

    }

    @SuppressWarnings("unused")
    public static class JCommanderPrimaryConfig implements PrimaryConfig {

        private static final String PP = "--pp";
        private static final String PHP = "--php";
        private static final String PPP = "--ppp";
        private static final String PM = "--pm";
        private static final String PHM = "--phm";
        private static final String PPM = "--ppm";

        @Parameter(names = {PP})
        public static String publicParameter = "publcParameter";

        @Parameter(names = {PHP}, hidden = true)
        public static String publicHiddenParameter = "publcHiddenParameter";

        @Parameter(names = {PPP}, password = true)
        public static String publicPasswordParameter = "publcPasswordParameter";

        @Parameter(names = {PM})
        public static void setPublicMethod(String s) {

        }

        public String getPublicMethod() {
            return "setPublicMethod";
        }

        @Parameter(names = {PHM}, hidden = true)
        public void setPublicHiddenMethod(String s) {

        }

        public String getPublicHiddenMethod() {
            return "getPublicHiddenMethod";
        }

        @Parameter(names = {PPM}, password = true)
        public void setPublicPasswordMethod(String s) {

        }

        public String getPublicPasswordMethod() {
            return "getPublicPasswordMethod";
        }

    }


}
