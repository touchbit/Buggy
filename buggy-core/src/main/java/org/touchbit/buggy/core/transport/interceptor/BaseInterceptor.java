package org.touchbit.buggy.core.transport.interceptor;

import java.util.Map;

/**
 * Created by Oleg Shaburov on 13.10.2018
 * shaburov.o.a@gmail.com
 */
public interface BaseInterceptor {

    <T extends Iterable<String>> void intercept(final String method,
                                                  final String url,
                                                  final Map<String, T> headers,
                                                  final String body);

}
