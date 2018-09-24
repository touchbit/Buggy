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

package org.touchbit.buggy.core.utils;

import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.process.Component;
import org.touchbit.buggy.core.process.Interface;
import org.touchbit.buggy.core.process.Service;
import org.touchbit.buggy.core.testng.TestSuite;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import static org.touchbit.buggy.core.utils.IOHelper.getFileFromResource;

/**
 * Created by Oleg Shaburov on 08.09.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("WeakerAccess")
public class BuggyUtils {

    public static final String CONSOLE_DELIMITER = "===============================================";

    public static boolean isListBaseSuiteContainsClass(final List<TestSuite> suites, final Class<?> aClass) {
        if (suites == null || suites.isEmpty() || aClass == null) {
            return false;
        }
        for (TestSuite suite : suites) {
            for (XmlTest test : suite.getTests()) {
                for (XmlClass xmlClass : test.getXmlClasses()) {
                    if (xmlClass.getName().equals(aClass.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Component getComponent(Suite suite) {
        try {
            return suite.component().newInstance();
        } catch (Exception e) {
            throw new BuggyConfigurationException("Can not create a new instance of the Component class " +
                    suite.component());
        }
    }

    public static Service getService(Suite suite) {
        try {
            return suite.service().newInstance();
        } catch (Exception e) {
            throw new BuggyConfigurationException("Can not create a new instance of the Service class " +
                    suite.service());
        }
    }

    public static Interface getInterface(Suite suite) {
        try {
            return suite.interfaze().newInstance();
        } catch (Exception e) {
            throw new BuggyConfigurationException("Can not create a new instance of the Interface class " +
                    suite.interfaze());
        }
    }

    public static boolean equalsSuites(Suite s1, Suite s2) {
        if (s1 == null || s2 == null) {
            return false;
        }
        if (Objects.equals(s1, s2)) {
            return true;
        }
        return s1.component().equals(s2.component()) &&
                s1.service().equals(s2.service()) &&
                s1.interfaze().equals(s2.interfaze());
    }

    public static Attributes getManifestAttributes() {
        Manifest mf = getManifest();
        return mf.getMainAttributes();
    }

    public static Manifest getManifest() {
        Manifest mf = new Manifest();
        try {
            InputStream inputStream = IOHelper.getResourceAsStream("./META-INF/MANIFEST.MF");
            if (inputStream == null) {
                inputStream = IOHelper.getResourceAsStream("META-INF/MANIFEST.MF");
            }
            if (inputStream != null) {
                mf.read(inputStream);
            }
            return mf;
        } catch (IOException ignore) {
            return mf;
        }
    }

    private BuggyUtils() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }

}
