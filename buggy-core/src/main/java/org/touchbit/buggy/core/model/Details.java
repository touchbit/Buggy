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

package org.touchbit.buggy.core.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.touchbit.buggy.core.model.Status.NONE;

/**
 * Interface of binding test methods.
 * Custom statuses for tests are provided.
 * <p>
 * Created by Oleg Shaburov on 16.05.2018
 * shaburov.o.a@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Details {

    /**
     * Test-case identifiers
     */
    String[] caseIDs() default {""};

    /**
     * Test status (see: {@link Status})
     * Used values: EXP_IMPL, EXP_FIX, BLOCKED, CORRUPTED
     */
    Status status() default NONE;

    /**
     * Issues ID in the task-tracker system. Format: "STORY-269"
     */
    String[] issues() default {};

    /**
     * Defects ID in the task-tracker system. Format: "BUG-354"
     */
    String[] bugs() default {};

    /**
     * Type of auto test
     */
    Type[] types() default {Type.REGRESSION};

}
