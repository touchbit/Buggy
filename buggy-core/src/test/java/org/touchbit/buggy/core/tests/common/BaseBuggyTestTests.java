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
import org.slf4j.Logger;
import org.touchbit.buggy.core.BaseBuggyTest;
import org.touchbit.buggy.core.tests.BaseUnitTest;

/**
 * Created by Oleg Shaburov on 19.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("BaseBuggyTest class Tests")
class BaseBuggyTestTests extends BaseUnitTest {

//    @Test
//    @DisplayName("Check BuggyTest.getSteps()")
//    void unitTest_20180919223844() {
//        System.setProperty(INTELLIJ_IDEA_TEST_RUN, Boolean.TRUE.toString());
//        try {
//            Logger logger = mock(Logger.class);
//            BuggyTest buggyTest = new BuggyTest(logger);
//            buggyTest.test();
//        } finally {
//            System.setProperty(INTELLIJ_IDEA_TEST_RUN, Boolean.FALSE.toString());
//        }
//    }
//
//    @Test
//    @DisplayName("Check setLog(final Logger logger)")
//    void unitTest_20180919230422() {
//        System.setProperty(INTELLIJ_IDEA_TEST_RUN, Boolean.TRUE.toString());
//        try {
//            Logger logger = mock(Logger.class);
//            BuggyTest buggyTest = new BuggyTest();
//            buggyTest.setLogger(logger);
//            buggyTest.test();
//            buggyTest.setLogger(null);
//        } finally {
//            System.setProperty(INTELLIJ_IDEA_TEST_RUN, Boolean.FALSE.toString());
//        }
//    }
//
//    @Test
//    @DisplayName("Check Missing IntellijIdeaPluginListener")
//    void unitTest_20180920000012() {
//        System.setProperty(INTELLIJ_IDEA_TEST_RUN, Boolean.FALSE.toString());
//        try {
//            Logger logger = mock(Logger.class);
//            BuggyTest buggyTest = new BuggyTest();
//            buggyTest.setLogger(logger);
//            buggyTest.test();
//            assertExitCode(1, "Missing IntellijIdeaPluginListener in the Intellij IDEA TestNG plugin configuration.");
//        } finally {
//            System.setProperty(INTELLIJ_IDEA_TEST_RUN, Boolean.FALSE.toString());
//        }
//    }

    private static class BuggyTest extends BaseBuggyTest {

        BuggyTest() {
        }

        BuggyTest(final Logger logger) {
            super(logger);
        }

        void setLogger(final Logger logger) {
            BaseBuggyTest.setLog(logger);
        }

        protected void test() {
            step("test");
        }

    }
}
