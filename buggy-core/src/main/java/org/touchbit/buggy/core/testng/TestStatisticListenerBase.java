package org.touchbit.buggy.core.testng;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.testng.IExecutionListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.touchbit.buggy.core.logback.ConfLogger;
import org.touchbit.buggy.core.model.Buggy;
import org.touchbit.buggy.core.model.Status;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.ITestResult.*;
import static org.touchbit.buggy.core.utils.ANSI.*;

public abstract class TestStatisticListenerBase implements BuggyListener, IExecutionListener, IInvokedMethodListener {

    protected static AtomicInteger totalCountOfTests = new AtomicInteger(0);
    protected static AtomicInteger totalCountOfTestsRun = new AtomicInteger(0);
    protected static AtomicInteger successfulTests = new AtomicInteger(0);
    protected static AtomicInteger skippedTests = new AtomicInteger(0);
    protected static AtomicInteger unsuccess = new AtomicInteger(0);
    protected static AtomicInteger notDetailedTests = new AtomicInteger(0);

    protected static AtomicInteger newErrors = new AtomicInteger(0);
    protected static AtomicInteger expFix = new AtomicInteger(0);
    protected static AtomicInteger expImpl = new AtomicInteger(0);
    protected static AtomicInteger blockedTests = new AtomicInteger(0);
    protected static AtomicInteger corruptedTests = new AtomicInteger(0);
    protected static AtomicInteger fixedCases = new AtomicInteger(0);
    protected static AtomicInteger implemented = new AtomicInteger(0);

    protected long startTime;
    protected long finishTime;

    @Override
    public void onExecutionStart() {
        startTime = new Date().getTime();
    }

    @Override
    public void onExecutionFinish() {
        finishTime = new Date().getTime();
        printTestStatistic();
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            Buggy buggy = getBuggyAnnotation(method);
            if (buggy == null) {
                notDetailedTests.incrementAndGet(); // TODO remove after --check realisation
            }
            totalCountOfTests.incrementAndGet();
            int testNgTestStatus = testResult.getStatus();
            if (testNgTestStatus == SKIP) {
                skippedTests.incrementAndGet();
            } else {
                totalCountOfTestsRun.incrementAndGet();
            }
            if (testNgTestStatus == SUCCESS) {
                successfulTests.incrementAndGet();
            }
            if (testNgTestStatus == FAILURE || testNgTestStatus == SUCCESS_PERCENTAGE_FAILURE) {
                unsuccess.incrementAndGet();
            }
            if (buggy != null) {
                Status status = buggy.status();
                if (testNgTestStatus == SUCCESS) {
                    switch (status) {
                        case BLOCKED:
                        case CORRUPTED:
                        case EXP_FIX:
                            fixedCases.incrementAndGet();
                            break;
                        case EXP_IMPL:
                            implemented.incrementAndGet();
                            break;
                        case NONE:
                            break;
                        default:
                            // do nothing
                    }
                }
                if (testNgTestStatus == FAILURE || testNgTestStatus == SUCCESS_PERCENTAGE_FAILURE) {
                    switch (status) {
                        case CORRUPTED:
                            corruptedTests.incrementAndGet();
                            break;
                        case BLOCKED:
                            blockedTests.incrementAndGet();
                            break;
                        case EXP_FIX:
                            expFix.incrementAndGet();
                            break;
                        case EXP_IMPL:
                            expImpl.incrementAndGet();
                            break;
                        case NONE:
                        default:
                            newErrors.incrementAndGet();
                    }
                }
            }
        }
    }

    public void printTestStatistic() {
        ConfLogger.blockDelimiter();
        ConfLogger.centerBold("Summary");
        ConfLogger.dotPlaceholder("Total count of tests", totalCountOfTests);
        ConfLogger.dotPlaceholder("Total count of tests run", totalCountOfTestsRun);
        ConfLogger.dotPlaceholder("Unsuccessful tests", unsuccess, PURPLE, isNotZero(unsuccess));
        ConfLogger.dotPlaceholder("Successful tests", successfulTests);
        ConfLogger.dotPlaceholder("Skipped tests", skippedTests);
        ConfLogger.dotPlaceholder("Without @Details", notDetailedTests);
        ConfLogger.center("Details");
        ConfLogger.dotPlaceholder("New Errors", newErrors, RED, isNotZero(newErrors));
        ConfLogger.dotPlaceholder("Corrupted tests", corruptedTests, RED, isNotZero(corruptedTests));
        ConfLogger.dotPlaceholder("Blocked tests", blockedTests, PURPLE, isNotZero(blockedTests));
        ConfLogger.dotPlaceholder("Waiting to fix a defect", expFix, PURPLE, isNotZero(expFix));
        ConfLogger.dotPlaceholder("Waiting for implementation", expImpl, PURPLE, isNotZero(expImpl));
        ConfLogger.dotPlaceholder("Fixed (did not work before)", fixedCases, GREEN, isNotZero(fixedCases));
        ConfLogger.dotPlaceholder("Implemented cases", implemented, GREEN, isNotZero(implemented));
        ConfLogger.stepDelimiter();
        String time = DurationFormatUtils.formatDuration(finishTime - startTime, "HH:mm:ss,SSS");
        ConfLogger.dotPlaceholder("Execution time", time);
    }

    protected boolean isNotZero(AtomicInteger integer) {
        return integer.get() != 0;
    }

}
