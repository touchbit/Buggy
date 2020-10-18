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

package org.touchbit.buggy.spring.boot.starter.jcommander.converters;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import static org.touchbit.buggy.spring.boot.starter.jcommander.CommandNames.*;

/**
 * Created by Oleg Shaburov on 08.09.2018
 * shaburov.o.a@gmail.com
 */
public class ParameterValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) {
        assertNotEmpty(name, value);
    }

    private void assertNotEmpty(String name, String value) {
        switch (name) {
            case C:
            case COMPONENTS:
            case S:
            case SERVICES:
            case I:
            case INTERFACES:
            case T:
            case TYPE:
                if (value == null || value.isEmpty()) {
                    throw new ParameterException("Parameter " + name + " can not be empty");
                }
                break;
            default:
                // do nothing
        }
    }
}