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

import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.tests.BaseUnitTest;

import java.util.*;

import static feign.Request.HttpMethod.POST;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

/**
 * Created by Oleg Shaburov on 13.10.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("Feign http client cURL logging interceptors tests")
class FeignCURLLoggingInterceptorTests extends BaseUnitTest {

    private static final String TARGET = "http://touchbit.org";

    @Test
    @DisplayName("Check FeignCURLLoggingInterceptor(final Consumer<String> logMethod)")
    void unitTest_20181013143711() {
        FeignCURLLoggingInterceptor interceptor = new FeignCURLLoggingInterceptor(TARGET, TEST_LOGGER::info);
        Map<String, Collection<String>> headers = new HashMap<String, Collection<String>>() {{
            put("test_header", new ArrayList<String>() {{
                add("value");
                add("charset=utf-8");
            }});
        }};
        RequestTemplate rt = new RequestTemplate()
                .method(POST)
                .uri("/api/v1", true)
                .headers(headers)
                .body("test-body");
        interceptor.apply(rt);
        assertThat(TEST_LOGGER.takeLoggedMessages(),
                contains("Playback curl:\ncurl -i -k -X POST 'http://touchbit.org/api/v1' " +
                        "-H 'test_header: charset=utf-8; value' -H 'Content-Length: 9' --data 'test-body'"
                ));
    }

    @Test
    @DisplayName("Check FeignCURLLoggingInterceptor(final Logger log)")
    void unitTest_20181013151037() {
        new FeignCURLLoggingInterceptor(TARGET, TEST_LOGGER);
    }

    @Test
    @DisplayName("Check FeignCURLLoggingInterceptor()")
    void unitTest_20181013153128() {
        new FeignCURLLoggingInterceptor(TARGET);
    }

    @Test
    @DisplayName("Check FeignCURLLoggingInterceptor if body == null")
    void unitTest_20181013162214() {
        FeignCURLLoggingInterceptor interceptor = new FeignCURLLoggingInterceptor(TARGET, TEST_LOGGER::info);
        Map<String, Collection<String>> headers = new HashMap<String, Collection<String>>() {{
            put("test_header", new ArrayList<String>() {{
                add("value");
                add("charset=utf-8");
            }});
        }};
        RequestTemplate rt = new RequestTemplate()
                .method(POST)
                .uri("/api/v1", true)
                .headers(headers)
                .body((String) null);
        interceptor.apply(rt);
        assertThat(TEST_LOGGER.takeLoggedMessages(),
                contains("Playback curl:\ncurl -i -k -X POST 'http://touchbit.org/api/v1' " +
                        "-H 'test_header: charset=utf-8; value'"
                ));
    }

}
