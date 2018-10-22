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
import org.touchbit.buggy.core.testng.listeners.IntellijIdeaTestNgPluginListener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.touchbit.buggy.core.testng.listeners.IntellijIdeaTestNgPluginListener.INTELLIJ_IDEA_TEST_RUN;

/**
 * Created by Oleg Shaburov on 20.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("IntellijIdeaTestNgPluginListener class tests")
class IntellijIdeaTestNgPluginListenerTests extends BaseUnitTest {

    @Test
    @DisplayName("Check onExecutionStart()")
    void unitTest_20180920223244() {
        try {
            IntellijIdeaTestNgPluginListener listener = new IntellijIdeaTestNgPluginListener();
            listener.onExecutionStart();
            assertThat(listener.isEnable(), is(false));
        } finally {
            System.setProperty(INTELLIJ_IDEA_TEST_RUN, Boolean.FALSE.toString());
        }
    }

}
