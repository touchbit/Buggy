package org.touchbit.buggy.core.utils;

import org.jetbrains.annotations.Nullable;
import org.testng.IInvokedMethod;
import org.testng.ITestNGMethod;
import org.touchbit.buggy.core.model.Details;

import java.lang.reflect.Method;

public class TestNGHelper {

    @Nullable
    public static Details getDetails(IInvokedMethod method) {
        return getDetails(method.getTestMethod());
    }

    @Nullable
    public static Details getDetails(ITestNGMethod method) {
        return getDetails(method.getConstructorOrMethod().getMethod());
    }

    @Nullable
    public static Details getDetails(Method method) {
        return method.getAnnotation(Details.class);
    }

    private TestNGHelper() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }

}
