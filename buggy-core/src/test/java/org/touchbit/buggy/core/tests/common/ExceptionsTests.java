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
import org.touchbit.buggy.core.tests.BaseUnitTest;
import org.touchbit.buggy.core.exceptions.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by Oleg Shaburov on 19.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("Buggy exceptions tests")
class ExceptionsTests extends BaseUnitTest {

    @Test
    @DisplayName("Check AssertionException(String msg)")
    void unitTest_20180919214935() {
        AssertionException exception = new AssertionException("test");
        assertThat(exception.getMessage(), is("test"));
        assertThat(exception.getStackTrace(), is(emptyArray()));
        assertThat(exception.getCause(), is(nullValue()));
    }

    @Test
    @DisplayName("Check AssertionException(String msg, boolean suppressStacktrace)")
    void unitTest_20180919215112() {
        AssertionException exception = new AssertionException("test", false);
        assertThat(exception.getMessage(), is("test"));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), is(nullValue()));
    }

    @Test
    @DisplayName("Check AssertionException(String msg, Throwable e)")
    void unitTest_20180919215940() {
        AssertionException exception = new AssertionException("test", new Exception());
        assertThat(exception.getMessage(), is("test"));
        assertThat(exception.getStackTrace(), is(emptyArray()));
        assertThat(exception.getCause(), not(nullValue()));
    }

    @Test
    @DisplayName("Check AssertionException(String msg, Throwable e, boolean suppressStacktrace)")
    void unitTest_20180919220039() {
        AssertionException exception = new AssertionException("test", new Exception(), false);
        assertThat(exception.getMessage(), is("test"));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), not(nullValue()));
    }

    @Test
    @DisplayName("Check AssertionException(Throwable e)")
    void unitTest_20180919220130() {
        AssertionException exception = new AssertionException(new Exception());
        assertThat(exception.getMessage(), is(nullValue()));
        assertThat(exception.getStackTrace(), is(emptyArray()));
        assertThat(exception.getCause(), not(nullValue()));
    }

    @Test
    @DisplayName("Check AssertionException(Throwable e, boolean suppressStacktrace)")
    void unitTest_20180919220209() {
        AssertionException exception = new AssertionException(new Exception(), false);
        assertThat(exception.getMessage(), is(nullValue()));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), not(nullValue()));
    }

    @Test
    @DisplayName("Check BlockedTestException(String msg)")
    void unitTest_20180919220637() {
        BlockedTestException exception = new BlockedTestException("test");
        assertThat(exception.getMessage(), is("test"));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), is(nullValue()));
    }

    @Test
    @DisplayName("Check BlockedTestException(String msg, Throwable e)")
    void unitTest_20180919220724() {
        BlockedTestException exception = new BlockedTestException("test", new Exception());
        assertThat(exception.getMessage(), is("test"));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), not(nullValue()));
    }

    @Test
    @DisplayName("Check BlockedTestException(Throwable e)")
    void unitTest_20180919220750() {
        BlockedTestException exception = new BlockedTestException(new Exception());
        assertThat(exception.getMessage(), is(Exception.class.getTypeName()));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), not(nullValue()));
    }

    @Test
    @DisplayName("Check BuggyConfigurationException(String msg)")
    void unitTest_20180919220824() {
        BuggyConfigurationException exception = new BuggyConfigurationException("test");
        assertThat(exception.getMessage(), is("test"));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), is(nullValue()));
    }

    @Test
    @DisplayName("Check BuggyConfigurationException(String msg, Throwable e)")
    void unitTest_20180919220843() {
        BuggyConfigurationException exception = new BuggyConfigurationException("test", new Exception());
        assertThat(exception.getMessage(), is("test"));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), not(nullValue()));
    }

    @Test
    @DisplayName("Check BuggyConfigurationException(Throwable e)")
    void unitTest_20180919220908() {
        BuggyConfigurationException exception = new BuggyConfigurationException(new Exception());
        assertThat(exception.getMessage(), is(Exception.class.getTypeName()));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), not(nullValue()));
    }

    @Test
    @DisplayName("Check BuggyException(String msg)")
    void unitTest_20180919220939() {
        BuggyException exception = new BuggyException("test");
        assertThat(exception.getMessage(), is("test"));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), is(nullValue()));
    }

    @Test
    @DisplayName("Check BuggyException(String msg, Throwable e)")
    void unitTest_20180919221000() {
        BuggyException exception = new BuggyException("test", new Exception());
        assertThat(exception.getMessage(), is("test"));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), not(nullValue()));
    }

    @Test
    @DisplayName("Check BuggyException(Throwable e)")
    void unitTest_20180919221017() {
        BuggyException exception = new BuggyException(new Exception());
        assertThat(exception.getMessage(), is(Exception.class.getTypeName()));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), not(nullValue()));
    }

    @Test
    @DisplayName("Check CorruptedTestException()")
    void unitTest_20180919221046() {
        CorruptedTestException exception = new CorruptedTestException();
        assertThat(exception.getMessage(), is("The autotest is corrupted and must be repaired."));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), is(nullValue()));
    }

    @Test
    @DisplayName("Check CorruptedTestException(String msg)")
    void unitTest_20180919221109() {
        CorruptedTestException exception = new CorruptedTestException("test");
        assertThat(exception.getMessage(), is("test"));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), is(nullValue()));
    }

    @Test
    @DisplayName("Check CorruptedTestException(String msg, Throwable e)")
    void unitTest_20180919221154() {
        CorruptedTestException exception = new CorruptedTestException("test", new Exception());
        assertThat(exception.getMessage(), is("test"));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), not(nullValue()));
    }

    @Test
    @DisplayName("Check CorruptedTestException(Throwable e)")
    void unitTest_20180919221214() {
        CorruptedTestException exception = new CorruptedTestException(new Exception());
        assertThat(exception.getMessage(), is(Exception.class.getTypeName()));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), not(nullValue()));
    }

    @Test
    @DisplayName("Check ExpectedImplementationException(String msg)")
    void unitTest_20180919221231() {
        ExpectedImplementationException exception = new ExpectedImplementationException("test");
        assertThat(exception.getMessage(), is("test"));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), is(nullValue()));
    }

    @Test
    @DisplayName("Check ExpectedImplementationException(String msg, Throwable e)")
    void unitTest_20180919221254() {
        ExpectedImplementationException exception = new ExpectedImplementationException("test", new Exception());
        assertThat(exception.getMessage(), is("test"));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), not(nullValue()));
    }

    @Test
    @DisplayName("Check ExpectedImplementationException(Throwable e)")
    void unitTest_20180919221321() {
        ExpectedImplementationException exception = new ExpectedImplementationException(new Exception());
        assertThat(exception.getMessage(), is(Exception.class.getTypeName()));
        assertThat(exception.getStackTrace(), not(emptyArray()));
        assertThat(exception.getCause(), not(nullValue()));
    }

}
