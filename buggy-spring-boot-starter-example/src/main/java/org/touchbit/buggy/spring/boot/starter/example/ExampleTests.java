package org.touchbit.buggy.spring.boot.starter.example;

import org.testng.annotations.Test;
import org.touchbit.buggy.core.BaseBuggyTest;
import org.touchbit.buggy.core.config.BuggyConfig;
import org.touchbit.buggy.core.goal.interfaze.API;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.spring.boot.starter.example.goal.Actions;
import org.touchbit.buggy.spring.boot.starter.example.goal.GitHub;

@Suite(component = GitHub.class, service = Actions.class, interfaze = API.class)
public class ExampleTests extends BaseBuggyTest {

    @Test
    @Details
    public void test_1() {
        step("test_1");
    }

}
