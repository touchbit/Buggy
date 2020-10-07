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

import org.testng.IInvokedMethod;
import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.touchbit.buggy.core.logback.SiftingTestLogger;
import org.touchbit.buggy.core.utils.JUtils;

/**
 * Intellij IDEA TestNG Plugin Listener
 * <p>
 * Created by Oleg Shaburov on 15.05.2018
 * shaburov.o.a@gmail.com
 */
public final class IntellijIdeaTestNgPluginListener extends LoggingListener {

    public IntellijIdeaTestNgPluginListener() {
        if (JUtils.isJetBrainsIdeTestNGPluginRun()) {
            JUtils.initBuggyConfigurationYml();
        }
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (JUtils.isJetBrainsIdeTestNGPluginRun()) {
            super.beforeInvocation(method, testResult);
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (JUtils.isJetBrainsIdeTestNGPluginRun()) {
            super.afterInvocation(method, testResult);
        }
    }

    @Override
    public final boolean isEnable() {
        return JUtils.isJetBrainsIdeTestNGPluginRun();
    }

}