package org.touchbit.buggy.okhttp;

import okhttp3.*;
import okhttp3.internal.http.RealResponseBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.StringJoiner;

import okio.Buffer;
import org.touchbit.buggy.core.tests.BaseUnitTest;

import static okhttp3.Protocol.HTTP_1_1;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Oleg Shaburov on 14.10.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("OkHttpCallLoggingInterceptor class tests")
class OkHttpCallLoggingInterceptorTests extends BaseUnitTest {

    @Test
    @DisplayName("Check POST msg logging")
    void unitTest_20181014142418() throws Exception {
        OkHttpCallLoggingInterceptor interceptor = new OkHttpCallLoggingInterceptor(UNIT_TEST_LOGGER);
        Request request = new Request.Builder()
                .url("https://touchbit.org/")
                .post(RequestBody.create(null, "RequestBody"))
                .header("request_header", "value")
                .build();
        Interceptor.Chain chain = mock(Interceptor.Chain.class);
        when(chain.request()).thenReturn(request);
        Response response = new Response.Builder()
                .code(200)
                .request(request)
                .protocol(HTTP_1_1)
                .message("OOOOOOK")
                .header("response_header", "value")
                .body(new RealResponseBody("contentTypeString", 100500L, new Buffer()))
                .build();
        when(chain.proceed(request)).thenReturn(response);
        interceptor.intercept(chain);
//        StringJoiner stringJoiner = new StringJoiner("\n>>>>>>>>>>>\n");
//        UNIT_TEST_LOGGER.takeLoggedMessages().forEach(stringJoiner::add);
//        throw new Exception(stringJoiner.toString());
        assertThat(UNIT_TEST_LOGGER.takeLoggedMessages(), contains(
                "Request:\nPOST https://touchbit.org/\nHeaders:\n    request_header: [value]\nBody:\nRequestBody",
                "Response:\nCode: 200\nMessage: OOOOOOK\nHeaders:\n    response_header: [value]\nBody:\n"
        ));
    }

    @Test
    @DisplayName("Check GET msg logging with empty response body")
    void unitTest_20181014211639() throws Exception {
        OkHttpCallLoggingInterceptor interceptor = new OkHttpCallLoggingInterceptor(UNIT_TEST_LOGGER);
        Request request = new Request.Builder()
                .url("https://touchbit.org/")
                .get()
                .header("request_header", "value")
                .build();
        Interceptor.Chain chain = mock(Interceptor.Chain.class);
        when(chain.request()).thenReturn(request);
        Response response = new Response.Builder()
                .code(200)
                .request(request)
                .protocol(HTTP_1_1)
                .message("OOOOOOK")
                .header("response_header", "value")
                .body(null)
                .build();
        when(chain.proceed(request)).thenReturn(response);
        interceptor.intercept(chain);
        assertThat(UNIT_TEST_LOGGER.takeLoggedMessages(), contains(
                "Request:\nGET https://touchbit.org/\nHeaders:\n    request_header: [value]\nBody:\n<no request body>",
                "Response:\nCode: 200\nMessage: OOOOOOK\nHeaders:\n    response_header: [value]\nBody:\n<no response body>"
        ));
    }

    @Test
    @DisplayName("Check GET msg logging with null response body")
    void unitTest_20181014212051() throws Exception {
        OkHttpCallLoggingInterceptor interceptor = new OkHttpCallLoggingInterceptor(UNIT_TEST_LOGGER);
        Request request = new Request.Builder()
                .url("https://touchbit.org/")
                .get()
                .header("request_header", "value")
                .build();
        Interceptor.Chain chain = mock(Interceptor.Chain.class);
        when(chain.request()).thenReturn(request);
        when(chain.proceed(request)).thenReturn(null);
        interceptor.intercept(chain);
        assertThat(UNIT_TEST_LOGGER.takeLoggedMessages(), contains(
                "Request:\nGET https://touchbit.org/\nHeaders:\n    request_header: [value]\nBody:\n<no request body>",
                "Response: null"
        ));
    }

    @Test
    @DisplayName("Check GET msg logging with null input stream")
    void unitTest_20181014212407() throws Exception {
        OkHttpCallLoggingInterceptor interceptor = new OkHttpCallLoggingInterceptor(UNIT_TEST_LOGGER);
        Request request = new Request.Builder()
                .url("https://touchbit.org/")
                .get()
                .header("request_header", "value")
                .build();
        Interceptor.Chain chain = mock(Interceptor.Chain.class);
        when(chain.request()).thenReturn(request);
        Response response = new Response.Builder()
                .code(200)
                .request(request)
                .protocol(HTTP_1_1)
                .message("OOOOOOK")
                .header("response_header", "value")
                .body(new RealResponseBody("contentTypeString", 100500L, null))
                .build();
        when(chain.proceed(request)).thenReturn(response);
        interceptor.intercept(chain);
        assertThat(UNIT_TEST_LOGGER.takeLoggedMessages(), contains(
                "Request:\nGET https://touchbit.org/\nHeaders:\n    request_header: [value]\nBody:\n<no request body>",
                "Response:\nCode: 200\nMessage: OOOOOOK\nHeaders:\n    response_header: [value]\nBody:\n<no response body>"
        ));
    }
}
