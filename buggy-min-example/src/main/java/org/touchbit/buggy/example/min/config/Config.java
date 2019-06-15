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

import org.touchbit.buggy.core.config.PrimaryConfig;
import org.touchbit.buggy.testrail.BaseTestRailConfig;

/**
 * Created by Oleg Shaburov on 18.09.2018
 * shaburov.o.a@gmail.com
 */
public class Config implements PrimaryConfig, BaseTestRailConfig {

    public Config() {
        setPrintLogFile(true);
        setPrintCause(true);
        setPrintSuite(true);
        BaseTestRailConfig.setTestRailHost("https://touchbit.org/testrail");
        BaseTestRailConfig.setLogin("automation@touchbit.org");
        BaseTestRailConfig.setPass("automation");
    }

}
