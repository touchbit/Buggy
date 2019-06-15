package org.touchbit.buggy.testrail;

import com.beust.jcommander.Parameter;

import static org.touchbit.buggy.testrail.BTRParameters.*;

/**
 * Created by Oleg Shaburov on 16.05.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings({"squid:S1118", "WeakerAccess", "squid:S1214"})
public interface BaseTestRailConfig {

    class DefaultValues {

        private static Boolean testRailEnable = false;
        private static String testRailHost = "https://testrail.example";
        private static String testRailLogin = "";
        private static String testRailPassToken = "";

        /** Utility class. Prohibit instantiation. */
        private DefaultValues() { }
    }

    @Parameter(names = {ENABLE}, description = "Flag of translation results in TestRail")
    static void setTestRailEnable(Boolean testRailEnable) {
        DefaultValues.testRailEnable = testRailEnable;
    }

    @Parameter(names = {HOST}, hidden = true, description = "TestRail server")
    static void setTestRailHost(String testRailHost) {
        DefaultValues.testRailHost = testRailHost;
    }

    @Parameter(names = {LOGIN}, hidden = true, password = true, description = "TestRail base64 auth")
    static void setLogin(String testRailLogin) {
        DefaultValues.testRailLogin = testRailLogin;
    }

    @Parameter(names = {PASS_TOKEN}, hidden = true, description = "TestRail user password or api token")
    static void setPass(String testRailPassToken) {
        DefaultValues.testRailPassToken = testRailPassToken;
    }

    static Boolean isTestRailEnable() {
        return DefaultValues.testRailEnable;
    }

    static String getTestRailHost() {
        return DefaultValues.testRailHost;
    }

    static String getLogin() {
        return DefaultValues.testRailLogin;
    }

    static String getPass() {
        return DefaultValues.testRailPassToken;
    }

}
