package org.touchbit.buggy.spring.boot.starter.example.tests;

import org.testng.annotations.Test;
import org.touchbit.buggy.core.BaseBuggyTest;
import org.touchbit.buggy.core.exceptions.AssertionException;
import org.touchbit.buggy.core.goal.interfaze.API;
import org.touchbit.buggy.core.model.Buggy;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.model.Type;

import java.util.Map;
import java.util.UUID;

import static org.touchbit.buggy.core.model.Type.SYSTEM;

@Suite(interfaze = API.class)
public class ExampleTests2 extends BaseBuggyTest {

    @Test
    @Buggy()
    public void test1603008938585() throws Exception {
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n",
                    envName,
                    env.get(envName));
        }
        System.out.println(" >>>>>> " + System.getProperty("BUGGY_EXAMPLE"));
        System.out.println(" >>>>>> " + System.getProperty("JAVA_BUGGY_EXAMPLE"));
        System.out.println(" >>>>>> " + System.getenv("BUGGY_EXAMPLE"));
        System.out.println(" >>>>>> " + System.getenv("JAVA_BUGGY_EXAMPLE"));
    }

    @Test(description = "Ожидается успешное создание пользователя, если firstName=<пустая строка>")
    @Buggy(status = Status.EXP_FIX, bugs = "JIRA-123")
    public void test_2_1() {
        String id = UUID.randomUUID().toString();
        step("Create user account: id={}", id);
        step("Activate user account: id={}", id);
        throw new AssertionException("The following 4 assertions failed:\n" +
                "     1) [Living Guests] expected:<[7]> but was:<[6]>\n" +
                "     2) [Library] expected:<'[clean]'> but was:<'[messy]'>\n" +
                "     3) [Candlestick] expected:<'[pristine]'> but was:<'[bent]'>\n" +
                "     4) [Professor] expected:<'[well kempt]'> but was:<'[bloodied and dishevelled]'>");
    }

    @Test()
    @Buggy("Ожидается успешное создание пользователя, если middleName=<пустая строка>")
    public void test_2_2() {

    }

    @Test(enabled = false)
    @Buggy("Ожидается успешное создание пользователя, если email=<пустая строка>")
    public void test_2_3() {

    }

    @Test()
    @Buggy(value = "Ожидается успешное создание пользователя, если email=<пустая строка>", types = SYSTEM)
    public void test_2_4() {

    }

}
