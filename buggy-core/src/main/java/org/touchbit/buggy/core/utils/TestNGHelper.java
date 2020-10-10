package org.touchbit.buggy.core.utils;

import org.testng.IInvokedMethod;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.lang.reflect.Method;

public class TestNGHelper {

    public static Method getRealMethod(ITestResult result) {
        return getRealMethod(result.getMethod());
    }

    public static Method getRealMethod(IInvokedMethod method) {
        return getRealMethod(method.getTestMethod());
    }

    public static Method getRealMethod(ITestNGMethod method) {
        return method.getConstructorOrMethod().getMethod();
    }


    public static String getClassSimpleName(IInvokedMethod method) {
        return method.getTestMethod().getRealClass().getSimpleName();
    }

    public static String getMethodName(IInvokedMethod method) {
        return method.getTestMethod().getMethodName();
    }

    private TestNGHelper() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }

}
