package org.touchbit.buggy.testrail.listeners;

import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import org.touchbit.buggy.core.exceptions.BlockedTestException;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.exceptions.CorruptedTestException;
import org.touchbit.buggy.core.exceptions.ExpectedImplementationException;
import org.touchbit.buggy.core.model.Buggy;
import org.touchbit.buggy.core.model.ResultStatus;
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
        Buggy buggy = getBuggyAnnotation(method);
        String throwableMsg = processResultThrowable(buggy, result);
        if (method.isTestMethod() && buggy != null && result.getMethod().getInvocationCount() > 0) {
            int rStatus = result.getStatus();
            if (rStatus == ITestResult.SUCCESS) {
                processSuccessTest(buggy, result, throwableMsg);
            } else {
                processErrorTest(buggy, result, throwableMsg);
            }
        }
    }

    protected void processErrorTest(Buggy buggy, ITestResult result, String throwableMsg) {
        switch (buggy.status()) {
//            case SKIP:
//            case FAILED:
            case EXP_FIX:
            case BLOCKED:
            case EXP_IMPL:
            case CORRUPTED:
                addResult(result, buggy, buggy.status(), throwableMsg);
                break;
            default:
//                addResult(result, buggy, FAILED, throwableMsg);
        }
    }

    protected String processResultThrowable(Buggy buggy, ITestResult result) {
        if (result.getStatus() != ITestResult.SUCCESS) {
            if (BLOCKED.equals(buggy.status())) {
                result.setThrowable(new BlockedTestException());
            }
            if (EXP_IMPL.equals(buggy.status())) {
                result.setThrowable(new ExpectedImplementationException());
            }
            if (CORRUPTED.equals(buggy.status())) {
                result.setThrowable(new CorruptedTestException(buggy));
            }
        }
        Throwable throwable = result.getThrowable();
        return throwable == null ? "" : " " + throwable.getMessage();
    }

    protected void processSuccessTest(Buggy buggy, ITestResult result, String throwableMsg) {
//        switch (buggy.status()) {
//            case EXP_FIX:
//            case BLOCKED:
//                addResult(result, buggy, FIXED,
//                        "The error has been fixed. You need to edit the status for the test." + throwableMsg);
//                break;
//            case EXP_IMPL:
//                addResult(result, buggy, IMPLEMENTED,
//                        "The test passed successfully. You need to edit the status for the test." + throwableMsg);
//                break;
//            case CORRUPTED:
//                addResult(result, buggy, CORRUPTED, new CorruptedTestException(buggy).getMessage());
//                break;
//            default:
//                addResult(result, buggy, SUCCESS, "The test passed successfully." + throwableMsg);
//        }
    }

    @Override
    public boolean isEnable() {
        return BaseTestRailConfig.isTestRailEnable();
    }

    public static class DefaultStatusMapper implements StatusMapper<Status> {

        @Override
        public long getId(Status status) {
            switch (status) {
//                case SUCCESS:
//                case FIXED:
//                case IMPLEMENTED:
//                    return Statuses.PASSED.getId();
//                case BLOCKED:
//                case SKIP:
//                    return Statuses.BLOCKED.getId();
//                case NONE:
//                    return Statuses.UNTESTED.getId();
//                case FAILED:
//                case CORRUPTED:
//                case EXP_IMPL:
//                case EXP_FIX:
//                    return Statuses.FAILED.getId();
                default:
                    throw new BuggyConfigurationException("Unhandled status received: " + status);
            }
        }
    }

}
