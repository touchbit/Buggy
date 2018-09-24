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

package org.touchbit.buggy.core.testng.listeners;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testng.IInvokedMethod;
import org.testng.ITestNGMethod;
import org.touchbit.buggy.core.BaseUnitTest;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Oleg Shaburov on 20.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("TestSupervisor class tests")
class TestSupervisorTests extends BaseUnitTest {

    @Test
    @DisplayName("Check TestSupervisor() constructor")
    void unitTest_20180920214207() {
        TestSupervisor supervisor = new TestSupervisor();
        assertThat(supervisor.isRunMetronome(), is(false));
        assertThat(supervisor.isEnable(), is(true));
    }

    @Test
    @DisplayName("Check TestSupervisor(long tact)")
    void unitTest_20180920214822() {
        TestSupervisor supervisor = new TestSupervisor(10);
        assertThat(supervisor.isRunMetronome(), is(false));
        assertThat(supervisor.isEnable(), is(true));
    }

    @Test
    @DisplayName("Check TestSupervisor onExecutionStart & onExecutionFinish")
    void unitTest_20180920214841() {
        TestSupervisor supervisor = new TestSupervisor();
        try {
            supervisor.onExecutionStart();
            assertThat(supervisor.isRunMetronome(), is(true));
        } finally {
            supervisor.onExecutionFinish();
            assertThat(supervisor.isRunMetronome(), is(false));
        }
    }

    @Test
    @DisplayName("Check beforeInvocation & afterInvocation")
    void unitTest_20180920215058() {
        TestSupervisor supervisor = new TestSupervisor();
        ITestNGMethod iTestNGMethod = mock(ITestNGMethod.class);
        when(iTestNGMethod.getMethodName()).thenReturn("method");
        IInvokedMethod iInvokedMethod = mock(IInvokedMethod.class);
        when(iInvokedMethod.getTestMethod()).thenReturn(iTestNGMethod);
        when(iTestNGMethod.getRealClass()).thenReturn(TestSupervisor.class);
        try {
            supervisor.onExecutionStart();
            supervisor.beforeInvocation(iInvokedMethod, null);
            assertThat(supervisor.getExecutableTests(), contains("method (TestSupervisor)"));
            supervisor.afterInvocation(iInvokedMethod, null);
            assertThat(supervisor.getExecutableTests(), is(empty()));
            assertThat(supervisor.isRunMetronome(), is(true));
        } finally {
            supervisor.onExecutionFinish();
            assertThat(supervisor.isRunMetronome(), is(false));
        }
    }

    @Test
    @DisplayName("Check test duplicates")
    void unitTest_20180920220236() {
        TestSupervisor supervisor = new TestSupervisor();
        ITestNGMethod iTestNGMethod = mock(ITestNGMethod.class);
        when(iTestNGMethod.getMethodName()).thenReturn("method");
        IInvokedMethod iInvokedMethod = mock(IInvokedMethod.class);
        when(iInvokedMethod.getTestMethod()).thenReturn(iTestNGMethod);
        when(iTestNGMethod.getRealClass()).thenReturn(TestSupervisor.class);
        try {
            supervisor.onExecutionStart();
            supervisor.beforeInvocation(iInvokedMethod, null);
            assertThat(supervisor.getExecutableTests().size(), is(1));
            assertThat(supervisor.getExecutableTests(), contains("method (TestSupervisor)"));
            supervisor.beforeInvocation(iInvokedMethod, null);
            assertThat(supervisor.getExecutableTests().size(), is(1));
            assertThat(supervisor.getExecutableTests(), contains("method (TestSupervisor)"));
            supervisor.afterInvocation(iInvokedMethod, null);
            assertThat(supervisor.getExecutableTests(), is(empty()));
            supervisor.afterInvocation(iInvokedMethod, null);
            assertThat(supervisor.getExecutableTests(), is(empty()));
            assertThat(supervisor.isRunMetronome(), is(true));
        } finally {
            supervisor.onExecutionFinish();
            assertThat(supervisor.isRunMetronome(), is(false));
        }
    }

    @Test
    @DisplayName("Check onExecutionFinish")
    void unitTest_20180920220448() {
        TestSupervisor supervisor = new TestSupervisor();
        supervisor.onExecutionFinish();
        assertThat(supervisor.isRunMetronome(), is(false));
    }

    @Test
    @DisplayName("Check Metronome wait")
    void unitTest_20180920220626() {
        TestSupervisor supervisor = new TestSupervisor(1);
        try {
            supervisor.onExecutionStart();
            await().atMost(100, MILLISECONDS);
            assertThat(supervisor.isRunMetronome(), is(true));
        } finally {
            supervisor.onExecutionFinish();
            assertThat(supervisor.isRunMetronome(), is(false));
        }
    }

    @Test
    @DisplayName("Check incorrect tact")
    void unitTest_20180920221236() {
        BuggyConfigurationException e = execute(() -> new TestSupervisor(0), BuggyConfigurationException.class);
        assertThat(e.getMessage(), is("TestSupervisor metronome tact can not be less than 1 ms. Received: 0"));
    }

}
