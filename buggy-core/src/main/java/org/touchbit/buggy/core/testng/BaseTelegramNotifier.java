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

package org.touchbit.buggy.core.testng;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.testng.IExecutionListener;
import org.touchbit.buggy.core.config.BuggyConfig;
import org.touchbit.buggy.core.model.Notifier;
import org.touchbit.buggy.core.utils.StringUtils;

import java.util.StringJoiner;

/**
 * Created by Oleg Shaburov on 06.09.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings({"unused", "squid:S2629"})
public abstract class BaseTelegramNotifier extends BaseBuggyExecutionListener implements IExecutionListener {

    private final Notifier notifier;

    @SuppressWarnings("WeakerAccess")
    public BaseTelegramNotifier(final Notifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void onExecutionStart() {
    }
//
//    @Override
//    public void onExecutionFinish() {
//        if (isEnable()) {
//            telegramResultNotification();
//        }
//    }
//
//    private void telegramResultNotification() {
//        String name = BuggyConfig.getProgramName();
//        StringJoiner sj;
//        if (name == null || name.isEmpty()) {
//            sj = new StringJoiner("\n", "Run Results:\n", "\n");
//        } else {
//            sj = new StringJoiner("\n", "*" + name + "*\nRun Results:\n", "\n");
//        }
//        int len = 33;
//        int errorCount = expFixError.get() + expImplError.get() + newError.get() +
//                corruptedError.get() + blockedError.get();
//        int testCountLen = testCount.toString().length();
//        int fullLen = len + testCountLen;
//        sj.add(StringUtils.filler("`", "-", fullLen, "`"));
//        int runningLen = len + testCountLen - testCount.toString().length();
//        sj.add(StringUtils.dotFiller("`Running tests", runningLen, "`") + testCount.get());
//        sj.add(StringUtils.filler("`", "-", fullLen, "`"));
//        int successfulLen = len + testCountLen - (String.valueOf(testCount.get() - errorCount)).length();
//        sj.add(StringUtils.dotFiller("`Successful tests", successfulLen, "`") +
//                (testCount.get() - errorCount));
//        sj.add(StringUtils.dotFiller("`Skipped tests", successfulLen, "`") + (skippedTests.get()));
//        int failedLen = len + testCountLen - (String.valueOf(errorCount)).length();
//        sj.add(StringUtils.dotFiller("`Failed tests", failedLen, "`") + errorCount);
//        sj.add(StringUtils.filler("`", "-", fullLen, "`"));
//        int newErrorsLen = len + testCountLen - newError.toString().length();
//        sj.add(StringUtils.dotFiller("`New Errors", newErrorsLen, "`") +
//                wrapErrorsMDLink(newError.get(), "new/"));
//        int waitFixLen = len + testCountLen - expFixError.toString().length();
//        sj.add(StringUtils.dotFiller("`Waiting to fix a defect", waitFixLen, "`") +
//                wrapErrorsMDLink(expFixError.get(), "exp_fix/"));
//        int waitImplLen = len + testCountLen - expImplError.toString().length();
//        sj.add(StringUtils.dotFiller("`Waiting for the implementation", waitImplLen, "`") +
//                wrapErrorsMDLink(expImplError.get(), "exp_impl/"));
//        int blockedLen = len + testCountLen - blockedError.toString().length();
//        sj.add(StringUtils.dotFiller("`Blocked tests", blockedLen, "`") +
//                wrapErrorsMDLink(blockedError.get(), "blocked/"));
//        int corruptedLen = len + testCountLen - corruptedError.toString().length();
//        sj.add(StringUtils.dotFiller("`Corrupted tests", corruptedLen, "`") +
//                wrapErrorsMDLink(corruptedError.get(), "corrupted/"));
//        sj.add(StringUtils.filler("`", "-", fullLen, "`"));
//        int fixedLen = len + testCountLen - fixed.toString().length();
//        sj.add(StringUtils.dotFiller("`Fixed defects", fixedLen, "`") +
//                wrapFixedMDLink(fixed.get(), "exp_fix/"));
//        int implementedLen = len + testCountLen - implemented.toString().length();
//        sj.add(StringUtils.dotFiller("`Implemented cases", implementedLen, "`") +
//                wrapFixedMDLink(implemented.get(), "exp_impl/"));
//        sj.add(StringUtils.filler("`", "-", fullLen, "`"));
//        String date = DurationFormatUtils.formatDuration(finishTime - startTime, "HH:mm:ss");
//        sj.add("Test execution time: *" + date + "*");
//        sj.add("[Logs](" + BuggyConfig.getArtifactsUrl() + ")");
//        try {
//            notifier.report(sj.toString());
//        } catch (Exception e) {
////            BuggyLoggers.CONSOLE.error("Failed to send message to Telegram.", e); TODO
//        }
//    }
//
//    private String wrapErrorsMDLink(Integer value, String subLink) {
//        String artifactLogPath = BuggyConfig.getArtifactsUrl() + "/errors/" + subLink;
//        if (value > 0) {
//            return "[" + value + "](" + artifactLogPath + ")";
//        }
//        return String.valueOf(value);
//    }
//
//    private String wrapFixedMDLink(Integer value, String subLink) {
//        String artifactLogPath = BuggyConfig.getArtifactsUrl() + "/fixed/" + subLink;
//        if (value > 0) {
//            return "[" + value + "](" + artifactLogPath + ")";
//        }
//        return String.valueOf(value);
//    }

}
