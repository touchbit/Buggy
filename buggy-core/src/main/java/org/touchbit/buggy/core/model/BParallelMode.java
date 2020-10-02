package org.touchbit.buggy.core.model;

import org.testng.xml.XmlSuite;

public enum BParallelMode {

    TESTS(XmlSuite.ParallelMode.TESTS),
    METHODS(XmlSuite.ParallelMode.METHODS),
    CLASSES(XmlSuite.ParallelMode.CLASSES),
    INSTANCES(XmlSuite.ParallelMode.INSTANCES),
    NONE(XmlSuite.ParallelMode.NONE),
    ;

    private XmlSuite.ParallelMode testNGMode;

    BParallelMode(XmlSuite.ParallelMode testNGMode) {
        this.testNGMode = testNGMode;
    }

    public XmlSuite.ParallelMode getTestNGMode() {
        return this.testNGMode;
    }

}
