package org.touchbit.buggy.core.config;

import com.beust.jcommander.Parameter;
import org.touchbit.buggy.core.logback.FrameworkLogger;
import org.touchbit.buggy.core.utils.JUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public interface JCommand extends ConfigurationYML {

    /**
     * Convert configuration parameters and values to map where:
     * key - {@link Parameter#names()}
     * value - field or method value
     *
     * @return map with configuration parameters names and values
     */
    default Map<String, Object> configurationToMap() {
        Map<String, Object> map = new HashMap<>();
        addFieldValuesToMap(map);
        addMethodsValuesToMap(map);
        return sort(map);
    }

    /**
     * Method adds to Map {@link Parameter#names()} and values of annotated fields
     *
     * @param map - configuration parameters and values map
     */
    default void addFieldValuesToMap(Map<String, Object> map) {
        for (Field field : JUtils.getFields(this)) {
            Parameter parameter = field.getAnnotation(Parameter.class);
            if (parameter != null && !parameter.hidden()) {
                String[] names = parameter.names();
                String parameters = Arrays.toString(names);
                try {
                    if (parameter.password()) {
                        map.put(parameters, "*****");
                    } else {
                        field.setAccessible(true);
                        map.put(parameters, field.get(this));
                    }
                } catch (Exception e) {
                    map.put(parameters, "<ERROR>");
                    new FrameworkLogger().error("Error getting value from field " +
                            field.getDeclaringClass().getSimpleName() + "#" + field.getName(), e);
                } finally {
                    field.setAccessible(false);
                }
            }
        }
    }

    /**
     * Method adds to Map {@link Parameter#names()} and values of annotated methods
     *
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
                    String is = "is" + mName.substring(3);
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
                map.put(method.getKey().toString(), "<ERROR>");
                Method m = method.getValue();
                StringJoiner sj = new StringJoiner(", ", "(", ")");
                Arrays.stream(m.getParameterTypes()).forEach(p -> sj.add(p.getSimpleName()));
                new FrameworkLogger().error("Error getting value from method " +
                        m.getDeclaringClass().getSimpleName() + "#" + m.getName() + sj.toString(), e);
            } finally {
                method.getValue().setAccessible(false);
            }
        }
    }

    /**
     * Default no sort
     *
     * @param map - unsorted map
     * @return - sorted map
     */
    default Map<String, Object> sort(Map<String, Object> map) {
        return map;
    }

}
