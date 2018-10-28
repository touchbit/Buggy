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

package org.touchbit.buggy.core.tests.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.tests.BaseUnitTest;
import org.touchbit.buggy.core.exceptions.AssertionException;
import org.touchbit.buggy.core.utils.StringUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by Oleg Shaburov on 14.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("StringUtils Tests")
class StringUtilsTests extends BaseUnitTest {

    @Test
    @DisplayName("Check dotFiller(Object prefix, int i)")
    void test20180914194403() {
        String msg = StringUtils.dotFiller("Test", 10);
        assertThat(msg, is("Test......"));
    }

    @Test
    @DisplayName("Check dotFiller(Object prefix, int i, Object suffix)")
    void unitTest_20180914220458() {
        String msg = StringUtils.dotFiller("Test", 10, "OK");
        assertThat(msg, is("Test....OK"));
    }

    @Test
    @DisplayName("Check underscoreFiller(Object prefix, int i)")
    void unitTest_20180914221041() {
        String msg = StringUtils.underscoreFiller("Test", 10);
        assertThat(msg, is("Test______"));
    }

    @Test
    @DisplayName("Check underscoreFiller(Object prefix, int i, Object suffix)")
    void unitTest_20180914221123() {
        String msg = StringUtils.underscoreFiller("Test", 10, "OK");
        assertThat(msg, is("Test____OK"));
    }

    @Test
    @DisplayName("Check filler(Object prefix, String symbol, int i)")
    void unitTest_20180914221207() {
        String msg = StringUtils.filler("Test", "+", 10);
        assertThat(msg, is("Test++++++"));
    }

    @Test
    @DisplayName("Check filler(Object prefix, String symbol, int length, Object postfix)")
    void unitTest_20180914221305() {
        String msg = StringUtils.filler("Test", "+", 10, "OK");
        assertThat(msg, is("Test++++OK"));
    }

    @Test
    @DisplayName("Check filler default values")
    void unitTest_20180914221346() {
        String msg = StringUtils.filler(null, null, 5, null);
        assertThat(msg, is("....."));
    }

    @Test
    @DisplayName("WHEN filler with empty symbol THEN default dot")
    void unitTest_20181029012204() {
        String msg = StringUtils.filler(null, "", 5, null);
        assertThat(msg, is("....."));
    }

    @Test
    @DisplayName("Check out of message length")
    void unitTest_20180914221536() {
        String msg = StringUtils.filler("Test", "+", 5, "OK");
        assertThat(msg, is("Test+++OK"));
    }

    @Test
    @DisplayName("Check URL encode(String value)")
    void unitTest_20180914222346() {
        String msg = StringUtils.encode("[123]");
        assertThat(msg, is("%5B123%5D"));
    }

    @Test
    @DisplayName("Check URL decode(String value)")
    void unitTest_20180914222738() {
        String msg = StringUtils.decode("%5B123%5D");
        assertThat(msg, is("[123]"));
    }

    @Test
    @DisplayName("Check URL decode ignore exception")
    void unitTest_20180914222830() {
        String msg = StringUtils.decode(null);
        assertThat(msg, is(nullValue()));
    }

    @Test
    @DisplayName("Check URL encode ignore exception")
    void unitTest_20180914223048() {
        String msg = StringUtils.encode(null);
        assertThat(msg, is(nullValue()));
    }

    @Test
    @DisplayName("Check println(String msg)")
    void unitTest_20180914223136() {
        StringUtils.println("unitTest_20180914223136 test message.");
    }

    @Test
    @DisplayName("Check println(String msg, Throwable t)")
    void unitTest_20180914223257() {
        StringUtils.println("unitTest_20180914223257 test message", new AssertionException("With Exception"));
    }

    @Test
    @DisplayName("Check println() null values")
    void unitTest_20180914223459() {
        StringUtils.println(null, null);
    }

    @Test
    @DisplayName("Check BuggyUtils constructor")
    void unitTest_20180915213419() throws NoSuchMethodException {
        checkUtilityClassConstructor(StringUtils.class);
    }

}
