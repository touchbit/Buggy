package org.touchbit.buggy.core.test;

/**
 * Created by Oleg Shaburov on 13.01.2019
 * shaburov.o.a@gmail.com
 */
public enum TRProperty {

    RUN_ID ("testrail.run.id")
    ;

    private String property;

    TRProperty(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return property;
    }

}
