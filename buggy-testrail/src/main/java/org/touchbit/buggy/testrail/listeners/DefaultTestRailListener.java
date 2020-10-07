package org.touchbit.buggy.testrail.listeners;

import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import org.touchbit.buggy.core.exceptions.BlockedTestException;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.exceptions.CorruptedTestException;
import org.touchbit.buggy.core.exceptions.ExpectedImplementationException;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.testrail.BaseTestRailConfig;
import org.touchbit.buggy.testrail.StatusMapper;
import org.touchbit.testrail4j.core.type.Statuses;

import static org.touchbit.buggy.core.model.Status.*;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class DefaultTestRailListener extends BaseTestRailListener<Status> {

    public DefaultTestRailListener() {
        this(new DefaultStatusMapper());
    }

    public DefaultTestRailListener(StatusMapper<Status> statusMapper) {
        super(statusMapper);
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        Details details = getDetails(method);
        String throwableMsg = processResultThrowable(details, result);
        if (method.isTestMethod() && details != null && result.getMethod().getInvocationCount() > 0) {
            int rStatus = result.getStatus();
            if (rStatus == ITestResult.SUCCESS) {
                processSuccessTest(details, result, throwableMsg);
            } else {
                processErrorTest(details, result, throwableMsg);
            }
        }
    }

    protected void processErrorTest(Details details, ITestResult result, String throwableMsg) {
        switch (details.status()) {
            case SKIP:
            case FAILED:
            case EXP_FIX:
            case BLOCKED:
            case EXP_IMPL:
            case CORRUPTED:
                addResult(result, details, details.status(), throwableMsg);
                break;
            default:
                addResult(result, details, FAILED, throwableMsg);
        }
    }

    protected String processResultThrowable(Details details, ITestResult result) {
        if (result.getStatus() != ITestResult.SUCCESS) {
            if (BLOCKED.equals(details.status())) {
                result.setThrowable(new BlockedTestException());
            }
            if (EXP_IMPL.equals(details.status())) {
                result.setThrowable(new ExpectedImplementationException());
            }
            if (CORRUPTED.equals(details.status())) {
                result.setThrowable(new CorruptedTestException(details));
            }
        }
        Throwable throwable = result.getThrowable();
        return throwable == null ? "" : " " + throwable.getMessage();
    }

    protected void processSuccessTest(Details details, ITestResult result, String throwableMsg) {
        switch (details.status()) {
            case EXP_FIX:
            case BLOCKED:
                addResult(result, details, FIXED,
                        "The error has been fixed. You need to edit the status for the test." + throwableMsg);
                break;
            case EXP_IMPL:
                addResult(result, details, IMPLEMENTED,
                        "The test passed successfully. You need to edit the status for the test." + throwableMsg);
                break;
            case CORRUPTED:
                addResult(result, details, CORRUPTED, new CorruptedTestException(details).getMessage());
                break;
            default:
                addResult(result, details, SUCCESS, "The test passed successfully." + throwableMsg);
        }
    }

    @Override
    public boolean isEnable() {
        return BaseTestRailConfig.isTestRailEnable();
    }

    public static class DefaultStatusMapper implements StatusMapper<Status> {

        @Override
        public long getId(Status status) {
            switch (status) {
                case SUCCESS:
                case FIXED:
                case IMPLEMENTED:
                    return Statuses.PASSED.getId();
                case BLOCKED:
                case SKIP:
                    return Statuses.BLOCKED.getId();
                case NONE:
                    return Statuses.UNTESTED.getId();
                case FAILED:
                case CORRUPTED:
                case EXP_IMPL:
                case EXP_FIX:
                    return Statuses.FAILED.getId();
                default:
                    throw new BuggyConfigurationException("Unhandled status received: " + status);
            }
        }
    }

}
