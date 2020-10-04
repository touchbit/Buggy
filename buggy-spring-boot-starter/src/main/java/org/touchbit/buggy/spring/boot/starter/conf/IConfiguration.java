package org.touchbit.buggy.spring.boot.starter.conf;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.touchbit.buggy.spring.boot.starter.BuggyRunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.lang.reflect.Modifier.*;

/**
 * Interface for Buggy spring boot configurations
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
public interface IConfiguration {

    @SuppressWarnings("unchecked")
    default Set<BeanDefinition> scanBeanDefinitions(boolean useDefaultFilters, String basePackage, Class<?> c) {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(useDefaultFilters);
        if (c.isAnnotation()) {
            Class<Annotation> annotation = (Class<Annotation>) c;
            scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));
        } else {
            scanner.addIncludeFilter(new AssignableTypeFilter(c));
        }
        try {
            return scanner.findCandidateComponents(basePackage);
        } catch (Exception e) {
            BuggyRunner.exit("Could not find beans of class " + c.getSimpleName() +
                    " in package [" + basePackage + "]", e);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    default <T> Set<T> getBeanDefinitionInstances(Set<BeanDefinition> definitions, Class<T> tClass) {
        Set<T> result = new LinkedHashSet<>();
        for (BeanDefinition bd : definitions) {
            try {
                Class<?> c = BeanGenerator.class.getClassLoader().loadClass(bd.getBeanClassName());
                Constructor<?> con = c.getConstructor();
                T instance = (T) con.newInstance();
                result.add(instance);
            } catch (Exception e) {
                BuggyRunner.exit("Could not load class for assigned type: " + tClass.getSimpleName(), e);
            }
        }
        return result;
    }

    default Set<Class<?>> getBeanDefinitionAnnotatedClasses(Set<BeanDefinition> definitions, Class<?> tClass) {
        Set<Class<?>> result = new LinkedHashSet<>();
        for (BeanDefinition bd : definitions) {
            try {
                Class<?> testClass = BeanGenerator.class.getClassLoader().loadClass(bd.getBeanClassName());
                int mods = testClass.getModifiers();
                if (isPublic(mods) && !isAbstract(mods) && !isInterface(mods)) {
                    result.add(testClass);
                }
            } catch (ClassNotFoundException e) {
                BuggyRunner.exit("Could not load class for annotation type: " + tClass.getSimpleName(), e);
            }
        }
        return result;
    }

}
