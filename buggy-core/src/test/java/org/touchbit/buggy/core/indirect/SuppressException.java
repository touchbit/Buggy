package org.touchbit.buggy.core.indirect;

/**
 * Created by Oleg Shaburov on 17.09.2018
 * shaburov.o.a@gmail.com
 */
public class SuppressException extends RuntimeException {

    public SuppressException() {
        super("Suppressed System.exit()");
    }

}
