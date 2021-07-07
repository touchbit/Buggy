package org.touchbit.buggy.spring.boot.starter.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.utils.JUtils;

import java.util.LinkedHashSet;
import java.util.Set;

public class BeanScanner {

    private BeanScanner() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }

    public static <A> Set<A> getBeanDefinitionInstances(Class<A> tClass) {
        Set<BeanDefinition> definitions = scanBeanDefinitions(tClass);
        Set<A> result = new LinkedHashSet<>();
        for (BeanDefinition bd : definitions) {
            A instance = JUtils.newInstance(bd.getBeanClassName(), tClass, BuggyConfigurationException::new);
            result.add(instance);
        }
        return result;
    }

    public static <A> Set<A> getBeanDefinitionInstances(Set<BeanDefinition> definitions, Class<A> tClass) {
        Set<A> result = new LinkedHashSet<>();
        for (BeanDefinition bd : definitions) {
            A instance = JUtils.newInstance(bd.getBeanClassName(), tClass, BuggyConfigurationException::new);
            result.add(instance);
        }
        return result;
    }

    public static Set<BeanDefinition> scanBeanDefinitions(Class<?> c) {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(c));
        try {
            return scanner.findCandidateComponents("**.buggy");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
