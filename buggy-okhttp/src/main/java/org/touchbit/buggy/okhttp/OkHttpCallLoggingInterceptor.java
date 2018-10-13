package org.touchbit.buggy.okhttp;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by Oleg Shaburov on 13.10.2018
 * shaburov.o.a@gmail.com
 */
public class OkHttpCallLoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        return null;
    }

}
