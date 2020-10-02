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

package org.touchbit.buggy.core.tests.testng;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;
import org.touchbit.buggy.core.goal.component.AllComponents;
import org.touchbit.buggy.core.tests.BaseUnitTest;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.config.TestClassWithoutSuite;
import org.touchbit.buggy.core.config.TestComponent;
import org.touchbit.buggy.core.config.TestInterface;
import org.touchbit.buggy.core.config.TestService;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.testng.TestSuite;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.xml.XmlSuite.ParallelMode.TESTS;

/**
 * Created by Oleg Shaburov on 20.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("TestSuite class tests")
class TestSuiteTests extends BaseUnitTest {

    @Test
    @DisplayName("Check TestSuite() constructor with @Suite")
    void unitTest_20180920150141() {
        UnitTestTestSuite suite = new UnitTestTestSuite();
        assertThat(suite.getInterface(), is(notNullValue()));
        assertThat(suite.getComponent(), is(notNullValue()));
        assertThat(suite.getService(), is(notNullValue()));
        assertThat(suite.getSuite(), is(notNullValue()));
        assertThat(suite.getSuite().purpose(), is(""));
    }

    @Test
    @DisplayName("Check TestSuite(int threadCount) constructor with @Suite")
    void unitTest_20180920153817() {
        UnitTestTestSuite suite = new UnitTestTestSuite(10);
        assertThat(suite.getInterface(), is(notNullValue()));
        assertThat(suite.getComponent(), is(notNullValue()));
        assertThat(suite.getService(), is(notNullValue()));
        assertThat(suite.getSuite(), is(notNullValue()));
        assertThat(suite.getSuite().purpose(), is(""));
    }

    @Test
    @DisplayName("Check TestSuite() constructor without @Suite")
    void unitTest_20180920151455() {
        BuggyConfigurationException e = execute(TestSuite::new, BuggyConfigurationException.class);
        assertThat(e.getMessage(),
                is("There is no @Suite annotation for class org.touchbit.buggy.core.testng.TestSuite"));
    }

    @Test
    @DisplayName("Check TestSuite(int threadCount) constructor without @Suite")
    void unitTest_20180920153513() {
        BuggyConfigurationException e = execute(() -> new TestSuite(10), BuggyConfigurationException.class);
        assertThat(e.getMessage(),
                is("There is no @Suite annotation for class org.touchbit.buggy.core.testng.TestSuite"));
    }

    @Test
    @DisplayName("Check TestSuite(String name, int threadCount, ParallelMode parallel, Suite s)")
    void unitTest_20180920154032() {
        TestSuite testSuite = new TestSuite("name", 120, TESTS, SUITE);
        assertThat(testSuite.getInterface(), is(notNullValue()));
        assertThat(testSuite.getComponent(), is(notNullValue()));
        assertThat(testSuite.getService(), is(notNullValue()));
        assertThat(testSuite.getSuite(), is(notNullValue()));
        assertThat(testSuite.getSuite().purpose(), is("task"));
        assertThat(testSuite.getName(), is("name"));
    }

    @Test
    @DisplayName("Check the same XmlTest & XmlClasses")
    void unitTest_20180920154709() {
        TestSuite testSuite = new TestSuite("name", 120, TESTS, SUITE);
        testSuite.addTestPackage("TestPackage", TestClassWithoutSuite.class);
        testSuite.addTestPackage("TestPackage", TestClassWithoutSuite.class);
        assertThat(testSuite.getTests().size(), is(1));
        assertThat(testSuite.getTests().get(0).getName(), is("TestPackage"));
        assertThat(testSuite.getTests().get(0).getXmlClasses().size(), is(1));
        assertThat(testSuite.getTests().get(0).getXmlClasses().get(0).getName(),
                is(TestClassWithoutSuite.class.getTypeName()));
    }

    @Test
    @DisplayName("Check different XmlClasses")
    void unitTest_20180920173105() {
        TestSuite testSuite = new TestSuite("name", 120, TESTS, SUITE);
        testSuite.addTestPackage("TestPackage", TestClassWithoutSuite.class);
        testSuite.addTestPackage("TestPackage", TestSuiteTests.class);
        assertThat(testSuite.getName(), is("name"));
        assertThat(testSuite.getTests().size(), is(1));
        assertThat(testSuite.getTests().get(0).getName(), is("TestPackage"));
        assertThat(testSuite.getTests().get(0).getXmlClasses().size(), is(2));
        Set<String> unavailableItems = testSuite.getTests().get(0).getXmlClasses().stream()
                .map(XmlClass::getName)
                .collect(Collectors.toSet());
        assertThat(unavailableItems,
                containsInAnyOrder(TestSuiteTests.class.getTypeName(), TestClassWithoutSuite.class.getTypeName()));
    }

    @Test
    @DisplayName("Check duplicates classes")
    void unitTest_20180920200611() {
        TestSuite testSuite = new TestSuite("name", 120, TESTS, SUITE);
        testSuite.addTestPackage("TestPackage", TestClassWithoutSuite.class, TestClassWithoutSuite.class);
        assertThat(testSuite.getName(), is("name"));
        assertThat(testSuite.getTests().size(), is(1));
        assertThat(testSuite.getTests().get(0).getName(), is("TestPackage"));
        assertThat(testSuite.getTests().get(0).getXmlClasses().size(), is(1));
        Set<String> unavailableItems = testSuite.getTests().get(0).getXmlClasses().stream()
                .map(XmlClass::getName)
                .collect(Collectors.toSet());
        assertThat(unavailableItems,
                containsInAnyOrder(TestClassWithoutSuite.class.getTypeName()));
    }

    @Test
    @DisplayName("Check duplicates XmlTest")
    void unitTest_20180920200806() {
        TestSuite testSuite = new TestSuite("name", 120, TESTS, SUITE);
        testSuite.setLog(TEST_LOGGER);
        testSuite.addTestPackage("TestPackage1", TestClassWithoutSuite.class, TestClassWithoutSuite.class);
        testSuite.addTestPackage("TestPackage2", TestSuiteTests.class, TestSuiteTests.class);
        testSuite.addTestPackage("TestPackage1", TestClassWithoutSuite.class, TestClassWithoutSuite.class);
        assertThat(testSuite.getName(), is("name"));
        assertThat(testSuite.getTests().size(), is(2));
        Set<String> xmlTestSet = testSuite.getTests().stream()
                .map(XmlTest::getName)
                .collect(Collectors.toSet());
        assertThat(xmlTestSet, containsInAnyOrder("TestPackage1", "TestPackage2"));
        assertThat(testSuite.getTests().get(0).getXmlClasses().size(), is(1));
        for (XmlTest test : testSuite.getTests()) {
            Set<String> classSet = test.getXmlClasses().stream()
                    .map(XmlClass::getName)
                    .collect(Collectors.toSet());
            if (test.getName().equalsIgnoreCase("TestPackage1")) {
                assertThat(classSet, containsInAnyOrder(TestClassWithoutSuite.class.getTypeName()));
            } else {
                assertThat(classSet, containsInAnyOrder(TestSuiteTests.class.getTypeName()));
            }
        }
        assertThat(TEST_LOGGER.takeLoggedMessages().toString(), containsString(" classes:\n"));
    }

    @Test
    @DisplayName("Check empty XmlTest")
    void unitTest_20180920204620() {
        TestSuite testSuite = new TestSuite("name", 120, TESTS, SUITE);
        testSuite.addTestPackage("TestPackage1");
        assertThat(testSuite.getTests().size(), is(0));
    }

    @Test
    @DisplayName("Check empty XmlTest name")
    void unitTest_20180920204732() {
        TestSuite testSuite = new TestSuite("name", 120, TESTS, SUITE);
        testSuite.addTestPackage("");
        assertThat(testSuite.getTests().size(), is(0));
        testSuite.addTestPackage(null);
        assertThat(testSuite.getTests().size(), is(0));
    }

    @Test
    @DisplayName("Check equals by Suite annotation")
    void unitTest_20181022031937() {
        TestSuite testSuite1 = new TestSuite("name", 120, TESTS, SUITE);
        TestSuite testSuite2 = new TestSuite("name", 120, TESTS, SUITE);
        assertThat(testSuite1.equals(testSuite2), is(true));
    }

    @Test
    @DisplayName("Check not equals by Suite annotation")
    void unitTest_20181022032227() {
        TestSuite testSuite1 = new TestSuite("name", 120, TESTS, SUITE);
        TestSuite testSuite2 = new TestSuite("name", 120, TESTS, SUITE2);
        assertThat(testSuite1.equals(testSuite2), is(false));
    }

    @Test
    @DisplayName("Check not equals by Object")
    void unitTest_20181022032554() {
        TestSuite testSuite = new TestSuite("name", 120, TESTS, SUITE);
        testSuite.setLog(TEST_LOGGER);
        assertThat(testSuite.equals(new Object()), is(false));
    }

    @Test
    @DisplayName("Check hashCode for 2 same suites")
    void unitTest_20181022033229() {
        TestSuite testSuite1 = new TestSuite("name", 120, TESTS, SUITE);
        TestSuite testSuite2 = new TestSuite("name", 120, TESTS, SUITE);
        assertThat(testSuite1.hashCode(), is(testSuite2.hashCode()));
    }

    @Test
    @DisplayName("Check hashCode for 2 different suites")
    void unitTest_20181022033314() {
        TestSuite testSuite1 = new TestSuite("name", 120, TESTS, SUITE);
        TestSuite testSuite2 = new TestSuite("name", 120, TESTS, SUITE2);
        assertThat(testSuite1.hashCode(), not(testSuite2.hashCode()));
    }

    @Test
    @DisplayName("GIVEN  WHEN  THEN")
    void unitTest_20181029004902() {
        TestSuite testSuite = new TestSuite("TestSuiteName", 120, TESTS, SUITE);
        testSuite.setLog(TEST_LOGGER);
        TEST_LOGGER.whenDebugEnabled(false);
        testSuite.addTestPackage("TestPackage_1", Object.class);
        assertThat(TEST_LOGGER.takeLoggedMessages(),
                contains("Suite TestSuiteName. Add test package (XmlTest): TestPackage_1")
        );
        TEST_LOGGER.whenDebugEnabled(true);
        testSuite.addTestPackage("TestPackage_2", Object.class);
        assertThat(TEST_LOGGER.takeLoggedMessages(), contains(
                "The test package TestPackage_1 already contains a class: [XmlClass class=java.lang.Object]",
                "The TestPackage_2 test package (XmlTest) does not contain unique test classes and has not been added to the TestSuiteName test suite.")
        );
    }

    private static final Suite SUITE2 = new Suite() {
        @Override
        public Class<? extends Annotation> annotationType() { return Suite.class; }
        @Override
        public Class<? extends Component> component() { return AllComponents.class; }
        @Override
        public Class<? extends Service> service() { return TestService.class; }
        @Override
        public Class<? extends Interface> interfaze() { return TestInterface.class; }
        @Override
        public String purpose() { return "task"; }
    };

    private static final Suite SUITE = new Suite() {
        @Override
        public Class<? extends Annotation> annotationType() { return Suite.class; }
        @Override
        public Class<? extends Component> component() { return TestComponent.class; }
        @Override
        public Class<? extends Service> service() { return TestService.class; }
        @Override
        public Class<? extends Interface> interfaze() { return TestInterface.class; }
        @Override
        public String purpose() { return "task"; }
    };

    @SuppressWarnings("WeakerAccess")
    @Suite(component = TestComponent.class, service = TestService.class, interfaze = TestInterface.class)
    public static class UnitTestTestSuite extends TestSuite {

        public UnitTestTestSuite(int threadCount) {
            super(threadCount);
            addTestPackage("Example", TestClassWithoutSuite.class);
        }

        public UnitTestTestSuite() {
            addTestPackage("Example", TestClassWithoutSuite.class);
        }

    }

}
