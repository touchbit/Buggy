package org.touchbit.buggy.spring.boot.starter.example.tests;

import org.testng.annotations.Test;
import org.touchbit.buggy.core.goal.interfaze.API;
import org.touchbit.buggy.core.model.Buggy;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Suite;

import java.net.ProtocolException;

import static org.touchbit.buggy.core.model.Status.CORRUPTED;

@Suite(interfaze = API.class)
public class ExampleTests3 {

    @Test
    @Buggy(status = CORRUPTED)
    public void test_3_1() throws Exception {
        throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
    }

    @Test
    @Buggy(status = Status.BLOCKED, bugs = "JIRA-321")
    public void test_3_2() {

    }

}
