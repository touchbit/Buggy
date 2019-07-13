package org.touchbit.buggy.core.tests.testng.listeners;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testng.ITestNGMethod;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.tests.BaseUnitTest;
import org.touchbit.buggy.core.testng.listeners.BaseBuggyExecutionListener;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Oleg Shaburov on 14.10.2018
 * shaburov.o.a@gmail.com
 */
class BaseBuggyExecutionListenerTests extends BaseUnitTest {

    @Test
    @DisplayName("Check get url log file where !getArtifactsUrl().endsWith(\"/\")")
    void unitTest_20181014215332() {
        String temp = Buggy.getPrimaryConfig().getArtifactsUrl();
        try {
            UnitTestBaseBuggyExecutionListener listener = new UnitTestBaseBuggyExecutionListener();
            Buggy.getPrimaryConfig().setArtifactsUrl("https://touchbit.org/artifacts");
            ITestNGMethod method = getMockITestNGMethod();
            String result = listener.getLogFilePath(method);
            assertThat(result, is("https://touchbit.org/artifacts/waste-unit-tests/tests/iTestResultMethodWithDetails.log"));
        } finally {
            Buggy.getPrimaryConfig().setArtifactsUrl(temp);
        }
    }

    @Test
    @DisplayName("Check get url log file where getArtifactsUrl().endsWith(\"/\")")
    void unitTest_20181014222846() {
        String temp = Buggy.getPrimaryConfig().getArtifactsUrl();
        try {
            UnitTestBaseBuggyExecutionListener listener = new UnitTestBaseBuggyExecutionListener();
            Buggy.getPrimaryConfig().setArtifactsUrl("https://touchbit.org/artifacts/");
            ITestNGMethod method = getMockITestNGMethod();
            String result = listener.getLogFilePath(method);
            assertThat(result, is("https://touchbit.org/artifacts/waste-unit-tests/tests/iTestResultMethodWithDetails.log"));
        } finally {
            Buggy.getPrimaryConfig().setArtifactsUrl(temp);
        }
    }

    @Test
    @DisplayName("Check get local log file if ArtifactsUrl == null")
    void unitTest_20181014223022() {
        String temp = Buggy.getPrimaryConfig().getArtifactsUrl();
        try {
            Buggy.getPrimaryConfig().setArtifactsUrl(null);
            UnitTestBaseBuggyExecutionListener listener = new UnitTestBaseBuggyExecutionListener();
            Buggy.getPrimaryConfig().setArtifactsUrl(null);
            ITestNGMethod method = getMockITestNGMethod();
            String result = listener.getLogFilePath(method);
            assertThat(result, is("file://" + Buggy.getPrimaryConfig().getAbsoluteLogPath() +
                    "/tests/iTestResultMethodWithDetails.log"));
        } finally {
            Buggy.getPrimaryConfig().setArtifactsUrl(temp);
        }
    }

    @Test
    @DisplayName("Check get local log file if ArtifactsUrl == \"null\"")
    void unitTest_20181016135040() {
        String temp = Buggy.getPrimaryConfig().getArtifactsUrl();
        try {
            Buggy.getPrimaryConfig().setArtifactsUrl("null");
            UnitTestBaseBuggyExecutionListener listener = new UnitTestBaseBuggyExecutionListener();
            Buggy.getPrimaryConfig().setArtifactsUrl(null);
            ITestNGMethod method = getMockITestNGMethod();
            String result = listener.getLogFilePath(method);
            assertThat(result, is("file://" + Buggy.getPrimaryConfig().getAbsoluteLogPath() +
                    "/tests/iTestResultMethodWithDetails.log"));
        } finally {
            Buggy.getPrimaryConfig().setArtifactsUrl(temp);
        }
    }

    private static class UnitTestBaseBuggyExecutionListener extends BaseBuggyExecutionListener {

        @Override
        public boolean isEnable() {
            return false;
        }

        @Override
        public String getLogFilePath(ITestNGMethod method) {
            return super.getLogFilePath(method);
        }

    }

}
