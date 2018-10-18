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

package org.touchbit.buggy.core.tests.config;

import com.beust.jcommander.Parameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.tests.BaseUnitTest;
import org.touchbit.buggy.core.config.Parameters;
import org.touchbit.buggy.core.config.PrimaryConfig;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.config.TestInterface;
import org.touchbit.buggy.core.config.TestService;
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
        assertThat(Buggy.getPrimaryConfig().getDefaultValueProvider(), instanceOf(PrimaryConfig.DefaultValueProvider.class));
    }

    @Test
    @DisplayName("Check PrimaryConfig.isHelp()")
    void unitTest_20180919193201() {
        assertThat(Buggy.getPrimaryConfig().isHelp(), is(false));
        Buggy.getPrimaryConfig().setHelp(true);
        assertThat(Buggy.getPrimaryConfig().isHelp(), is(false));
    }

    @Test
    @DisplayName("Check PrimaryConfig.isSmoke()")
    void unitTest_20180919193410() {
        boolean isSmoke = Buggy.getPrimaryConfig().isSmoke();
        try {
            assertThat(isSmoke, is(false));
            Buggy.getPrimaryConfig().setSmoke(true);
            assertThat(Buggy.getPrimaryConfig().isSmoke(), is(true));
        } finally {
            Buggy.getPrimaryConfig().setSmoke(isSmoke);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setPrintAllParameters(true)")
    void unitTest_20180919193619() {
        boolean isPrintAllParameters = Buggy.getPrimaryConfig().isPrintAllParameters();
        try {
            assertThat(isPrintAllParameters, is(false));
            Buggy.getPrimaryConfig().setPrintAllParameters(true);
            assertThat(Buggy.getPrimaryConfig().isPrintAllParameters(), is(true));
        } finally {
            Buggy.getPrimaryConfig().setPrintAllParameters(isPrintAllParameters);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setForceRun(true)")
    void unitTest_20180919193913() {
        boolean isForceRun = Buggy.getPrimaryConfig().isForceRun();
        try {
            assertThat(isForceRun, is(false));
            Buggy.getPrimaryConfig().setForceRun(true);
            assertThat(Buggy.getPrimaryConfig().isForceRun(), is(true));
        } finally {
            Buggy.getPrimaryConfig().setForceRun(isForceRun);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setThreads(10)")
    void unitTest_20180919194007() {
        Integer threads = Buggy.getPrimaryConfig().getThreads();
        try {
            assertThat(threads, is(50));
            Buggy.getPrimaryConfig().setThreads(10);
            assertThat(Buggy.getPrimaryConfig().getThreads(), is(10));
        } finally {
            Buggy.getPrimaryConfig().setThreads(threads);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setLogPath(\"testLogs\")")
    void unitTest_20180919194154() {
        String log = Buggy.getPrimaryConfig().getLogPath();
        try {
            assertThat(log, is("logs"));
            Buggy.getPrimaryConfig().setLogPath("testLogs");
            assertThat(Buggy.getPrimaryConfig().getLogPath(), is("testLogs"));
        } finally {
            Buggy.getPrimaryConfig().setLogPath(log);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setServices(List<Service> services)")
    void unitTest_20180919194549() {
        List<Service> services = Buggy.getPrimaryConfig().getServices();
        try {
            List<Service> newServices = new ArrayList<Service>() {{ add(new TestService()); }};
            Buggy.getPrimaryConfig().setServices(newServices);
            assertThat(Buggy.getPrimaryConfig().getServices(), is(newServices));
        } finally {
            Buggy.getPrimaryConfig().setServices(services);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setInterfaces(List<Interface> interfaces)")
    void unitTest_20180919195248() {
        List<Interface> interfaces = Buggy.getPrimaryConfig().getInterfaces();
        try {
            List<Interface> newInterfaces = new ArrayList<Interface>() {{ add(new TestInterface()); }};
            Buggy.getPrimaryConfig().setInterfaces(newInterfaces);
            assertThat(Buggy.getPrimaryConfig().getInterfaces(), is(newInterfaces));
        } finally {
            Buggy.getPrimaryConfig().setInterfaces(interfaces);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setType(Type type)")
    void unitTest_20180919195515() {
        Type type = Buggy.getPrimaryConfig().getType();
        try {
            assertThat(type, is(INTEGRATION));
            Buggy.getPrimaryConfig().setType(SYSTEM);
            assertThat(Buggy.getPrimaryConfig().getType(), is(SYSTEM));
        } finally {
            Buggy.getPrimaryConfig().setType(type);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setStatus(Integer status)")
    void unitTest_20180919195706() {
        Integer status = Buggy.getPrimaryConfig().getStatus();
        try {
            assertThat(status, is(nullValue()));
            Buggy.getPrimaryConfig().setStatus(10);
            assertThat(Buggy.getPrimaryConfig().getStatus(), is(10));
        } finally {
            Buggy.getPrimaryConfig().setStatus(status);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setNotify(Boolean notify)")
    void unitTest_20180919201358() {
        boolean notify = Buggy.getPrimaryConfig().isNotify();
        try {
            assertThat(notify, is(false));
            Buggy.getPrimaryConfig().setNotify(true);
            assertThat(Buggy.getPrimaryConfig().isNotify(), is(true));
        } finally {
            Buggy.getPrimaryConfig().setNotify(notify);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setArtifactsUrl(String buildsUrl)")
    void unitTest_20180919201516() {
        String buildLogUrl = Buggy.getPrimaryConfig().getArtifactsUrl();
        try {
            assertThat(String.valueOf(buildLogUrl), is("null"));
            Buggy.getPrimaryConfig().setArtifactsUrl("BuildUrl");
            assertThat(Buggy.getPrimaryConfig().getArtifactsUrl(), is("BuildUrl"));
        } finally {
            Buggy.getPrimaryConfig().setArtifactsUrl(buildLogUrl);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setPrintSuite(Boolean printSuite)")
    void unitTest_20180919201621() {
        boolean printSuite = Buggy.getPrimaryConfig().isPrintSuite();
        try {
            assertThat(printSuite, is(false));
            Buggy.getPrimaryConfig().setPrintSuite(true);
            assertThat(Buggy.getPrimaryConfig().isPrintSuite(), is(true));
        } finally {
            Buggy.getPrimaryConfig().setPrintSuite(printSuite);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setPrintCause(Boolean printCause)")
    void unitTest_20180919201754() {
        boolean printCause = Buggy.getPrimaryConfig().isPrintCause();
        try {
            assertThat(printCause, is(false));
            Buggy.getPrimaryConfig().setPrintCause(true);
            assertThat(Buggy.getPrimaryConfig().isPrintCause(), is(true));
        } finally {
            Buggy.getPrimaryConfig().setPrintCause(printCause);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setPrintLogFile(Boolean printLog)")
    void unitTest_20180919201844() {
        boolean printLogFile = Buggy.getPrimaryConfig().isPrintLogFile();
        try {
            assertThat(printLogFile, is(false));
            Buggy.getPrimaryConfig().setPrintLogFile(true);
            assertThat(Buggy.getPrimaryConfig().isPrintLogFile(), is(true));
        } finally {
            Buggy.getPrimaryConfig().setPrintLogFile(printLogFile);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setVersion(Boolean version)")
    void unitTest_20180919201933() {
        assertThat(Buggy.getPrimaryConfig().isVersion(), is(false));
        Buggy.getPrimaryConfig().setVersion(true);
        assertThat(Buggy.getPrimaryConfig().isVersion(), is(false));
    }

    @Test
    @DisplayName("Check PrimaryConfig.setAbsoluteLogPath(String path)")
    void unitTest_20180919202117() {
        String absolutePath = Buggy.getPrimaryConfig().getAbsoluteLogPath();
        try {
            assertThat(absolutePath, is(WASTE));
            Buggy.getPrimaryConfig().setAbsoluteLogPath("AbsolutePath");
            assertThat(Buggy.getPrimaryConfig().getAbsoluteLogPath(), is("AbsolutePath"));
        } finally {
            Buggy.getPrimaryConfig().setAbsoluteLogPath(absolutePath);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.getErrorLogDir()")
    void unitTest_20180919202645() {
        assertThat(Buggy.getPrimaryConfig().getErrorLogDir(), is(new File(WASTE, "errors")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getTestLogDir()")
    void unitTest_20180919202812() {
        assertThat(Buggy.getPrimaryConfig().getTestLogDir(), is(new File(WASTE, "tests")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getFixedLogDir()")
    void unitTest_20180919202835() {
        assertThat(Buggy.getPrimaryConfig().getFixedLogDir(), is(new File(WASTE, "fixed")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getImplementedLogDir()")
    void unitTest_20180919202856() {
        assertThat(Buggy.getPrimaryConfig().getImplementedLogDir(), is(new File(WASTE, "implemented")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getNewErrorLogDir()")
    void unitTest_20180919202909() {
        assertThat(Buggy.getPrimaryConfig().getNewErrorLogDir(), is(new File(WASTE, "errors/new")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getExpFixErrorLogDir()")
    void unitTest_20180919202937() {
        assertThat(Buggy.getPrimaryConfig().getExpFixErrorLogDir(), is(new File(WASTE, "errors/exp_fix")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getExpImplErrorLogDir()")
    void unitTest_20180919202959() {
        assertThat(Buggy.getPrimaryConfig().getExpImplErrorLogDir(), is(new File(WASTE, "errors/exp_impl")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getCorruptedErrorLogDir()")
    void unitTest_20180919203022() {
        assertThat(Buggy.getPrimaryConfig().getCorruptedErrorLogDir(), is(new File(WASTE, "errors/corrupted")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.getBlockedErrorLogDir()")
    void unitTest_20180919203048() {
        assertThat(Buggy.getPrimaryConfig().getBlockedErrorLogDir(), is(new File(WASTE, "errors/blocked")));
    }

    @Test
    @DisplayName("Check PrimaryConfig.")
    void unitTest_20180919204139() {
        String p = PrimaryConfig.configurationToString(Buggy.getPrimaryConfig());
        assertThat(p.contains("[--all]"), is(false));
    }

    @Test
    @DisplayName("Check PrimaryConfig.configurationToString(config) with PrintAllParameters(false)")
    void unitTest_20180919204525() {
        boolean printAllParameters = Buggy.getPrimaryConfig().isPrintAllParameters();
        try {
            Buggy.getPrimaryConfig().setPrintAllParameters(true);
            String p = PrimaryConfig.configurationToString(Buggy.getPrimaryConfig());
            assertThat(p.contains("[--all]"), is(true));
        } finally {
            Buggy.getPrimaryConfig().setPrintAllParameters(printAllParameters);
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
