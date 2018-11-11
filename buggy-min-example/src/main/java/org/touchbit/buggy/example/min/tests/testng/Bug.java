package org.touchbit.buggy.example.min.tests.testng;

import org.testng.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.UUID;

@Listeners(Bug.InvokedMethodListener.class)
public class Bug {
    @BeforeMethod
    public void BeforeMethod() {
        throw new RuntimeException("RuntimeException test2");
    }
    @Test()
    public void test1() { }

    public static class InvokedMethodListener implements IInvokedMethodListener, ISuiteListener {

        public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
            System.out.println("beforeInvocation() - method.getTestResult().getStatus()     - " + method.getTestResult().getStatus());
            System.out.println("beforeInvocation() - testResult.getStatus() - " + testResult.getStatus());
        }

        public void onFinish(ISuite suite) {
            for (IInvokedMethod method : suite.getAllInvokedMethods()) {
                String mName = method.getTestMethod().getMethodName();
                System.out.println("\nonFinish   " + mName);
                System.out.println("onFinish   method.getTestResult().get - " + method.getTestResult().getStatus());
                System.out.println("onFinish   method.getTestResult().getTestName() - " + method.getTestResult().getTestName());
                System.out.println("onFinish   method.getTestResult().getTestName() - " + method.getTestResult().getName());
                System.out.println("onFinish   method.getTestResult().getTestName() - " + method.getTestResult().getSkipCausedBy());
            }
        }

    }
}
