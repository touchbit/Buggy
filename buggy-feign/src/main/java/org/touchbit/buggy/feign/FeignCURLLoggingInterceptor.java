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

package org.touchbit.buggy.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.touchbit.buggy.core.transport.CURLLoggingInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by Oleg Shaburov on 13.10.2018
 * shaburov.o.a@gmail.com
 */
public class FeignCURLLoggingInterceptor extends CURLLoggingInterceptor implements RequestInterceptor {

    private final String host;

    public FeignCURLLoggingInterceptor(final String host) {
        this(host, LoggerFactory.getLogger(FeignCURLLoggingInterceptor.class));
    }

    public FeignCURLLoggingInterceptor(final String host, final Logger log) {
        this(host, log::info);
    }

    public FeignCURLLoggingInterceptor(final String host, final Consumer<String> logMethod) {
        super(logMethod);
        this.host = host;
    }

    @Override
    public void apply(final RequestTemplate requestTemplate) {
        final String method = requestTemplate.method();
        final String url = requestTemplate.url();
        final Map<String, Collection<String>> headers = requestTemplate.headers();
        String body = null;
        if (requestTemplate.requestBody() != null && requestTemplate.requestBody().asBytes() != null) {
            byte[] bodyBytes = requestTemplate.requestBody().asBytes().clone();
            body = new String(bodyBytes, StandardCharsets.UTF_8);
        }
        super.intercept(method, host + url, headers, body);
    }

}
