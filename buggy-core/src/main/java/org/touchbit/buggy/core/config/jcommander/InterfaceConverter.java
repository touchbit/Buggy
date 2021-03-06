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

import com.beust.jcommander.converters.DefaultListConverter;
import org.touchbit.buggy.core.process.Interface;

/**
 * Created by Oleg Shaburov on 19.05.2018
 * shaburov.o.a@gmail.com
 */
public final class InterfaceConverter extends DefaultListConverter<Interface> {

    public InterfaceConverter() {
        super(new ArraySplitter(), new GoalConverter<>(Interface.class));
    }

}
