package org.touchbit.buggy.testrail;

/**
 * TestRail status mapper.
 * <p>
 * Created by Oleg Shaburov on 16.05.2018
 * shaburov.o.a@gmail.com
 */
public interface StatusMapper<T> {

    long getId(T status);

}
