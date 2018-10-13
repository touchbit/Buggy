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

package org.touchbit.buggy.transport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.transport.utils.InterceptorHelper;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Oleg Shaburov on 13.10.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("InterceptorHelper util class tests")
class InterceptorHelperTests extends BaseUnitTest {

    @Test
    @DisplayName("Check getCurl() with body and headers")
    void unitTest_20181013152105() {
        Map<String, Collection<String>> headers = new HashMap<String, Collection<String>>() {{
            put("test_header", new ArrayList<String>() {{
                add("value");
                add("charset=utf-8");
            }});
        }};
        String result = InterceptorHelper.getCurl("GET", "http://url.test", headers, "unitTest_20181013152105");
        assertThat(result, is("curl -i -k -X GET 'http://url.test' -h 'test_header: value; charset=utf-8' --data 'unitTest_20181013152105'"));
    }

    @Test
    @DisplayName("Check getCurl() without body and headers")
    void unitTest_20181013152204() {
        String result = InterceptorHelper.getCurl("PUT", "http://url.test", null, null);
        assertThat(result, is("curl -i -k -X PUT 'http://url.test'"));
    }

    @Test
    @DisplayName("Check getCurl() header value == null")
    void unitTest_20181013161843() {
        Map<String, Collection<String>> headers = new HashMap<String, Collection<String>>() {{
            put("test_header", null);
        }};
        String result = InterceptorHelper.getCurl("PUT", "http://url.test", headers, null);
        assertThat(result, is("curl -i -k -X PUT 'http://url.test'"));
    }

    @Test
    @DisplayName("Check getCurl() header value is empty list")
    void unitTest_20181013162005() {
        Map<String, Collection<String>> headers = new HashMap<String, Collection<String>>() {{
            put("test_header", new ArrayList<>());
        }};
        String result = InterceptorHelper.getCurl("PUT", "http://url.test", headers, null);
        assertThat(result, is("curl -i -k -X PUT 'http://url.test'"));
    }

}
