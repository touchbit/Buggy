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

import static org.touchbit.buggy.core.config.BParameters.*;
import static org.touchbit.buggy.core.utils.BuggyUtils.CONSOLE_DELIMITER;
import static org.touchbit.buggy.core.utils.StringUtils.underscoreFiller;

/**
 * Created by Oleg Shaburov on 19.09.2018
 * shaburov.o.a@gmail.com
 */
public class ValueValidator implements IValueValidator<Object> {

    @Override
    public void validate(String name, Object value) {
        // TODO remove
    }

}
