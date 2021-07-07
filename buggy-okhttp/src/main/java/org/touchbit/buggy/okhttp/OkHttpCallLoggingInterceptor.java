package org.touchbit.buggy.okhttp;

import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * Created by Oleg Shaburov on 13.10.2018
 * shaburov.o.a@gmail.com
 */
public class OkHttpCallLoggingInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private final Consumer<String> logMethod;

    public OkHttpCallLoggingInterceptor(final Logger logger) {
        this(logger::info);
    }

    public OkHttpCallLoggingInterceptor(final Consumer<String> logMethod) {
        this.logMethod = logMethod;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request okHttpRequest = chain.request();
        StringJoiner requestHeaders = new StringJoiner("\n");
        okHttpRequest.headers().toMultimap().forEach((k, v) -> requestHeaders.add("    " + k + ": " + v));
        String requestBody = requestBodyToString(okHttpRequest.body());
        String requestMsg = String.format("Request:%n%s %s%nHeaders:%n%s%nBody:%n%s",
                okHttpRequest.method(), okHttpRequest.url(), requestHeaders.toString(), requestBody);
        logMethod.accept(requestMsg);
        Response response = chain.proceed(okHttpRequest);
        StringJoiner responseHeaders = new StringJoiner("\n");
        response.headers().toMultimap().forEach((k, v) -> responseHeaders.add("    " + k + ": " + v));
        String responseBody = responseBodyToString(response.body());
        String responseMsg = String.format("Response:%nCode: %s%nMessage: %s%nHeaders:%n%s%nBody:%n%s",
                response.code(), response.message(), responseHeaders.toString(), responseBody);
        logMethod.accept(responseMsg);
        return response;
    }

    private String requestBodyToString(RequestBody requestBody) throws IOException {
        if (requestBody != null) {
            Buffer bufferedSink = new Buffer();
            requestBody.writeTo(bufferedSink);
            return bufferedSink.clone().readString(UTF8);
        }
        return "<no request body>";
    }

    private String responseBodyToString(@Nullable ResponseBody responseBody) throws IOException {
        String result = "";
        if (responseBody != null) {
            responseBody.source();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();
            result = buffer.clone().readString(UTF8);
        }
        return result.isEmpty() ? "<no response body>" : result;
    }
}
