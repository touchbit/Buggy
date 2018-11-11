package org.touchbit.buggy.example.min.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.test.BaseBuggyTest;
import org.touchbit.buggy.example.min.goals.API;
import org.touchbit.buggy.example.min.goals.GitLab;

import java.util.concurrent.atomic.AtomicBoolean;

@Suite(service = GitLab.class, interfaze = API.class, task = "temp")
public class A extends BaseBuggyTest {

    AtomicBoolean b = new AtomicBoolean(true);

    @BeforeMethod(description = "description A_beforeMethod")
    public void A_beforeMethod() {
        step("BEFORE_METHOD do something");
        step("BEFORE_METHOD do something");
        step("BEFORE_METHOD do something");
        if (!b.compareAndSet(true, false)) {
            throw new RuntimeException("A_beforeMethod RuntimeException");
        }
    }

    @Test(description = "descriptionA1_test_20181031164241")
    @Details
    public void A1_test_20181031164241() {
        step("do something A1_test_20181031164241");
        step("do something A1_test_20181031164241");
        step("do something A1_test_20181031164241");
    }

    @Test(description = "description A2_test_20181031164242")
    @Details
    public void A2_test_20181031164242() {
        step("do something A2_test_20181031164242");
        step("do something A2_test_20181031164242");
        step("do something A2_test_20181031164242");
    }

}
