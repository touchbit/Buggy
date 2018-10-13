package org.touchbit.buggy.core.transport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.BaseUnitTest;
import org.touchbit.buggy.core.transport.interceptor.CURLLoggingInterceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Oleg Shaburov on 13.10.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("InterceptorHelper util class tests")
class CURLLoggingInterceptorTests extends BaseUnitTest {

    @Test
    @DisplayName("Check intercept() with body and headers")
    void unitTest_20181013152105() {
        Log log = new Log();
        CURLLoggingInterceptor interceptor = new CURLLoggingInterceptor(log::info) {{}};
        Map<String, Collection<String>> headers = new HashMap<String, Collection<String>>() {{
            put("test_header", new ArrayList<String>() {{
                add("value");
                add("charset=utf-8");
            }});
        }};
        interceptor.intercept("GET", "http://url.test", headers, "unitTest_20181013152105");
        assertThat(log.msg, is("Playback curl:\ncurl -i -k -X GET 'http://url.test' -h 'test_header: value; charset=utf-8' " +
                "--data 'unitTest_20181013152105'"));
    }

    @Test
    @DisplayName("Check intercept() without body and headers")
    void unitTest_20181013152204() {
        Log log = new Log();
        CURLLoggingInterceptor interceptor = new CURLLoggingInterceptor(log::info) {{}};
        interceptor.intercept("PUT", "http://url.test", null, null);
        assertThat(log.msg, is("Playback curl:\ncurl -i -k -X PUT 'http://url.test'"));
    }

    @Test
    @DisplayName("Check intercept() header value == null")
    void unitTest_20181013161843() {
        Log log = new Log();
        CURLLoggingInterceptor interceptor = new CURLLoggingInterceptor(log::info) {{}};
        Map<String, Collection<String>> headers = new HashMap<String, Collection<String>>() {{
            put("test_header", null);
        }};
        interceptor.intercept("PUT", "http://url.test", headers, null);
        assertThat(log.msg, is("Playback curl:\ncurl -i -k -X PUT 'http://url.test'"));
    }

    @Test
    @DisplayName("Check intercept() header value is empty list")
    void unitTest_20181013162005() {
        Log log = new Log();
        CURLLoggingInterceptor interceptor = new CURLLoggingInterceptor(log::info) {{}};
        Map<String, Collection<String>> headers = new HashMap<String, Collection<String>>() {{
            put("test_header", new ArrayList<>());
        }};
        interceptor.intercept("PUT", "http://url.test", headers, null);
        assertThat(log.msg, is("Playback curl:\ncurl -i -k -X PUT 'http://url.test'"));
    }

}
