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

import okhttp3.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.BaseUnitTest;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Oleg Shaburov on 13.10.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("cURL logging interceptors tests")
class CURLLoggingInterceptorTests extends BaseUnitTest {

    @Test
    @DisplayName("Check OkHttpCURLLoggingInterceptor()")
    void unitTest_20181013155150() {
        new OkHttpCURLLoggingInterceptor();
    }

    @Test
    @DisplayName("Check OkHttpCURLLoggingInterceptor(final Logger log)")
    void unitTest_20181013155212() {
        new OkHttpCURLLoggingInterceptor(new Log());
    }

    @Test
    @DisplayName("Check OkHttpCURLLoggingInterceptor() without body")
    void unitTest_20181013155307() throws IOException {
        final Log log = new Log();
        OkHttpCURLLoggingInterceptor interceptor = new OkHttpCURLLoggingInterceptor(log::info);
        Request request = new Request.Builder()
                .url("https://touchbit.org/")
                .get()
                .header("test_header", "value")
                .build();
        Interceptor.Chain chain = mock(Interceptor.Chain.class);
        when(chain.request()).thenReturn(request);
        interceptor.intercept(chain);
        assertThat(log.msg, is("Playback curl:\n" +
                "curl -i -k -X GET " +
                "'https://touchbit.org/' " +
                "-h 'test_header: value'"));
    }

    @Test
    @DisplayName("Check OkHttpCURLLoggingInterceptor() with body")
    void unitTest_20181013161130() throws IOException {
        final Log log = new Log();
        OkHttpCURLLoggingInterceptor interceptor = new OkHttpCURLLoggingInterceptor(log::info);
        Request request = new Request.Builder()
                .url("https://touchbit.org/")
                .post(RequestBody.create(null, "RequestBody"))
                .header("test_header", "value")
                .build();
        Interceptor.Chain chain = mock(Interceptor.Chain.class);
        when(chain.request()).thenReturn(request);
        interceptor.intercept(chain);
        assertThat(log.msg, is("Playback curl:\n" +
                "curl -i -k -X POST " +
                "'https://touchbit.org/' " +
                "-h 'test_header: value' " +
                "--data 'RequestBody'"));
    }

}
