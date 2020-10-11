package org.touchbit.buggy.core.testng;

import org.jetbrains.annotations.Nullable;
import org.testng.*;
import org.touchbit.buggy.core.config.BuggyConfigurationYML;
import org.touchbit.buggy.core.logback.SiftingTestLogger;
import org.touchbit.buggy.core.model.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

import static org.touchbit.buggy.core.model.Status.*;

/**
 * Created by Oleg Shaburov on 28.05.2018
 * shaburov.o.a@gmail.com
 */
public interface BuggyListener extends ITestNGListener {

    boolean isEnable();

    default boolean isRun(IInvokedMethod method) {
        assertNotNull(method);
        return isRun(method.getTestMethod());
    }

    default boolean isRun(ITestNGMethod method) {
        assertNotNull(method);
        return method.getInvocationCount() > 0;
    }

    default boolean isSkip(IInvokedMethod method) {
        assertNotNull(method);
        if (isRun(method)) {
            return false;
        }
        return isSkip(method.getTestResult());
    }

    default boolean isSkip(ITestNGMethod method) {
        assertNotNull(method);
        return !isRun(method);
    }

    default boolean isSkip(ITestResult result) {
        assertNotNull(result);
        return isITestResultSkip(result);
    }

    default boolean isSkipByTestStatus(ITestNGMethod method) {
        assertNotNull(method);
        Buggy buggy = getBuggyAnnotation(method);
        if (buggy != null && !buggy.status().equals(Status.NONE) && !BuggyConfigurationYML.isForceRun()) {
            return true;
        }
        return false;
    }

    default boolean isSkipByType(IInvokedMethod method) {
        assertNotNull(method);
        return isSkipByType(method.getTestMethod());
    }

    default boolean isSkipByType(ITestNGMethod method) {
        assertNotNull(method);
        Buggy buggyAnnotation = getBuggyAnnotation(method);
        if (buggyAnnotation == null) {
            return false;
        }
        List<Type> types = BuggyConfigurationYML.getTypes();
        if (types.contains(Type.ALL)) {
            return false;
        }
        boolean contains = false;
        for (Type type : buggyAnnotation.types()) {
            if (types.contains(type)) {
                contains = true;
                break;
            }
        }
        return !contains;
    }

    default boolean isSuccess(IInvokedMethod method) {
        assertNotNull(method);
        if (!hasBuggyAnnotation(method)) {
            return isITestResultSuccess(method);
        }
        Status status = getDetailsValue(Buggy::status, method);
        return status.equals(NONE) && isITestResultSuccess(method);
    }

    default boolean isNewError(IInvokedMethod method) {
        assertNotNull(method);
        if (!hasBuggyAnnotation(method)) {
            return isITestResultFailure(method);
        }
        Status status = getDetailsValue(Buggy::status, method);
        return status.equals(NONE) && isITestResultFailure(method);
    }

    default boolean isFixed(IInvokedMethod method) {
        assertNotNull(method);
        if (!hasBuggyAnnotation(method)) {
            return false;
        }
        Status status = getDetailsValue(Buggy::status, method);
        return (status.equals(EXP_FIX) || status.equals(BLOCKED) || status.equals(CORRUPTED)) &&
                isITestResultSuccess(method);
    }

    default boolean isImplemented(IInvokedMethod method) {
        assertNotNull(method);
        if (!hasBuggyAnnotation(method)) {
            return false;
        }
        Status status = getDetailsValue(Buggy::status, method);
        return status.equals(EXP_IMPL) && isITestResultSuccess(method);
    }

    default boolean isExpectedImplementation(IInvokedMethod method) {
        assertNotNull(method);
        if (!hasBuggyAnnotation(method)) {
            return false;
        }
        Status status = getDetailsValue(Buggy::status, method);
        return status.equals(EXP_IMPL) && isITestResultFailure(method);
    }

    default boolean isExpectedFix(IInvokedMethod method) {
        assertNotNull(method);
        if (!hasBuggyAnnotation(method)) {
            return false;
        }
        Status status = getDetailsValue(Buggy::status, method);
        return status.equals(EXP_FIX) && isITestResultFailure(method);
    }

    default boolean isCorrupted(IInvokedMethod method) {
        assertNotNull(method);
        if (!hasBuggyAnnotation(method)) {
            return false;
        }
        Status status = getDetailsValue(Buggy::status, method);
        return status.equals(CORRUPTED) && isITestResultFailure(method);
    }

    default boolean isBlocked(IInvokedMethod method) {
        assertNotNull(method);
        if (!hasBuggyAnnotation(method)) {
            return false;
        }
        Status status = getDetailsValue(Buggy::status, method);
        return status.equals(BLOCKED) && isITestResultFailure(method);
    }

    default ResultStatus getResultStatus(IInvokedMethod method) {
        if (isSkip(method)) {
            return ResultStatus.SKIP;
        }
        if (isSuccess(method)) {
            return ResultStatus.SUCCESS;
        }
        if (isNewError(method)) {
            return ResultStatus.FAILED;
        }
        if (isImplemented(method)) {
            return ResultStatus.IMPLEMENTED;
        }
        if (isFixed(method)) {
            return ResultStatus.FIXED;
        }
        if (isExpectedImplementation(method)) {
            return ResultStatus.EXP_IMPL;
        }
        if (isExpectedFix(method)) {
            return ResultStatus.EXP_FIX;
        }
        if (isCorrupted(method)) {
            return ResultStatus.CORRUPTED;
        }
        if (isBlocked(method)) {
            return ResultStatus.BLOCKED;
        }
        return ResultStatus.FAILED;
    }

    default boolean isITestResultSuccess(IInvokedMethod method) {
        assertNotNull(method);
        return isITestResultSuccess(method.getTestResult());
    }

    default boolean isITestResultSuccess(ITestResult testResult) {
        assertNotNull(testResult);
        int status = testResult.getStatus();
        return status == ITestResult.SUCCESS;
    }

    default boolean isITestResultFailure(IInvokedMethod method) {
        assertNotNull(method);
        return isITestResultFailure(method.getTestResult());
    }


    default boolean isITestResultFailure(ITestResult result) {
        assertNotNull(result);
        int iTestResult = getITestResultStatus(result);
        return iTestResult == ITestResult.FAILURE || iTestResult == ITestResult.SUCCESS_PERCENTAGE_FAILURE;
    }

    default boolean isITestResultSkip(IInvokedMethod method) {
        assertNotNull(method);
        return isITestResultSkip(method.getTestResult());
    }

    default boolean isITestResultSkip(ITestResult result) {
        assertNotNull(result);
        int iTestResult = getITestResultStatus(result);
        return iTestResult == ITestResult.SKIP;
    }

    default <T> T getDetailsValue(Function<Buggy, T> function, IInvokedMethod method) {
        assertNotNull(method);
        Buggy buggy = getBuggyAnnotation(method);
        return getDetailsValue(function, buggy);
    }

    default <T> T getDetailsValue(Function<Buggy, T> function, Buggy buggy) {
        assertNotNull(buggy);
        return function.apply(buggy);
    }

    default int getITestResultStatus(ITestResult iTestResult) {
        assertNotNull(iTestResult);
        return iTestResult.getStatus();
    }

    default boolean hasDescription(IInvokedMethod method) {
        if (method == null) {
            return false;
        }
        return hasDescription(method.getTestMethod());
    }

    default boolean hasDescription(ITestNGMethod method) {
        if (method == null) {
            return false;
        }
        String description = method.getDescription();
        return description != null && !description.isEmpty();
    }

    default String getDescription(IInvokedMethod method) {
        if (hasDescription(method)) {
            return getDescription(method.getTestMethod());
        }
        return "";
    }

    default String getDescription(ITestNGMethod method) {
        if (hasDescription(method)) {
            return method.getDescription();
        }
        return "";
    }

    default boolean hasBuggyAnnotation(IInvokedMethod method) {
        if (method != null) {
            return hasBuggyAnnotation(method.getTestMethod());
        }
        return false;
    }

    default boolean hasBuggyAnnotation(ITestNGMethod method) {
        if (method != null) {
            return hasBuggyAnnotation(method.getConstructorOrMethod().getMethod());
        }
        return false;
    }

    default boolean hasBuggyAnnotation(Method method) {
        if (method != null) {
            return method.isAnnotationPresent(Buggy.class);
        }
        return false;
    }

    @Nullable
    default Buggy getBuggyAnnotation(IInvokedMethod method) {
        if (hasBuggyAnnotation(method)) {
            return getBuggyAnnotation(method.getTestMethod());
        }
        return null;
    }

    @Nullable
    default Buggy getBuggyAnnotation(ITestNGMethod method) {
        if (hasBuggyAnnotation(method)) {
            return getBuggyAnnotation(method.getConstructorOrMethod().getMethod());
        }
        return null;
    }

    @Nullable
    default Buggy getBuggyAnnotation(Method method) {
        if (hasBuggyAnnotation(method)) {
            return method.getAnnotation(Buggy.class);
        }
        return null;
    }

    default boolean hasSuiteAnnotation(IInvokedMethod method) {
        if (method != null) {
            return hasSuiteAnnotation(method.getTestMethod());
        }
        return false;
    }

    default boolean hasSuiteAnnotation(ITestClass iTestClass) {
        if (iTestClass != null) {
            return hasSuiteAnnotation(iTestClass.getRealClass());
        }
        return false;
    }

    default boolean hasSuiteAnnotation(ITestNGMethod method) {
        if (method != null) {
            return hasSuiteAnnotation(method.getRealClass());
        }
        return false;
    }

    default boolean hasSuiteAnnotation(Class<?> realClass) {
        return realClass.isAnnotationPresent(Suite.class);
    }

    @Nullable
    default Suite getSuiteAnnotation(IInvokedMethod method) {
        if (hasSuiteAnnotation(method)) {
            return getSuiteAnnotation(method.getTestMethod());
        }
        return null;
    }

    @Nullable
    default Suite getSuiteAnnotation(ITestClass iTestClass) {
        if (hasSuiteAnnotation(iTestClass)) {
            return getSuiteAnnotation(iTestClass.getRealClass());
        }
        return null;
    }

    @Nullable
    default Suite getSuiteAnnotation(ITestNGMethod method) {
        if (hasSuiteAnnotation(method)) {
            return getSuiteAnnotation(method.getRealClass());
        }
        return null;
    }

    @Nullable
    default Suite getSuiteAnnotation(Class<?> realClass) {
        if (hasSuiteAnnotation(realClass)) {
            return realClass.getAnnotation(Suite.class);
        }
        return null;
    }

    default void assertNotNull(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
    }

    default String getMethodName(IInvokedMethod method) {
        assertNotNull(method);
        return method.getTestMethod().getMethodName();
    }

    default Method getRealMethod(ITestResult result) {
        assertNotNull(result);
        return getRealMethod(result.getMethod());
    }

    default Method getRealMethod(IInvokedMethod method) {
        assertNotNull(method);
        return getRealMethod(method.getTestMethod());
    }

    default Method getRealMethod(ITestNGMethod method) {
        assertNotNull(method);
        return method.getConstructorOrMethod().getMethod();
    }


    default String getClassSimpleName(IInvokedMethod method) {
        assertNotNull(method);
        return method.getTestMethod().getRealClass().getSimpleName();
    }

}
