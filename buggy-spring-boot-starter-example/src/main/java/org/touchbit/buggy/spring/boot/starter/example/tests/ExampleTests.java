package org.touchbit.buggy.spring.boot.starter.example.tests;

import org.testng.annotations.Test;
import org.touchbit.buggy.core.BaseBuggyTest;
import org.touchbit.buggy.core.goal.interfaze.API;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.spring.boot.starter.example.goal.Actions;
import org.touchbit.buggy.spring.boot.starter.example.goal.GitHub;

@Suite(component = GitHub.class, service = Actions.class, interfaze = API.class)
public class ExampleTests extends BaseBuggyTest {

    @Test(invocationCount = 10)
    @Details
    public void invocationCount_10() {
        step("test_1");
    }

}
