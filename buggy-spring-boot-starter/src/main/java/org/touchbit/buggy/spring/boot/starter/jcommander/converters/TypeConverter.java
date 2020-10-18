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

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.converters.StringConverter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.spring.boot.starter.util.BeanScanner;

import java.util.Set;

/**
 * Created by Oleg Shaburov on 08.09.2018
 * shaburov.o.a@gmail.com
 */
public final class TypeConverter implements IStringConverter<Type> {

    @Override
    public Type convert(String s) {
        System.out.println(" >>>>>>>>>>>>>>>>>>> s >>> " + s);
        Set<BeanDefinition> beanDefinitionInstances = BeanScanner.scanBeanDefinitions(Type.class);
        Set<Type> types = BeanScanner.getBeanDefinitionInstances(beanDefinitionInstances, Type.class);
        System.out.println(" >>>>> types >>> " + types);
        for (Type type : types) {
            if (type.name().equalsIgnoreCase(s)) {
                System.out.println(" >>>>> type >>> " + type + " >> " + type.getClass());
                return type;
            }
        }
        throw new BuggyConfigurationException("No " + Type.class.getSimpleName() + " found with name " + s);
    }

}