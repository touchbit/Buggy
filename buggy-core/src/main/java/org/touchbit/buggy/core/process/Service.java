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

package org.touchbit.buggy.core.process;

import org.atteo.classindex.IndexSubclasses;

/**
 * The tested service included in (belongs to) the system component
 * <p>
 * Created by Oleg Shaburov on 15.05.2018
 * shaburov.o.a@gmail.com
 */
@IndexSubclasses
public abstract class Service implements Goal {

    public String getName() {
        return this.getClass().getSimpleName().toUpperCase();
    }

    public abstract String getDescription();

    @Override
    public String toString() {
        return getName();
    }

}
