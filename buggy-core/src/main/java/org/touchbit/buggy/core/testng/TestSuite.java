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

import org.atteo.classindex.IndexSubclasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.process.Component;
import org.touchbit.buggy.core.process.Interface;
import org.touchbit.buggy.core.process.Service;
import org.touchbit.buggy.core.utils.BuggyUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.testng.TestNG.DEFAULT_COMMAND_LINE_TEST_NAME;
import static org.testng.xml.XmlSuite.ParallelMode.METHODS;

/**
 * Base class of test suite.
 * <p>
 * Created by Oleg Shaburov on 15.05.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("WeakerAccess")
@IndexSubclasses
public class TestSuite extends XmlSuite {

    protected Logger log = LoggerFactory.getLogger(TestSuite.class);

    private Suite suite;
    private Component component;
    private Service service;
    private Interface anInterface;

    public TestSuite() {
        this(Buggy.getPrimaryConfig().getThreads(), METHODS);
    }

    public TestSuite(Suite suite) {
        this(null, Buggy.getPrimaryConfig().getThreads(), METHODS, suite);
    }

    public TestSuite(int threadCount) {
        this(threadCount, METHODS);
    }

    public TestSuite(int threadCount, ParallelMode parallel) {
        this(null, threadCount, parallel);
    }

    public TestSuite(String name, int threadCount, ParallelMode parallel) {
        this(name, threadCount, parallel, null);
    }

    public TestSuite(String name, int threadCount, ParallelMode parallel, Suite s) {
        suite = s;
        if (s == null) {
            suite = this.getClass().getAnnotation(Suite.class);
        }
        if (suite == null) {
            throw new BuggyConfigurationException("There is no @Suite annotation for " + this.getClass());
        }
        component = BuggyUtils.getComponent(suite);
        service = BuggyUtils.getService(suite);
        anInterface = BuggyUtils.getInterface(suite);
        if (name == null) {
            this.setName(component.getName() + " " + service.getName() + " " + anInterface.getName() + " suite");
        } else {
            this.setName(name);
        }
        this.setParallel(parallel);
        this.setThreadCount(threadCount);
    }

    /**
     * The method of adding to the test classes {@link XmlClass} in the test package {@link XmlTest}
     * @param packageName - name / description of the test package
     * @param classes - array of added classes.
     */
    public void addTestPackage(String packageName, final Class<?>... classes) {
        if (packageName == null || packageName.isEmpty()) {
            packageName = DEFAULT_COMMAND_LINE_TEST_NAME;
        }
        if (classes.length == 0) {
            Buggy.incrementBuggyWarns();
            log.warn("There are no classes for the test package: {}", packageName);
            return;
        }
        log.debug("Suite {}. Add test package (XmlTest): {}", this.getName(), packageName);
        List<Class<?>> classList = new ArrayList<>(Arrays.asList(classes));
        removeDuplicatesClasses(classList);
        List<XmlClass> xmlClasses = new ArrayList<>();
        for (Class<?> aClass : classList) {
            xmlClasses.add(new XmlClass(aClass));
        }
        removeDuplicatesXmlClasses(xmlClasses, this.getTests());
        XmlTest testPackage = null;
        for (XmlTest t : this.getTests()) {
            if (t.getName().equals(packageName)) {
                testPackage = t;
                break;
            }
        }
        if (testPackage == null) {
            testPackage = new XmlTest(this);
        }
        testPackage.setName(packageName);
        testPackage.getXmlClasses().addAll(xmlClasses);
        if (log.isDebugEnabled()) {
            StringJoiner stringJoiner = new StringJoiner("\n    ", "[\n    ", "\n]");
            testPackage.getXmlClasses().forEach(c -> stringJoiner.add(c.getName()));
            log.debug("{} classes:\n{}", packageName, stringJoiner);
        }
    }

    private void removeDuplicatesClasses(final List<Class<?>> classList) {
        Set<Class<?>> hs = new HashSet<>(classList);
        if (hs.size() != classList.size()) {
            List<Class<?>> duplicates = new ArrayList<>(classList);
            hs.forEach(duplicates::remove);
            duplicates.forEach(c -> {
                classList.remove(c);
                Buggy.incrementBuggyWarns();
                log.info("A duplicate has been removed from the class list: {}", c.getTypeName());
            });
        }
    }

    private void removeDuplicatesXmlClasses(final List<XmlClass> classesList, List<XmlTest> xmlTests) {
        for (XmlTest test : xmlTests) {
            List<XmlClass> duplicates = test.getXmlClasses().stream()
                    .filter(tc -> classesList.stream()
                            .anyMatch(tc::equals))
                    .collect(Collectors.toList());
            if (!duplicates.isEmpty()) {
                duplicates.forEach(c -> {
                            Buggy.incrementBuggyWarns();
                            log.info("The test package {} already contains a class: {}", test.getName(), c);
                });
            }
            duplicates.forEach(classesList::remove);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            TestSuite testSuite = (TestSuite) obj;
            return testSuite.suite.equals(suite);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + ((suite == null) ? 0 : suite.hashCode());
    }

    public Suite getSuite() {
        return suite;
    }

    public Component getComponent() {
        return component;
    }

    public Service getService() {
        return service;
    }

    public Interface getInterface() {
        return anInterface;
    }

    public void setLog(Logger logger) {
        log = logger;
    }
}
