package org.touchbit.buggy.spring.boot.starter.example;

import org.testng.annotations.Test;
import org.touchbit.buggy.core.BaseBuggyTest;
import org.touchbit.buggy.core.config.BuggyConfig;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Suite;

@Suite()
public class ExampleTests extends BaseBuggyTest {

    @Test
    @Details
    public void test_1() {
        System.out.println("test_1" + BuggyConfig.isForce());
        System.out.println("test_1" + BuggyConfig.isPrintLog());
        System.out.println("test_1" + BuggyConfig.getComponents());
        System.out.println("test_1" + BuggyConfig.getServices());
        System.out.println("test_1" + BuggyConfig.getInterfaces());
        System.out.println("test_1" + BuggyConfig.getTypes());
    }

}
