package org.touchbit.buggy.spring.boot.starter.example;

import org.testng.annotations.Test;
import org.touchbit.buggy.core.goal.interfaze.API;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Suite;

@Suite(interfaze = API.class)
public class ExampleTests2 {

    @Test
    @Details(status = Status.EXP_FIX)
    public void test_2() {

    }

}
