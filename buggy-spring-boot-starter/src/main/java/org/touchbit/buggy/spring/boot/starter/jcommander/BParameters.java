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

package org.touchbit.buggy.spring.boot.starter.jcommander;

/**
 * Created by Oleg Shaburov on 18.09.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("WeakerAccess")
public class BParameters {

    public static final String V = "-v";
    public static final String VERSION = "--version";
    public static final String QUESTION_MARK = "-?";
    public static final String HELP = "--help";
    public static final String C = "-c";
    public static final String COMPONENTS = "--component";
    public static final String S = "-s";
    public static final String SERVICES = "--service";
    public static final String I = "-i";
    public static final String INTERFACE = "--interface";
    public static final String T = "-t";
    public static final String TYPE = "--type";
    public static final String PARALLEL_MODE = "--parallel-mode";
    public static final String LOGS_PATH = "--logs-path";
    public static final String THREADS = "--threads";
    public static final String F = "-f";
    public static final String FORCE = "--force-run";
    public static final String ARTIFACTS_URL = "--artifacts-url";
    public static final String EXIT_STATUS = "--exit-status";
    public static final String ALL = "--all";
    public static final String PRINT_SUITE = "--print-suite";
    public static final String PRINT_CAUSE = "--print-cause";
    public static final String PRINT_LOG = "--print-log";
    public static final String PRINT_LOG_IF_FAIL = "--print-log-if-fail";
    public static final String PROGRAM_NAME = "--program-name";

    private BParameters() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }

}
