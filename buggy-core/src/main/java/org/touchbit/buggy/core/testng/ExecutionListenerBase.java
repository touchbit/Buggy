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

import org.testng.*;

/**
 * Listener for processing executable tests.
 * <p>
 * Created by Shaburov Oleg on 31.07.2017.
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "squid:S2629"})
public abstract class ExecutionListenerBase implements BuggyListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        suite.getAllMethods().forEach(this::disableTestsByType);
        suite.getAllMethods().forEach(this::disableByTestStatus);
    }

    private void disableTestsByType(ITestNGMethod method) {
        if (isSkipByType(method)) {
            method.setInvocationCount(0);
        }
    }

    public void disableByTestStatus(ITestNGMethod method) {
        if (isSkipByTestStatus(method)) {
            method.setInvocationCount(0);
        }
    }

}
