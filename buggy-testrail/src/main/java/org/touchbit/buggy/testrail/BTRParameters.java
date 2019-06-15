package org.touchbit.buggy.testrail;

/**
 * Created by Oleg Shaburov on 20.01.2019
 * shaburov.o.a@gmail.com
 */
public class BTRParameters {

    public static final String ENABLE           = "--tr";
    public static final String HOST             = "--tr-host";
    public static final String LOGIN            = "--tr-login";
    public static final String PASS_TOKEN       = "--tr-auth";

    private BTRParameters() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }
}
