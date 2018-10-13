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

package org.touchbit.buggy.transport.utils;

import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by Oleg Shaburov on 13.10.2018
 * shaburov.o.a@gmail.com
 */
public class InterceptorHelper {

    public static <T extends Iterable<String>> String getCurl(final String method,
                                                              final String url,
                                                              final Map<String, T> headers,
                                                              final String body) {
        final StringBuilder curl = new StringBuilder();
        curl.append("curl -i -k");
        curl.append(" -X ").append(method);
        curl.append(" '").append(url).append("'");
        if (headers != null) {
            for (Map.Entry<String, T> h : headers.entrySet()) {
                if (h.getValue() != null && h.getValue().iterator().hasNext()) {
                    StringJoiner sj = new StringJoiner("; ", h.getKey() + ": ", "");
                    h.getValue().forEach(sj::add);
                    curl.append(" -h '").append(sj).append("'");
                }
            }
        }
        if (body != null) {
            curl.append(" --data '").append(body).append("'");
        }
        return curl.toString();
    }

    /**
     * Utility class. Prohibit instantiation.
     */
    private InterceptorHelper() { }

}
