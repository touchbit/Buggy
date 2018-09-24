package org.touchbit.buggy.core.testng.listeners;

import org.atteo.classindex.IndexSubclasses;
import org.testng.ITestNGListener;

/**
 * Created by Oleg Shaburov on 28.05.2018
 * shaburov.o.a@gmail.com
 */
@IndexSubclasses
public interface BuggyListener extends ITestNGListener {

    boolean isEnable();

}
