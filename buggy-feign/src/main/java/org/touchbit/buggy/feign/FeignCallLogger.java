package org.touchbit.buggy.feign;

import org.slf4j.Logger;

import java.util.function.Consumer;

/**
 * Created by Oleg Shaburov on 13.10.2018
 * shaburov.o.a@gmail.com
 */
public class FeignCallLogger extends feign.Logger {

    private final Consumer<String> logMethod;

    public FeignCallLogger(final Logger logger) {
        this(logger::info);
    }

    public FeignCallLogger(final Consumer<String> logMethod) {
        this.logMethod = logMethod;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        String msg = methodTag(configKey) + format;
        logMethod.accept(String.format(msg, args));
    }

}
