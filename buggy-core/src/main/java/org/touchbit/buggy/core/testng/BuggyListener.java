package org.touchbit.buggy.core.testng;

import org.testng.ITestNGListener;

/**
 * Created by Oleg Shaburov on 28.05.2018
 * shaburov.o.a@gmail.com
 */
public interface BuggyListener extends ITestNGListener {

    boolean isEnable();

}
