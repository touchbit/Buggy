package org.touchbit.buggy.core.config;

import org.testng.annotations.Test;
import org.touchbit.buggy.core.model.Buggy;
import org.touchbit.buggy.core.model.Suite;

/**
 * Created by Oleg Shaburov on 22.10.2018
 * shaburov.o.a@gmail.com
 */
@Suite(service = TestService.class, interfaze = TestInterface.class)
public class TestNGTestClassWithSuite {

    @Test
    @Buggy
    public void iTestResultMethodWithDetails() {

    }

    @Test
    public void iTestResultMethodWithoutDetails() {
        // for getMockITestResult()
    }

}
