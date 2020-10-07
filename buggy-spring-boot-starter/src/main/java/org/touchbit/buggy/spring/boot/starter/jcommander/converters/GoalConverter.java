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
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.goal.Goal;

import java.lang.reflect.Constructor;
import java.util.LinkedHashSet;
import java.util.Set;

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
        Set<BeanDefinition> beanDefinitionInstances = scanBeanDefinitions(false, "**.buggy", tClass);
        Set<T> goalClasses = getBeanDefinitionInstances(beanDefinitionInstances, tClass);
        for (T goal : goalClasses) {
            if (goal.getClass().getSimpleName().equalsIgnoreCase(s)) {
                return goal;
            }
        }
        throw new BuggyConfigurationException("No " + tClass.getSimpleName() + " found with name " + s);
    }

    private  <T> Set<T> getBeanDefinitionInstances(Set<BeanDefinition> definitions, Class<T> tClass) {
        Set<T> result = new LinkedHashSet<>();
        for (BeanDefinition bd : definitions) {
            try {
                Class<?> c = BeanGenerator.class.getClassLoader().loadClass(bd.getBeanClassName());
                Constructor<?> con = c.getConstructor();
                T instance = (T) con.newInstance();
                result.add(instance);
            } catch (Exception e) {
                throw new BuggyConfigurationException("Can not create a new instance of " + tClass.getSimpleName());
            }
        }
        return result;
    }

    private Set<BeanDefinition> scanBeanDefinitions(boolean useDefaultFilters, String basePackage, Class<?> c) {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(useDefaultFilters);
        scanner.addIncludeFilter(new AssignableTypeFilter(c));
        try {
            return scanner.findCandidateComponents(basePackage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}