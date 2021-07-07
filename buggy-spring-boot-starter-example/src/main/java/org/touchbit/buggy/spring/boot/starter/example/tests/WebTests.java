package org.touchbit.buggy.spring.boot.starter.example.tests;

import org.testng.annotations.Test;
import org.touchbit.buggy.core.BaseBuggyTest;
import org.touchbit.buggy.core.goal.interfaze.WEB;
import org.touchbit.buggy.core.model.Buggy;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.spring.boot.starter.example.goal.Actions;
import org.touchbit.buggy.spring.boot.starter.example.goal.GitHub;

@Suite(service = Actions.class, interfaze = WEB.class)
public class WebTests extends BaseBuggyTest {

    @Test
    @Buggy("Ожидается успешная авторизация в админке сужествующего, активного пользователя с правами администратора.")
    public void webTest1() {
        step("webTest1");
    }


}
