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

package org.touchbit.buggy.core.config.jcommander;

import com.beust.jcommander.IValueValidator;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.utils.BuggyUtils;
import org.touchbit.buggy.core.utils.StringUtils;

import java.util.StringJoiner;
import java.util.jar.Attributes;

import static org.touchbit.buggy.core.config.Parameters.*;
import static org.touchbit.buggy.core.utils.BuggyUtils.CONSOLE_DELIMITER;
import static org.touchbit.buggy.core.utils.StringUtils.underscoreFiller;

/**
 * Created by Oleg Shaburov on 19.09.2018
 * shaburov.o.a@gmail.com
 */
public class ValueValidator implements IValueValidator {

    @Override
    public void validate(String name, Object value) {
        String stringValue = String.valueOf(value);
        if ((name.equals(QUESTION_MARK) || name.equals(HELP)) && Boolean.valueOf(stringValue)) {
            Buggy.exitRunWithUsage(0);
        }
        if ((name.equals(V) || name.equals(VERSION)) && Boolean.valueOf(String.valueOf(stringValue))) {
            printManifestInfo();
            Buggy.exitRun(0);
        }
    }

    private static void printManifestInfo() {
        StringJoiner stringJoiner = new StringJoiner("\n");
        Attributes atts = BuggyUtils.getManifestAttributes();
        if (atts != null) {
            atts.entrySet().stream()
                    .filter(e -> String.valueOf(e.getKey()).startsWith("Implementation") && e.getValue() != null)
                    .forEach(e -> stringJoiner.add(
                            underscoreFiller(String.valueOf(e.getKey()).replace("Implementation-", ""),
                                    47, e.getValue())));
            if (stringJoiner.length() > 0) {
                StringUtils.println(stringJoiner.toString());
                StringUtils.println(CONSOLE_DELIMITER);
            }
        }
    }

}
