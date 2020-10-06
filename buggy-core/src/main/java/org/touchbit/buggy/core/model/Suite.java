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

import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.component.DefaultComponent;
import org.touchbit.buggy.core.goal.interfaze.DefaultInterface;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.DefaultService;
import org.touchbit.buggy.core.goal.service.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Oleg Shaburov on 19.05.2018
 * shaburov.o.a@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Suite {

    /**
     * The component to be tested
     */
    Class<? extends Component> component() default DefaultComponent.class;

    /**
     * The test service included in the component
     */
    Class<? extends Service> service() default DefaultService.class;

    /**
     * The interface on which the test is performed
     */
    Class<? extends Interface> interfaze() default DefaultInterface.class;

    /**
     * The purpose of the tests included in the test class. (example: 'update_user' or 'upload_pdf')
     */
    String purpose() default "";

}
