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

package org.touchbit.buggy.core;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.testng.annotations.Listeners;
import org.touchbit.buggy.core.log.BuggyLoggers;
import org.touchbit.buggy.core.testng.BuggyExecutionListener;
import org.touchbit.buggy.core.testng.IntellijIdeaTestNgPluginListener;

/**
 * Base class for tests.
 * <p>
 * Created by Oleg Shaburov on 17.05.2018
 * shaburov.o.a@gmail.com
 */
@Listeners(IntellijIdeaTestNgPluginListener.class)
public abstract class BaseBuggyTest {

    protected static Logger log;

    protected BaseBuggyTest() {
        this(BuggyLoggers.SIFTING);
    }

    protected BaseBuggyTest(final Logger logger) {
        setLog(logger);
    }

    /**
     * Method for the separation of steps in the test log.
     */
    protected static void step(@NotNull final String msg, @NotNull final Object... args) {
        BuggyExecutionListener.step(log, msg, args);
    }

    protected static void setLog(final Logger logger) {
        if (logger != null) {
            log = logger;
        }
    }

}
