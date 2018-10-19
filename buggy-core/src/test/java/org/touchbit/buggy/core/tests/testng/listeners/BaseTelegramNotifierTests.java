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

package org.touchbit.buggy.core.tests.testng.listeners;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.tests.BaseUnitTest;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.exceptions.CorruptedTestException;
import org.touchbit.buggy.core.model.Notifier;
import org.touchbit.buggy.core.testng.listeners.BaseTelegramNotifier;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Oleg Shaburov on 20.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("BaseTelegramNotifier class tests")
class BaseTelegramNotifierTests extends BaseUnitTest {

    @Test
    @DisplayName("Check onExecutionFinish report")
    void unitTest_20180920225957() {
        StringBuilder sb = new StringBuilder();
        Notifier notifier = sb::append;
        BaseTelegramNotifier telegram = new BaseTelegramNotifier(notifier) {
            @Override
            public boolean isEnable() {
                testCount.set(0);
                skippedTests.set(0);
                corruptedError.set(0);
                expFixError.set(0);
                expImplError.set(0);
                blockedError.set(0);
                newError.set(0);
                fixed.set(0);
                implemented.set(0);
                return true;
            }
        };
        telegram.onExecutionFinish();
        assertThat(sb.toString(), is("" +
                "*Buggy*\n" +
                "Run Results:\n" +
                "`--------------------------------`\n" +
                "`Running tests..................`0\n" +
                "`--------------------------------`\n" +
                "`Successful tests...............`0\n" +
                "`Skipped tests..................`0\n" +
                "`Failed tests...................`0\n" +
                "`--------------------------------`\n" +
                "`New Errors.....................`0\n" +
                "`Waiting to fix a defect........`0\n" +
                "`Waiting for the implementation.`0\n" +
                "`Blocked tests..................`0\n" +
                "`Corrupted tests................`0\n" +
                "`--------------------------------`\n" +
                "`Fixed defects..................`0\n" +
                "`Implemented cases..............`0\n" +
                "`--------------------------------`\n" +
                "Test execution time: *00:00:00*\n" +
                "[Logs](null)\n"));
        Buggy.setProgramName("");
        sb = new StringBuilder();
        notifier = sb::append;
        telegram = new BaseTelegramNotifier(notifier) {
            @Override
            public boolean isEnable() {
                testCount.set(0);
                skippedTests.set(0);
                corruptedError.set(0);
                expFixError.set(0);
                expImplError.set(0);
                blockedError.set(0);
                newError.set(0);
                fixed.set(0);
                implemented.set(0);
                return true;
            }
        };
        telegram.onExecutionFinish();
        assertThat(sb.toString(), is("" +
                "Run Results:\n" +
                "`--------------------------------`\n" +
                "`Running tests..................`0\n" +
                "`--------------------------------`\n" +
                "`Successful tests...............`0\n" +
                "`Skipped tests..................`0\n" +
                "`Failed tests...................`0\n" +
                "`--------------------------------`\n" +
                "`New Errors.....................`0\n" +
                "`Waiting to fix a defect........`0\n" +
                "`Waiting for the implementation.`0\n" +
                "`Blocked tests..................`0\n" +
                "`Corrupted tests................`0\n" +
                "`--------------------------------`\n" +
                "`Fixed defects..................`0\n" +
                "`Implemented cases..............`0\n" +
                "`--------------------------------`\n" +
                "Test execution time: *00:00:00*\n" +
                "[Logs](null)\n"));
    }

    @Test
    @DisplayName("Check report with program name")
    void unitTest_20180920231126() {
        StringBuilder sb = new StringBuilder();
        Buggy.setProgramName("unitTest_20180920231126");
        Notifier notifier = sb::append;
        BaseTelegramNotifier telegram = new BaseTelegramNotifier(notifier) {
            @Override
            public boolean isEnable() {
                testCount.set(0);
                skippedTests.set(0);
                corruptedError.set(0);
                expFixError.set(0);
                expImplError.set(0);
                blockedError.set(0);
                newError.set(0);
                fixed.set(0);
                implemented.set(0);
                return true;
            }
        };
        telegram.onExecutionFinish();
        assertThat(sb.toString(), is("" +
                "*unitTest_20180920231126*\n" +
                "Run Results:\n" +
                "`--------------------------------`\n" +
                "`Running tests..................`0\n" +
                "`--------------------------------`\n" +
                "`Successful tests...............`0\n" +
                "`Skipped tests..................`0\n" +
                "`Failed tests...................`0\n" +
                "`--------------------------------`\n" +
                "`New Errors.....................`0\n" +
                "`Waiting to fix a defect........`0\n" +
                "`Waiting for the implementation.`0\n" +
                "`Blocked tests..................`0\n" +
                "`Corrupted tests................`0\n" +
                "`--------------------------------`\n" +
                "`Fixed defects..................`0\n" +
                "`Implemented cases..............`0\n" +
                "`--------------------------------`\n" +
                "Test execution time: *00:00:00*\n" +
                "[Logs](null)\n"));
    }

    @Test
    @DisplayName("Check report with ArtifactsUrl")
    void unitTest_20180920231553() {
        StringBuilder sb = new StringBuilder();
        Buggy.setProgramName("unitTest_20180920231553");
        Notifier notifier = sb::append;
        Buggy.getPrimaryConfig().setArtifactsUrl("http://build.url");
        try {
            BaseTelegramNotifier telegram = new BaseTelegramNotifier(notifier) {
                @Override
                public boolean isEnable() {
                    testCount.set(6);
                    skippedTests.set(1);
                    corruptedError.incrementAndGet();
                    expFixError.incrementAndGet();
                    expImplError.incrementAndGet();
                    blockedError.incrementAndGet();
                    newError.incrementAndGet();
                    fixed.incrementAndGet();
                    implemented.incrementAndGet();
                    return true;
                }
            };
            telegram.onExecutionFinish();
            assertThat(sb.toString(), is("*unitTest_20180920231553*\n" +
                    "Run Results:\n" +
                    "`--------------------------------`\n" +
                    "`Running tests..................`6\n" +
                    "`--------------------------------`\n" +
                    "`Successful tests...............`1\n" +
                    "`Skipped tests..................`1\n" +
                    "`Failed tests...................`5\n" +
                    "`--------------------------------`\n" +
                    "`New Errors.....................`[1](http://build.url/errors/new/)\n" +
                    "`Waiting to fix a defect........`[1](http://build.url/errors/exp_fix/)\n" +
                    "`Waiting for the implementation.`[1](http://build.url/errors/exp_impl/)\n" +
                    "`Blocked tests..................`[1](http://build.url/errors/blocked/)\n" +
                    "`Corrupted tests................`[1](http://build.url/errors/corrupted/)\n" +
                    "`--------------------------------`\n" +
                    "`Fixed defects..................`[1](http://build.url/fixed/exp_fix/)\n" +
                    "`Implemented cases..............`[1](http://build.url/fixed/exp_impl/)\n" +
                    "`--------------------------------`\n" +
                    "Test execution time: *00:00:00*\n" +
                    "[Logs](http://build.url)\n"));
        } finally {
            Buggy.getPrimaryConfig().setArtifactsUrl(null);
        }
    }

    @Test
    @DisplayName("Check failed notification")
    void unitTest_20180920232942() {
        int errors = Buggy.getBuggyErrors();
        Notifier notifier = msg -> {
            throw new CorruptedTestException("unitTest_20180920232942");
        };
        BaseTelegramNotifier telegram = new BaseTelegramNotifier(notifier) {
            @Override
            public boolean isEnable() { return true; }
        };
        telegram.onExecutionStart();
        await().atMost(50, MILLISECONDS);
        telegram.onExecutionFinish();
        assertThat(Buggy.getBuggyErrors(), is((errors + 1)));
    }

    @Test
    @DisplayName("Check disable notification")
    void unitTest_20180920230951() {
        StringBuilder sb = new StringBuilder();
        Notifier notifier = sb::append;
        BaseTelegramNotifier telegram = new BaseTelegramNotifier(notifier) {
            @Override
            public boolean isEnable() { return false; }
        };
        telegram.onExecutionStart();
        telegram.onExecutionFinish();
        assertThat(sb.toString(), is(""));
    }

}
