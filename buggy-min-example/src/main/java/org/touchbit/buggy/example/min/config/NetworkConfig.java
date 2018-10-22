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

package org.touchbit.buggy.example.min.config;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.touchbit.buggy.core.config.SecondaryConfig;

import static org.touchbit.buggy.example.min.config.NetworkConfig.DefaultValues.*;

/**
 * Created by Oleg Shaburov on 02.09.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("squid:S1118")
@Parameters(commandNames = "network")
public class NetworkConfig implements SecondaryConfig {

    @SuppressWarnings("WeakerAccess")
    protected static class DefaultValues {
        protected static final String WRITE_TIMEOUT      = "--write-timeout";
        protected static final String READ_TIMEOUT       = "--read-timeout";
        protected static final String CONNECTION_TIMEOUT = "--connection-timeout";
        protected static final String TEST_HOST = "--host";

        private static int readTimeout = 10;
        private static int writeTimeout = 10;
        private static int connectionTimeout = 10;
        private static String host = "http://example.com";

        private DefaultValues() {
            throw new IllegalStateException("Utility class. Prohibit instantiation.");
        }
    }

    @Parameter(names = {READ_TIMEOUT}, description = "Read timeout for response")
    public static void setReadTimeout(int readTimeout) {
        DefaultValues.readTimeout = readTimeout;
    }

    public static int getReadTimeout() {
        return DefaultValues.readTimeout;
    }

    @Parameter(names = {WRITE_TIMEOUT}, description = "Write timeout for request")
    public static void setWriteTimeout(int writeTimeout) {
        DefaultValues.writeTimeout = writeTimeout;
    }

    public static int getWriteTimeout() {
        return DefaultValues.writeTimeout;
    }

    @Parameter(names = {CONNECTION_TIMEOUT}, description = "Connection timeout for request")
    public static void setConnectionTimeout(int writeTimeout) {
        DefaultValues.connectionTimeout = writeTimeout;
    }

    public static int getConnectionTimeout() {
        return DefaultValues.connectionTimeout;
    }

    @Parameter(names = {TEST_HOST}, description = "Tested host")
    public static void setHost(String host) {
        DefaultValues.host = host;
    }

    public static String getHost() {
        return DefaultValues.host;
    }

}
