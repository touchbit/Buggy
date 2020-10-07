package org.touchbit.buggy.core.tests.testng;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testng.ITestNGMethod;
import org.touchbit.buggy.core.config.BuggyConfig;
import org.touchbit.buggy.core.testng.BaseBuggyExecutionListener;
import org.touchbit.buggy.core.tests.BaseUnitTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Oleg Shaburov on 14.10.2018
 * shaburov.o.a@gmail.com
 */
class BaseBuggyExecutionListenerTests extends BaseUnitTest {
//
//    @Test
//    @DisplayName("Check get url log file where !getArtifactsUrl().endsWith(\"/\")")
//    void unitTest_20181014215332() {
//        String temp = BuggyConfig.getArtifactsUrl();
//        try {
//            UnitTestBaseBuggyExecutionListener listener = new UnitTestBaseBuggyExecutionListener();
//            BuggyConfig.setArtifactsUrl("https://touchbit.org/artifacts");
//            ITestNGMethod method = getMockITestNGMethod();
//            String result = listener.getLogFilePath(method);
//            assertThat(result, is("https://touchbit.org/artifacts/waste-unit-tests/tests/iTestResultMethodWithDetails.log"));
//        } finally {
//            BuggyConfig.setArtifactsUrl(temp);
//        }
//    }
//
//    @Test
//    @DisplayName("Check get url log file where getArtifactsUrl().endsWith(\"/\")")
//    void unitTest_20181014222846() {
//        String temp = BuggyConfig.getArtifactsUrl();
//        try {
//            UnitTestBaseBuggyExecutionListener listener = new UnitTestBaseBuggyExecutionListener();
//            BuggyConfig.setArtifactsUrl("https://touchbit.org/artifacts/");
//            ITestNGMethod method = getMockITestNGMethod();
//            String result = listener.getLogFilePath(method);
//            assertThat(result, is("https://touchbit.org/artifacts/waste-unit-tests/tests/iTestResultMethodWithDetails.log"));
//        } finally {
//            BuggyConfig.setArtifactsUrl(temp);
//        }
//    }
//
//    @Test
//    @DisplayName("Check get local log file if ArtifactsUrl == null")
//    void unitTest_20181014223022() {
//        String temp = BuggyConfig.getArtifactsUrl();
//        try {
//            BuggyConfig.setArtifactsUrl(null);
//            UnitTestBaseBuggyExecutionListener listener = new UnitTestBaseBuggyExecutionListener();
//            BuggyConfig.setArtifactsUrl(null);
//            ITestNGMethod method = getMockITestNGMethod();
//            String result = listener.getLogFilePath(method);
//            assertThat(result, is("file://" + BuggyConfig.getAbsoluteLogPath() +
//                    "/tests/iTestResultMethodWithDetails.log"));
//        } finally {
//            BuggyConfig.setArtifactsUrl(temp);
//        }
//    }
//
//    @Test
//    @DisplayName("Check get local log file if ArtifactsUrl == \"null\"")
//    void unitTest_20181016135040() {
//        String temp = BuggyConfig.getArtifactsUrl();
//        try {
//            BuggyConfig.setArtifactsUrl("null");
//            UnitTestBaseBuggyExecutionListener listener = new UnitTestBaseBuggyExecutionListener();
//            BuggyConfig.setArtifactsUrl(null);
//            ITestNGMethod method = getMockITestNGMethod();
//            String result = listener.getLogFilePath(method);
//            assertThat(result, is("file://" + BuggyConfig.getAbsoluteLogPath() +
//                    "/tests/iTestResultMethodWithDetails.log"));
//        } finally {
//            BuggyConfig.setArtifactsUrl(temp);
//        }
//    }

    private static class UnitTestBaseBuggyExecutionListener extends BaseBuggyExecutionListener {

        @Override
        public boolean isEnable() {
            return false;
        }

//        @Override
//        public String getLogFilePath(ITestNGMethod method, S) {
//            return super.getLogFilePath(method, null);
//        }

    }

}
