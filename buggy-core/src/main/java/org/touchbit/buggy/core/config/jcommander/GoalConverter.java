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

import com.beust.jcommander.IStringConverter;
import org.atteo.classindex.ClassIndex;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.process.Goal;

/**
 * Created by Oleg Shaburov on 08.09.2018
 * shaburov.o.a@gmail.com
 */
public final class GoalConverter<T extends Goal> implements IStringConverter<T> {

    private final Class<T> tClass;

    public GoalConverter(final Class<T> t) {
        tClass = t;
    }

    @Override
    public T convert(String s) {
        Iterable<Class<? extends T>> goalClasses = ClassIndex.getSubclasses(tClass);
        for (Class<? extends T> goal : goalClasses) {
            try {
                if (goal.getSimpleName().equalsIgnoreCase(s)) {
                    return goal.newInstance();
                }
            } catch (Exception e) {
                throw new BuggyConfigurationException("Can not create a new instance of " + goal +
                        " inheriting from class " + tClass.getSimpleName());
            }
        }
        throw new BuggyConfigurationException("No " + tClass.getSimpleName() + " found with name " + s);
    }

}