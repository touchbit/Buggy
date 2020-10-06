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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.config.BuggyConfig;
import org.touchbit.buggy.core.config.TestInterface;
import org.touchbit.buggy.core.config.TestService;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.tests.BaseUnitTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Oleg Shaburov on 19.09.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("ALL")
@DisplayName("JCommander configuration tests")
class JCommanderConfigTests extends BaseUnitTest {
//
//    @Test
//    @DisplayName("Check parameters constructor")
//    void unitTest_20180919162214() throws NoSuchMethodException {
//        checkUtilityClassConstructor(BParameters.class);
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.isHelp()")
//    void unitTest_20180919193201() {
//        assertThat(BuggyConfig.isHelp(), is(false));
//        BuggyConfig.setHelp(true);
//        assertThat(BuggyConfig.isHelp(), is(false));
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.setPrintAllParameters(true)")
//    void unitTest_20180919193619() {
//        boolean isPrintAllParameters = BuggyConfig.isPrintAllParameters();
//        try {
//            assertThat(isPrintAllParameters, is(false));
//            BuggyConfig.setPrintAllParameters(true);
//            assertThat(BuggyConfig.isPrintAllParameters(), is(true));
//        } finally {
//            BuggyConfig.setPrintAllParameters(isPrintAllParameters);
//        }
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.setForceRun(true)")
//    void unitTest_20180919193913() {
//        boolean isForceRun = BuggyConfig.isForceRun();
//        try {
//            assertThat(isForceRun, is(false));
//            BuggyConfig.setForceRun(true);
//            assertThat(BuggyConfig.isForceRun(), is(true));
//        } finally {
//            BuggyConfig.setForceRun(isForceRun);
//        }
//    }

    @Test
    @DisplayName("Check PrimaryConfig.setThreads(10)")
    void unitTest_20180919194007() {
        Integer threads = BuggyConfig.getThreads();
        try {
            assertThat(threads, is(50));
            BuggyConfig.setThreads(10);
            assertThat(BuggyConfig.getThreads(), is(10));
        } finally {
            BuggyConfig.setThreads(threads);
        }
    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.setLogPath(\"testLogs\")")
//    void unitTest_20180919194154() {
//        String log = BuggyConfig.getLogPath();
//        try {
//            assertThat(log, is("logs"));
//            BuggyConfig.setLogPath("testLogs");
//            assertThat(BuggyConfig.getLogPath(), is("testLogs"));
//        } finally {
//            BuggyConfig.setLogPath(log);
//        }
//    }

    @Test
    @DisplayName("Check PrimaryConfig.setServices(List<Service> services)")
    void unitTest_20180919194549() {
        List<Service> services = BuggyConfig.getServices();
        try {
            List<Service> newServices = new ArrayList<Service>() {{
                add(new TestService());
            }};
            BuggyConfig.setServices(newServices);
            assertThat(BuggyConfig.getServices(), is(newServices));
        } finally {
            BuggyConfig.setServices(services);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setInterfaces(List<Interface> interfaces)")
    void unitTest_20180919195248() {
        List<Interface> interfaces = BuggyConfig.getInterfaces();
        try {
            List<Interface> newInterfaces = new ArrayList<Interface>() {{
                add(new TestInterface());
            }};
            BuggyConfig.setInterfaces(newInterfaces);
            assertThat(BuggyConfig.getInterfaces(), is(newInterfaces));
        } finally {
            BuggyConfig.setInterfaces(interfaces);
        }
    }

//    @Test
//    @DisplayName("Check PrimaryConfig.setType(Type type)")
//    void unitTest_20180919195515() {
//        Type type = BuggyConfig.getType();
//        try {
//            assertThat(type, is(INTEGRATION));
//            BuggyConfig.setType(SYSTEM);
//            assertThat(BuggyConfig.getType(), is(SYSTEM));
//        } finally {
//            BuggyConfig.setType(type);
//        }
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.setStatus(Integer status)")
//    void unitTest_20180919195706() {
//        Integer status = BuggyConfig.getStatus();
//        try {
//            assertThat(status, is(nullValue()));
//            BuggyConfig.setStatus(10);
//            assertThat(BuggyConfig.getStatus(), is(10));
//        } finally {
//            BuggyConfig.setStatus(status);
//        }
//    }

    @Test
    @DisplayName("Check PrimaryConfig.setArtifactsUrl(String buildsUrl)")
    void unitTest_20180919201516() {
        String buildLogUrl = BuggyConfig.getArtifactsUrl();
        try {
            assertThat(String.valueOf(buildLogUrl), is("null"));
            BuggyConfig.setArtifactsUrl("BuildUrl");
            assertThat(BuggyConfig.getArtifactsUrl(), is("BuildUrl"));
        } finally {
            BuggyConfig.setArtifactsUrl(buildLogUrl);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setPrintSuite(Boolean printSuite)")
    void unitTest_20180919201621() {
        boolean printSuite = BuggyConfig.isPrintSuite();
        try {
            assertThat(printSuite, is(false));
            BuggyConfig.setPrintSuite(true);
            assertThat(BuggyConfig.isPrintSuite(), is(true));
        } finally {
            BuggyConfig.setPrintSuite(printSuite);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setPrintCause(Boolean printCause)")
    void unitTest_20180919201754() {
        boolean printCause = BuggyConfig.isPrintCause();
        try {
            assertThat(printCause, is(false));
            BuggyConfig.setPrintCause(true);
            assertThat(BuggyConfig.isPrintCause(), is(true));
        } finally {
            BuggyConfig.setPrintCause(printCause);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setPrintLogFile(Boolean printLog)")
    void unitTest_20180919201844() {
        boolean printLogFile = BuggyConfig.isPrintLog();
        try {
            assertThat(printLogFile, is(false));
            BuggyConfig.setPrintLog(true);
            assertThat(BuggyConfig.isPrintLog(), is(true));
        } finally {
            BuggyConfig.setPrintLog(printLogFile);
        }
    }

    @Test
    @DisplayName("Check PrimaryConfig.setVersion(Boolean version)")
    void unitTest_20180919201933() {
        assertThat(BuggyConfig.isVersion(), is(false));
        BuggyConfig.setVersion(true);
        assertThat(BuggyConfig.isVersion(), is(false));
    }
//
//    @Test
//    @DisplayName("GIVEN PrimaryConfig WHEN setCheck() THEN isCheck()")
//    void unitTest_20181020063716() {
//        boolean check = BuggyConfig.isCheck();
//        try {
//            assertThat(check, is(false));
//            BuggyConfig.setCheck(true);
//            assertThat(BuggyConfig.isCheck(), is(true));
//        } finally {
//            BuggyConfig.setCheck(check);
//        }
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.setAbsoluteLogPath(String path)")
//    void unitTest_20180919202117() {
//        String absolutePath = BuggyConfig.getAbsoluteLogPath();
//        try {
//            assertThat(absolutePath, is(WASTE));
//            BuggyConfig.setAbsoluteLogPath(WASTE + "/AbsolutePath");
//            assertThat(BuggyConfig.getAbsoluteLogPath(), is(WASTE + "/AbsolutePath"));
//        } finally {
//            BuggyConfig.setAbsoluteLogPath(absolutePath);
//        }
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.getErrorLogDir()")
//    void unitTest_20180919202645() {
//        assertThat(BuggyConfig.getErrorLogDir(), is(new File(WASTE, "errors")));
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.getTestLogDir()")
//    void unitTest_20180919202812() {
//        assertThat(BuggyConfig.getTestLogDir(), is(new File(WASTE, "tests")));
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.getFixedLogDir()")
//    void unitTest_20180919202835() {
//        assertThat(BuggyConfig.getFixedLogDir(), is(new File(WASTE, "fixed")));
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.getImplementedLogDir()")
//    void unitTest_20180919202856() {
//        assertThat(BuggyConfig.getImplementedLogDir(), is(new File(WASTE, "implemented")));
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.getNewErrorLogDir()")
//    void unitTest_20180919202909() {
//        assertThat(BuggyConfig.getNewErrorLogDir(), is(new File(WASTE, "errors/new")));
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.getExpFixErrorLogDir()")
//    void unitTest_20180919202937() {
//        assertThat(BuggyConfig.getExpFixErrorLogDir(), is(new File(WASTE, "errors/exp_fix")));
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.getExpImplErrorLogDir()")
//    void unitTest_20180919202959() {
//        assertThat(BuggyConfig.getExpImplErrorLogDir(), is(new File(WASTE, "errors/exp_impl")));
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.getCorruptedErrorLogDir()")
//    void unitTest_20180919203022() {
//        assertThat(BuggyConfig.getCorruptedErrorLogDir(), is(new File(WASTE, "errors/corrupted")));
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.getBlockedErrorLogDir()")
//    void unitTest_20180919203048() {
//        assertThat(BuggyConfig.getBlockedErrorLogDir(), is(new File(WASTE, "errors/blocked")));
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.")
//    void unitTest_20180919204139() {
//        String p = PrimaryConfig.configurationToString(BuggyConfig);
//        assertThat(p.contains("[--all]"), is(false));
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.configurationToString(config) with PrintAllParameters(false)")
//    void unitTest_20180919204525() {
//        boolean printAllParameters = BuggyConfig.isPrintAllParameters();
//        try {
//            BuggyConfig.setPrintAllParameters(true);
//            String p = PrimaryConfig.configurationToString(BuggyConfig);
//            assertThat(p.contains("[--all]"), is(true));
//        } finally {
//            BuggyConfig.setPrintAllParameters(printAllParameters);
//        }
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.configurationToString(config) with PrintAllParameters(true)")
//    void unitTest_20180919205544() {
//        JCommanderPrimaryConfig config = new JCommanderPrimaryConfig();
//        config.setPrintAllParameters(true);
//        String p = PrimaryConfig.configurationToString(config);
//        assertThat(p.contains("[--phm]...................getPublicHiddenMethod"), is(true));
//        assertThat(p.contains("[--php]....................publcHiddenParameter"), is(true));
//        assertThat(p.contains("[--pm]..........................setPublicMethod"), is(true));
//        assertThat(p.contains("[--pp]...........................publcParameter"), is(true));
//        assertThat(p.contains("[--ppm]................................********"), is(true));
//        assertThat(p.contains("[--ppp]................................********"), is(true));
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.configurationToString(config) with private parameter")
//    void unitTest_20180919211539() {
//        JCommanderPrimaryConfigFailGetField config = new JCommanderPrimaryConfigFailGetField();
//        config.setPrintAllParameters(true);
//        PrimaryConfig.configurationToString(config);
//    }
//
//    @Test
//    @DisplayName("Check PrimaryConfig.")
//    void unitTest_20180919212056() {
//        JCommanderPrimaryConfigFailGetMethod config = new JCommanderPrimaryConfigFailGetMethod();
//        config.setPrintAllParameters(true);
//        PrimaryConfig.configurationToString(config);
//    }
//
//    @Test
//    @DisplayName("GIVEN private constructor WHEN new DefaultValues() THEN IllegalStateException")
//    void unitTest_20181028233416() throws Exception {
//        Constructor<PrimaryConfig.DefaultValues> constructor = PrimaryConfig.DefaultValues.class.getDeclaredConstructor();
//        constructor.setAccessible(true);
//        constructor.newInstance();
//    }
//
//    @Test
//    @DisplayName("GIVEN DefaultValueProvider WHEN any value THEN return null")
//    void unitTest_20181028233806() {
//        PrimaryConfig.DefaultValueProvider provider = new PrimaryConfig.DefaultValueProvider();
//        assertThat(provider.getDefaultValueFor(""), is(nullValue()));
//        assertThat(provider.getDefaultValueFor("-?"), is(nullValue()));
//        assertThat(provider.getDefaultValueFor(null), is(nullValue()));
//        assertThat(provider.getDefaultValueFor("unitTest_20181028233806"), is(nullValue()));
//    }
//
//    @Test
//    @DisplayName("GIVEN  WHEN  THEN")
//    void unitTest_20181029002410() {
//        boolean isPrintAllParameters = BuggyConfig.isPrintAllParameters();
//        try {
//            Map<String, Object> map = new HashMap<>();
//            JCommanderPrimaryConfigGetField config = new JCommanderPrimaryConfigGetField() {};
//            config.setPrintAllParameters(false);
//            PrimaryConfig.addFieldValuesToMap(config, map);
//            assertThat(map.get("[-ffff]"), is("publicParameter"));
//            assertThat(map.get("[-hhhh]"), is(not("publicHiddenParameter")));
//            assertThat(map.size(), is(1));
//            config.setPrintAllParameters(true);
//            map.clear();
//            PrimaryConfig.addFieldValuesToMap(config, map);
//            assertThat(map.get("[-ffff]"), is("publicParameter"));
//            assertThat(map.get("[-hhhh]"), is("publicHiddenParameter"));
//            assertThat(map.size(), is(2));
//        } finally {
//            BuggyConfig.setPrintAllParameters(isPrintAllParameters);
//        }
//    }

}
