/*
 * Copyright © 2018 Shaburov Oleg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.touchbit.buggy.okhttp;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.touchbit.buggy.core.transport.interceptor.CURLLoggingInterceptor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by Oleg Shaburov on 13.10.2018
 * shaburov.o.a@gmail.com
 */
public class OkHttpCURLLoggingInterceptor extends CURLLoggingInterceptor implements Interceptor {

    public OkHttpCURLLoggingInterceptor() {
        this(LoggerFactory.getLogger(OkHttpCURLLoggingInterceptor.class));
    }

    public OkHttpCURLLoggingInterceptor(final Logger log) {
        this(log::info);
    }

    public OkHttpCURLLoggingInterceptor(final Consumer<String> logMethod) {
        super(logMethod);
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        final Request request = chain.request();
        final String method = request.method();
        final String url = request.url().toString();
        final Map<String, List<String>> headers = request.headers().toMultimap();
        final String body = bodyToString(request);
        super.intercept(method, url, headers, body);
        return chain.proceed(request);
    }

    private String bodyToString(final Request request) throws IOException {
        try (final Buffer buffer = new Buffer()){
            if (request.body() == null) {
                return null;
            }
            request.body().writeTo(buffer);
            return buffer.clone().readString(Charset.forName("UTF-8"));
        }
    }

}
