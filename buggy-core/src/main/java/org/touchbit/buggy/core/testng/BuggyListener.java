package org.touchbit.buggy.core.testng;

import org.jetbrains.annotations.Nullable;
import org.testng.*;
import org.touchbit.buggy.core.model.Buggy;
import org.touchbit.buggy.core.model.ResultStatus;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Suite;

import java.lang.reflect.Method;
import java.util.function.Function;

import static org.touchbit.buggy.core.model.Status.*;

/**
 * Created by Oleg Shaburov on 28.05.2018
 * shaburov.o.a@gmail.com
 */
public interface BuggyListener extends ITestNGListener {

    boolean isEnable();

    default boolean isRun(IInvokedMethod method) {
        throwNPE(method);
        return isRun(method.getTestMethod());
    }

    default boolean isRun(ITestNGMethod method) {
        throwNPE(method);
        return method.getInvocationCount() > 0;
    }

    default boolean isSkip(IInvokedMethod method) {
        throwNPE(method);
        if (isRun(method)) {
            return false;
        }
        return isSkip(method.getTestResult());
    }

    default boolean isSkip(ITestResult result) {
        throwNPE(result);
        return isITestResultSkip(result);
    }

    default boolean isSuccess(IInvokedMethod method) {
        throwNPE(method);
        if (!hasDetails(method)) {
            return isITestResultSuccess(method);
        }
        Status status = getDetailsValue(Buggy::status, method);
        return status.equals(NONE) && isITestResultSuccess(method);
    }

    default boolean isNewError(IInvokedMethod method) {
        throwNPE(method);
        if (!hasDetails(method)) {
            return isITestResultFailure(method);
        }
        Status status = getDetailsValue(Buggy::status, method);
        return status.equals(NONE) && isITestResultFailure(method);
    }

    default boolean isFixed(IInvokedMethod method) {
        throwNPE(method);
        if (!hasDetails(method)) {
            return false;
        }
        Status status = getDetailsValue(Buggy::status, method);
        return (status.equals(EXP_FIX) || status.equals(BLOCKED) || status.equals(CORRUPTED)) &&
                isITestResultSuccess(method);
    }

    default boolean isImplemented(IInvokedMethod method) {
        throwNPE(method);
        if (!hasDetails(method)) {
            return false;
        }
        Status status = getDetailsValue(Buggy::status, method);
        return status.equals(EXP_IMPL) && isITestResultSuccess(method);
    }

    default boolean isExpectedImplementation(IInvokedMethod method) {
        throwNPE(method);
        if (!hasDetails(method)) {
            return false;
        }
        Status status = getDetailsValue(Buggy::status, method);
        return status.equals(EXP_IMPL) && isITestResultFailure(method);
    }

    default boolean isExpectedFix(IInvokedMethod method) {
        throwNPE(method);
        if (!hasDetails(method)) {
            return false;
        }
        Status status = getDetailsValue(Buggy::status, method);
        return status.equals(EXP_FIX) && isITestResultFailure(method);
    }

    default boolean isCorrupted(IInvokedMethod method) {
        throwNPE(method);
        if (!hasDetails(method)) {
            return false;
        }
        Status status = getDetailsValue(Buggy::status, method);
        return status.equals(CORRUPTED) && isITestResultFailure(method);
    }

    default boolean isBlocked(IInvokedMethod method) {
        throwNPE(method);
        if (!hasDetails(method)) {
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
        throwNPE(method);
        return isITestResultSuccess(method.getTestResult());
    }

    default boolean isITestResultSuccess(ITestResult testResult) {
        throwNPE(testResult);
        int status = testResult.getStatus();
        return status == ITestResult.SUCCESS;
    }

    default boolean isITestResultFailure(IInvokedMethod method) {
        throwNPE(method);
        return isITestResultFailure(method.getTestResult());
    }


    default boolean isITestResultFailure(ITestResult result) {
        throwNPE(result);
        int iTestResult = getITestResultStatus(result);
        return iTestResult == ITestResult.FAILURE || iTestResult == ITestResult.SUCCESS_PERCENTAGE_FAILURE;
    }

    default boolean isITestResultSkip(IInvokedMethod method) {
        throwNPE(method);
        return isITestResultSkip(method.getTestResult());
    }

    default boolean isITestResultSkip(ITestResult result) {
        throwNPE(result);
        int iTestResult = getITestResultStatus(result);
        return iTestResult == ITestResult.SKIP;
    }

    default <T> T getDetailsValue(Function<Buggy, T> function, IInvokedMethod method) {
        throwNPE(method);
        Buggy buggy = getDetails(method);
        return getDetailsValue(function, buggy);
    }

    default <T> T getDetailsValue(Function<Buggy, T> function, Buggy buggy) {
        throwNPE(buggy);
        return function.apply(buggy);
    }

    default int getITestResultStatus(ITestResult iTestResult) {
        throwNPE(iTestResult);
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

    default boolean hasDetails(IInvokedMethod method) {
        if (method != null) {
            return hasDetails(method.getTestMethod());
        }
        return false;
    }

    default boolean hasDetails(ITestNGMethod method) {
        if (method != null) {
            return hasDetails(method.getConstructorOrMethod().getMethod());
        }
        return false;
    }

    default boolean hasDetails(Method method) {
        if (method != null) {
            return method.isAnnotationPresent(Buggy.class);
        }
        return false;
    }

    @Nullable
    default Buggy getDetails(IInvokedMethod method) {
        if (hasDetails(method)) {
            return getDetails(method.getTestMethod());
        }
        return null;
    }

    @Nullable
    default Buggy getDetails(ITestNGMethod method) {
        if (hasDetails(method)) {
            return getDetails(method.getConstructorOrMethod().getMethod());
        }
        return null;
    }

    @Nullable
    default Buggy getDetails(Method method) {
        if (hasDetails(method)) {
            return method.getAnnotation(Buggy.class);
        }
        return null;
    }

    default boolean hasSuite(IInvokedMethod method) {
        if (method != null) {
            return hasSuite(method.getTestMethod());
        }
        return false;
    }

    default boolean hasSuite(ITestClass iTestClass) {
        if (iTestClass != null) {
            return hasSuite(iTestClass.getRealClass());
        }
        return false;
    }

    default boolean hasSuite(ITestNGMethod method) {
        if (method != null) {
            return hasSuite(method.getRealClass());
        }
        return false;
    }

    default boolean hasSuite(Class<?> realClass) {
        return realClass.isAnnotationPresent(Suite.class);
    }

    @Nullable
    default Suite getSuite(IInvokedMethod method) {
        if (hasSuite(method)) {
            return getSuite(method.getTestMethod());
        }
        return null;
    }

    @Nullable
    default Suite getSuite(ITestClass iTestClass) {
        if (hasSuite(iTestClass)) {
            return getSuite(iTestClass.getRealClass());
        }
        return null;
    }

    @Nullable
    default Suite getSuite(ITestNGMethod method) {
        if (hasSuite(method)) {
            return getSuite(method.getRealClass());
        }
        return null;
    }

    @Nullable
    default Suite getSuite(Class<?> realClass) {
        if (hasSuite(realClass)) {
            return realClass.getAnnotation(Suite.class);
        }
        return null;
    }

    default void throwNPE(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
    }


}
