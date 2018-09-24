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

import com.beust.jcommander.converters.IParameterSplitter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleg Shaburov on 08.09.2018
 * shaburov.o.a@gmail.com
 */
public final class ArraySplitter implements IParameterSplitter {

    @Override
    public List<String> split(String s) {
        List<String> result = new ArrayList<>();
        String[] element = s.replace("[", "").replace("]", "").split(",");
        for (String s1 : element) {
            if (s1.trim().length() > 0) {
                result.add(s1.trim().toUpperCase());
            }
        }
        return result;
    }

}
