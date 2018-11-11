package org.touchbit.buggy.example.min.tests;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;

@Listeners(Bug.BugInvokedMethodListener.class)
public class Bug {
    AtomicBoolean b = new AtomicBoolean(true);
    @BeforeMethod
    public void BeforeMethod() {
        if (b.compareAndSet(true, false)) {
            throw new RuntimeException("A_beforeMethod RuntimeException");
        }
    }

    @Test()
    public void test1() { }
    @Test()
    public void test2() { }

    public static class BugInvokedMethodListener implements IInvokedMethodListener {

//        public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
//            String mName = method.getTestMethod().getInvokedMethodName();
//            System.out.println("BEFORE_I " + mName + " CALL getStatus() - " + testResult.getStatus());
//            System.out.println("BEFORE_I " + mName + " CALL getSkipCausedBy() - " + testResult.getSkipCausedBy());
//        }
//
//        public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
//            String mName = method.getTestMethod().getInvokedMethodName();
//            System.out.println("_AFTER_I " + mName + " CALL getStatus() - " + testResult.getStatus());
//            System.out.println("_AFTER_I " + mName + " CALL getSkipCausedBy() - " + testResult.getSkipCausedBy());
//        }

        /** To be implemented if the method needs a handle to contextual information. */
        public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
            System.out.println(" >>>> " + context.getFailedConfigurations().getResults(method.getTestMethod()));
            if (testResult.getStatus() == 3) {
            }
        }

        /** To be implemented if the method needs a handle to contextual information. */
        public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
            System.out.println(" >>>> " + context.getFailedConfigurations().getResults(method.getTestMethod()));
        }
    }
}
