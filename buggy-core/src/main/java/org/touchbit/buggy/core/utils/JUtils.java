package org.touchbit.buggy.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import static org.touchbit.buggy.core.utils.BuggyUtils.CONSOLE_DELIMITER;
import static org.touchbit.buggy.core.utils.StringUtils.underscoreFiller;

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
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Set<Class<?>> interfaces = getAllInterfacesForClassAsSet(c, contextClassLoader);
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
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Set<Class<?>> interfaces = getAllInterfacesForClassAsSet(c, contextClassLoader);
        List<Method> methods = new ArrayList<>();
        methods.addAll(Arrays.asList(c.getDeclaredMethods()));
        methods.addAll(Arrays.asList(c.getMethods()));
        interfaces.forEach(i -> methods.addAll(Arrays.asList(i.getDeclaredMethods())));
        interfaces.forEach(i -> methods.addAll(Arrays.asList(i.getMethods())));
        return methods;
    }

    public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz, ClassLoader classLoader) {
        if (clazz.isInterface() && isLoadable(clazz, classLoader)) {
            return Collections.singleton(clazz);
        }
        Set<Class<?>> interfaces = new LinkedHashSet<>();
        Class<?> current = clazz;
        while (current != null) {
            Class<?>[] ifcs = current.getInterfaces();
            interfaces.addAll(Arrays.asList(ifcs));
            current = current.getSuperclass();
        }
        return interfaces;
    }

    private static boolean isLoadable(Class<?> clazz, ClassLoader classLoader) {
        try {
            return (clazz == classLoader.loadClass(clazz.getName()));
        }
        catch (ClassNotFoundException ignore) {
            return false;
        }
    }

    public static Map<String, String> getBuggyManifest() {
        return getManifestAttributesInfo("Buggy-");
    }

    public static Map<String, String> getManifestAttributesInfo(String prefix) {
        return getManifestAttributesInfo().entrySet().stream()
                .filter(e -> String.valueOf(e.getKey()).startsWith(prefix) && e.getValue() != null)
                .collect(Collectors.toMap(e -> e.getKey().replace(prefix, ""), Map.Entry::getValue));
    }

    public static Map<String, String> getManifestAttributesInfo() {
        Attributes attributes = getManifestAttributes();
        if (attributes != null) {
            return attributes.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> String.valueOf(e.getKey()),
                            e -> String.valueOf(e.getValue())));
        }
        return new HashMap<>();
    }

    public static Attributes getManifestAttributes() {
        Manifest mf = getManifest();
        return mf.getMainAttributes();
    }

    public static Manifest getManifest() {
        InputStream inputStream = IOHelper.getResourceAsStream("./META-INF/MANIFEST.MF");
        if (inputStream == null) {
            inputStream = IOHelper.getResourceAsStream("META-INF/MANIFEST.MF");
        }
        return readManifest(inputStream);
    }

    public static Manifest readManifest(InputStream inputStream) {
        Manifest manifest = new Manifest();
        try {
            if (inputStream != null) {
                manifest.read(inputStream);
            }
            return manifest;
        } catch (IOException ignore) {
            return manifest;
        }
    }

}
