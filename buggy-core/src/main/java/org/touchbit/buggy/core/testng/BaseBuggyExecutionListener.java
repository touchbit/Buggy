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

import org.slf4j.Logger;
import org.testng.IExecutionListener;
import org.touchbit.buggy.core.logback.ConsoleLogger;
import org.touchbit.buggy.core.logback.FrameworkLogger;
import org.touchbit.buggy.core.logback.SiftingTestLogger;
import org.touchbit.buggy.core.model.Buggy;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Created by Oleg Shaburov on 16.05.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseBuggyExecutionListener implements BuggyListener, IExecutionListener {

    protected Logger testLog;
    protected Logger frameworkLog;
    protected Logger consoleLog;

    @Override
    public void onExecutionStart() {
        if (testLog == null) {
            testLog = new SiftingTestLogger();
        }
        if (frameworkLog == null) {
            frameworkLog = new FrameworkLogger();
        }
        if (consoleLog == null) {
            consoleLog = new ConsoleLogger();
        }
    }

}
