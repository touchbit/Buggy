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
            String result = listener.getURLEncodedLogFilePath(method);
            assertThat(result, is(" \u2B9E https://touchbit.org/artifacts/waste-unit-tests/tests/iTestResultMethodWithDetails.log"));
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
            String result = listener.getURLEncodedLogFilePath(method);
            assertThat(result, is(" \u2B9E https://touchbit.org/artifacts/waste-unit-tests/tests/iTestResultMethodWithDetails.log"));
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
            String result = listener.getURLEncodedLogFilePath(method);
            assertThat(result, is(" \u2B9E file://" + Buggy.getPrimaryConfig().getAbsoluteLogPath() +
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
            String result = listener.getURLEncodedLogFilePath(method);
            assertThat(result, is(" \u2B9E file://" + Buggy.getPrimaryConfig().getAbsoluteLogPath() +
                    "/tests/iTestResultMethodWithDetails.log"));
        } finally {
            Buggy.getPrimaryConfig().setArtifactsUrl(temp);
        }
    }

    @Test
    @DisplayName("Check setArrow")
    void unitTest_20181019033011() {
        StringBuilder sb = new StringBuilder();
        Details details = getDetails(Status.FAILED, "BUG-123");
        new BaseBuggyExecutionListener() {
            @Override public boolean isEnable() { return false; }
            {
                setArrow(" ----------------> ");
                sb.append(buildDetailsMessage(details, "note"));
            }
        };
        assertThat(sb.toString(), is(" ----------------> [BUG-123] note"));
    }

    @Test
    @DisplayName("getInvokedMethodLogFileName when details contains test ids")
    void unitTest_20181019034500() {
        StringBuilder sb = new StringBuilder();
        Details details = getDetails(new int[]{1,2,3}, Status.FAILED, Type.SMOKE, "BUG-123");
        ITestNGMethod method = getMockITestNGMethod();
        new BaseBuggyExecutionListener() {
            @Override public boolean isEnable() { return false; }
            @Override protected Details getDetails(Method method) {
                return details;
            }
            {
                sb.append(getInvokedMethodLogFileName(method));
            }
        };
        assertThat(sb.toString(), is("[1, 2, 3]_iTestResultMethodWithDetails.log"));
    }

    private static class UnitTestBaseBuggyExecutionListener extends BaseBuggyExecutionListener {

        @Override
        public boolean isEnable() {
            return false;
        }

        @Override
        public String getURLEncodedLogFilePath(ITestNGMethod method) {
            return super.getURLEncodedLogFilePath(method);
        }

    }

}
