package org.touchbit.buggy.spring.boot.starter.utils;

import org.springframework.util.ClassUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
public class JUtils {

    private JUtils() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }

    public static boolean isJetBrainsIdeRun() {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        return arguments.stream()
                .anyMatch(v -> v.startsWith("-javaagent") &&
                        (v.contains("JetBrains") || v.contains("IDEA") || v.contains("idea")));
    }

    public static String getRunPath() {
        String runDir = System.getProperty("user.dir");
        if (isJetBrainsIdeRun()) {
            String javaClassPath = System.getProperty("java.class.path");
            if (javaClassPath != null && !javaClassPath.isEmpty()) {
                String firstClassPath = javaClassPath.split(":")[0];
                if (firstClassPath.contains("/target/") && firstClassPath.contains(runDir)) {
                    return firstClassPath.substring(0, firstClassPath.indexOf("/target/")) + "/target";
                }
            }
        }
        return runDir;
    }

    public static List<Field> getFields(Object o) {
        if (o != null) {
            return getFields(o.getClass());
        }
        return new ArrayList<>();
    }

    public static List<Field> getFields(Class<?> c) {
        Set<Class<?>> interfaces = ClassUtils.getAllInterfacesAsSet(c);
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(c.getDeclaredFields()));
        fields.addAll(Arrays.asList(c.getFields()));
        interfaces.forEach(i -> fields.addAll(Arrays.asList(i.getDeclaredFields())));
        interfaces.forEach(i -> fields.addAll(Arrays.asList(i.getFields())));
        return fields;
    }

    public static List<Method> getMethods(Object o) {
        if (o != null) {
            return getMethods(o.getClass());
        }
        return new ArrayList<>();
    }

    public static List<Method> getMethods(Class<?> c) {
        Set<Class<?>> interfaces = ClassUtils.getAllInterfacesAsSet(c);
        List<Method> methods = new ArrayList<>();
        methods.addAll(Arrays.asList(c.getDeclaredMethods()));
        methods.addAll(Arrays.asList(c.getMethods()));
        interfaces.forEach(i -> methods.addAll(Arrays.asList(i.getDeclaredMethods())));
        interfaces.forEach(i -> methods.addAll(Arrays.asList(i.getMethods())));
        return methods;
    }

}
