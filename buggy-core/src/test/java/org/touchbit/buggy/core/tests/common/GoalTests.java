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

package org.touchbit.buggy.core.tests.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.config.TestComponent;
import org.touchbit.buggy.core.config.TestInterface;
import org.touchbit.buggy.core.config.TestService;
import org.touchbit.buggy.core.goal.component.AllComponents;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by Oleg Shaburov on 19.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("Buggy goals tests")
class GoalTests {

    @Test
    @DisplayName("Check DefaultComponent")
    void unitTest_20180919222639() {
        AllComponents component = new AllComponents();
        assertThat(component.getName(), is("DEFAULT"));
        assertThat(component.toString(), is("DEFAULT"));
        assertThat(component.getDescription(), is("Default component"));
    }

    @Test
    @DisplayName("Check Component")
    void unitTest_20180919223019() {
        TestComponent component = new TestComponent();
        assertThat(component.getName(), is("TESTCOMPONENT"));
        assertThat(component.toString(), is("TESTCOMPONENT"));
        assertThat(component.getDescription(), is("TestComponent"));
    }

    @Test
    @DisplayName("Check Service")
    void unitTest_20180919223411() {
        TestService service = new TestService();
        assertThat(service.getName(), is("TESTSERVICE"));
        assertThat(service.toString(), is("TESTSERVICE"));
        assertThat(service.getDescription(), is("TestService"));
    }

    @Test
    @DisplayName("Check Interface")
    void unitTest_20180919223449() {
        TestInterface testInterface = new TestInterface();
        assertThat(testInterface.getName(), is("TESTINTERFACE"));
        assertThat(testInterface.toString(), is("TESTINTERFACE"));
        assertThat(testInterface.getDescription(), is("TestInterface"));
    }

}
