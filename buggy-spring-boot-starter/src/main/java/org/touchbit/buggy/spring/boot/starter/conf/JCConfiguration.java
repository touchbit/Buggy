package org.touchbit.buggy.spring.boot.starter.conf;

import com.beust.jcommander.Parameter;
import org.touchbit.buggy.spring.boot.starter.log.ConfigurationLogger;
import org.touchbit.buggy.spring.boot.starter.utils.JUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * An interface for finding all inherited {@link com.beust.jcommander.JCommander} configuration classes.
 * Search is performed using spring-boot annotation @ComponentScan.
 * <p>
 * All fields and setter-methods marked with {@link Parameter} annotation must be static.
 * <p>
 * Your config classes should be in the "buggy" package since ComponentScan
 * is limited to filter (basePackages = "**.buggy") to speed up the search for all inheritors.
 * For example: org.example.foo.bar.buggy.MyConfiguration.class (implements IBuggyConfig).
 * <p>
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
public interface JCConfiguration {

    /**
     * Convert configuration parameters and values to map where:
     * key - {@link Parameter#names()}
     * value - field or method value
     * @return map with configuration parameters names and values
     */
    default Map<String, Object> configurationToMap() {
        Map<String, Object> map = new HashMap<>();
        addFieldValuesToMap(map);
        addMethodsValuesToMap(map);
        Map<String, Object> sorted = new LinkedHashMap<>();
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(k -> sorted.put(k.getKey(), k.getValue()));
        return sorted;
    }

    /**
     * Method adds to Map {@link Parameter#names()} and values of annotated fields
     * @param map - configuration parameters and values map
     */
    default void addFieldValuesToMap(Map<String, Object> map) {
        for (Field field : JUtils.getFields(this)) {
            Parameter parameter = field.getAnnotation(Parameter.class);
            if (parameter != null && !parameter.hidden()) {
                String[] names = parameter.names();
                try {
                    if (parameter.password()) {
                        map.put(Arrays.toString(names), "*****");
                    } else {
                        field.setAccessible(true);
                        map.put(Arrays.toString(names), field.get(this));
                    }
                } catch (Exception e) {
                    ConfigurationLogger.errPrint(e.getMessage());
                } finally {
                    field.setAccessible(false);
                }
            }
        }
    }

    /**
     * Method adds to Map {@link Parameter#names()} and values of annotated methods
     * @param map - configuration parameters and values map
     */
    default void addMethodsValuesToMap(Map<String, Object> map) {
        List<Method> methods = JUtils.getMethods(this);
        Map<List<String>, Method> getters = new HashMap<>();
        for (Method method : methods) {
            Parameter parameter = method.getAnnotation(Parameter.class);
            if (parameter != null && !parameter.hidden()) {
                String[] names = parameter.names();
                if (parameter.password()) {
                    map.put(Arrays.toString(names), "*****");
                } else {
                    String mName = method.getName();
                    String is = "is" + mName.substring(2);
                    String get = "get" + mName.substring(3);
                    methods.stream()
                            .filter(m -> (m.getName().equalsIgnoreCase(is) || m.getName().equalsIgnoreCase(get)))
                            .forEach(g -> getters.put(new ArrayList<>(Arrays.asList(names)), g));
                }
            }
        }
        for (Map.Entry<List<String>, Method> method : getters.entrySet()) {
            try {
                method.getValue().setAccessible(true);
                map.put(method.getKey().toString(), method.getValue().invoke(this));
            } catch (Exception e) {
                ConfigurationLogger.errPrint(e.getMessage()); // TODO
            } finally {
                method.getValue().setAccessible(false);
            }
        }
    }

}
