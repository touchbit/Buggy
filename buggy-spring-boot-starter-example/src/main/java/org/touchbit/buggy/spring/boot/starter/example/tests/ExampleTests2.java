package org.touchbit.buggy.spring.boot.starter.example.tests;

import org.testng.annotations.Test;
import org.touchbit.buggy.core.BaseBuggyTest;
import org.touchbit.buggy.core.exceptions.AssertionException;
import org.touchbit.buggy.core.goal.interfaze.API;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Suite;

@Suite(interfaze = API.class)
public class ExampleTests2 extends BaseBuggyTest {

    @Test(description = "Ожидается успешное создание пользователя, если firstName=<пустая строка>")
    @Details(status = Status.EXP_FIX, bugs = "JIRA-123")
    public void test_2() {
        throw new AssertionException("The following 4 assertions failed:\n" +
                "     1) [Living Guests] expected:<[7]> but was:<[6]>\n" +
                "     2) [Library] expected:<'[clean]'> but was:<'[messy]'>\n" +
                "     3) [Candlestick] expected:<'[pristine]'> but was:<'[bent]'>\n" +
                "     4) [Professor] expected:<'[well kempt]'> but was:<'[bloodied and dishevelled]'>");
    }

}
