package org.touchbit.buggy.core.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IExecutionListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import static org.touchbit.buggy.core.utils.TestNGHelper.getClassSimpleName;
import static org.touchbit.buggy.core.utils.TestNGHelper.getMethodName;

/**
 * Created by Oleg Shaburov on 16.09.2018
 * shaburov.o.a@gmail.com
 */
public class TestSupervisor extends BaseBuggyExecutionListener implements IExecutionListener, IInvokedMethodListener {

    private static final Logger LOG = LoggerFactory.getLogger(TestSupervisor.class);

    private final List<String> executableTests = Collections.synchronizedList(new ArrayList<>());

    private final long tact;
    private boolean runMetronome = false;
    private Thread thread;

    public TestSupervisor() {
        this(9999);
    }

    public TestSupervisor(final long tact) {
        if (tact < 1000) {
            throw new BuggyConfigurationException("TestSupervisor metronome tact can not be less than 1 sec. " +
                    "Received: " + tact);
        }
        this.tact = tact;
    }

    @Override
    public void onExecutionStart() {
        executableTests.clear();
        thread = new Thread(new Metronome());
        thread.setName("Test Supervisor");
        thread.start();
        runMetronome = true;
        LOG.info("Supervisor initialized");
    }

    @Override
    public void onExecutionFinish() {
        stop();
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        addTest(getSupervisorTestName(method));
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        removeTest(getSupervisorTestName(method));
    }

    private String getSupervisorTestName(IInvokedMethod method) {
        return getMethodName(method) + " (" + getClassSimpleName(method) + ")";
    }

    private void addTest(String name) {
        if (!executableTests.contains(name)) {
            executableTests.add(name);
            LOG.info("Added executable test: {}", name);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public List<String> getExecutableTests() {
        return executableTests;
    }

    private void removeTest(String name) {
        if (executableTests.remove(name)) {
            LOG.info("The test {} is removed from the list of executable tests.", name);
        }
    }

    private void stop() {
        if (thread != null) {
            runMetronome = false;
            thread.interrupt();
            LOG.info("Metronome is stopped.");
        }
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isRunMetronome() {
        return runMetronome;
    }

    @Override
    public boolean isEnable() {
        return true;
    }

    private class Metronome implements Runnable {

        @Override
        public synchronized void run() {
            LOG.info("Metronome is started.");
            while (runMetronome) {
                try {
                    StringJoiner sj = new StringJoiner("\n    ", "[", "]");
                    getExecutableTests().forEach(sj::add);
                    LOG.info("Executable tests:\n{}", sj);
                    this.wait(tact);
                } catch (Exception ignore) {
                    runMetronome = false;
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}