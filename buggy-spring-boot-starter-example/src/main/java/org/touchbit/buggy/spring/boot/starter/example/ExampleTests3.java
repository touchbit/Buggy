package org.touchbit.buggy.spring.boot.starter.example;

import org.testng.annotations.Test;
import org.touchbit.buggy.core.exceptions.CorruptedTestException;
import org.touchbit.buggy.core.goal.interfaze.API;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Suite;

@Suite(interfaze = API.class)
public class ExampleTests3 {

    @Test
    @Details
    public void test_3() {
        throw new CorruptedTestException(" >>> ? ");
    }

}
