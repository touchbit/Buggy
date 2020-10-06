package org.touchbit.buggy.core.transport;

import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * Created by Oleg Shaburov on 13.10.2018
 * shaburov.o.a@gmail.com
 */
public abstract class CURLLoggingInterceptor {

    private final Consumer<String> logMethod;

    protected CURLLoggingInterceptor(final Consumer<String> logMethod) {
        this.logMethod = logMethod;
    }

    public <T extends Iterable<String>> void intercept(String method, String url, Map<String, T> headers, String body) {
        final StringBuilder curl = new StringBuilder();
        curl.append("curl -i -k");
        curl.append(" -X ").append(method);
        curl.append(" '").append(url).append("'");
        if (headers != null) {
            for (Map.Entry<String, T> h : headers.entrySet()) {
                if (h.getValue() != null && h.getValue().iterator().hasNext()) {
                    StringJoiner sj = new StringJoiner("; ", h.getKey() + ": ", "");
                    h.getValue().forEach(sj::add);
                    curl.append(" -H '").append(sj).append("'");
                }
            }
        }
        if (body != null) {
            curl.append(" --data '").append(body).append("'");
        }
        final String msg = "Playback curl:\n" + curl.toString();
        logMethod.accept(msg);
    }

}