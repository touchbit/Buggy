package org.touchbit.buggy.core.testng.listeners;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testng.ITestNGMethod;
import org.touchbit.buggy.core.BaseUnitTest;

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
        String temp = PRIMARY_CONFIG.getArtifactsUrl();
        try {
            BaseBuggyExecutionListener listener = new BaseBuggyExecutionListener() {
                @Override
                public boolean isEnable() {
                    return false;
                }
            };
            PRIMARY_CONFIG.setArtifactsUrl("https://touchbit.org/artifacts");
            ITestNGMethod method = getMockITestNGMethod();
            String result = listener.getURLEncodedLogFilePath(method);
            assertThat(result, is(" \u2B9E https://touchbit.org/artifacts/logs/tests/iTestResultMethodWithDetails.log"));
        } finally {
            PRIMARY_CONFIG.setArtifactsUrl(temp);
        }
    }

    @Test
    @DisplayName("Check get url log file where getArtifactsUrl().endsWith(\"/\")")
    void unitTest_20181014222846() {
        String temp = PRIMARY_CONFIG.getArtifactsUrl();
        try {
            BaseBuggyExecutionListener listener = new BaseBuggyExecutionListener() {
                @Override
                public boolean isEnable() {
                    return false;
                }
            };
            PRIMARY_CONFIG.setArtifactsUrl("https://touchbit.org/artifacts/");
            ITestNGMethod method = getMockITestNGMethod();
            String result = listener.getURLEncodedLogFilePath(method);
            assertThat(result, is(" \u2B9E https://touchbit.org/artifacts/logs/tests/iTestResultMethodWithDetails.log"));
        } finally {
            PRIMARY_CONFIG.setArtifactsUrl(temp);
        }
    }

    @Test
    @DisplayName("Check get local log file if ArtifactsUrl == null")
    void unitTest_20181014223022() {
        String temp = PRIMARY_CONFIG.getArtifactsUrl();
        try {
            PRIMARY_CONFIG.setArtifactsUrl(null);
            BaseBuggyExecutionListener listener = new BaseBuggyExecutionListener() {
                @Override
                public boolean isEnable() {
                    return false;
                }
            };
            PRIMARY_CONFIG.setArtifactsUrl(null);
            ITestNGMethod method = getMockITestNGMethod();
            String result = listener.getURLEncodedLogFilePath(method);
            assertThat(result, is(" \u2B9E file://" + PRIMARY_CONFIG.getAbsoluteLogPath() +
                    "/tests/iTestResultMethodWithDetails.log"));
        } finally {
            PRIMARY_CONFIG.setArtifactsUrl(temp);
        }
    }

    @Test
    @DisplayName("Check get local log file if ArtifactsUrl == \"null\"")
    void unitTest_20181016135040() {
        String temp = PRIMARY_CONFIG.getArtifactsUrl();
        try {
            PRIMARY_CONFIG.setArtifactsUrl("null");
            BaseBuggyExecutionListener listener = new BaseBuggyExecutionListener() {
                @Override
                public boolean isEnable() {
                    return false;
                }
            };
            PRIMARY_CONFIG.setArtifactsUrl(null);
            ITestNGMethod method = getMockITestNGMethod();
            String result = listener.getURLEncodedLogFilePath(method);
            assertThat(result, is(" \u2B9E file://" + PRIMARY_CONFIG.getAbsoluteLogPath() +
                    "/tests/iTestResultMethodWithDetails.log"));
        } finally {
            PRIMARY_CONFIG.setArtifactsUrl(temp);
        }
    }

}
