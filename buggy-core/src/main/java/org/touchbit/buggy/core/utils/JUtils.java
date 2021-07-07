package org.touchbit.buggy.core.utils;

import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.goal.Goal;
import org.touchbit.buggy.core.model.Suite;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

/**
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
public class JUtils {

    private JUtils() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }

    public static <T extends Goal> T getGoal(Function<Suite, Class<T>> function, Suite s) {
        Class<T> tClass = function.apply(s);
        return JUtils.newInstance(tClass, BuggyConfigurationException::new);
    }

    @SafeVarargs
    public static <T> List<T> getListWith(Supplier<T>... suppliers) {
        return Arrays.stream(suppliers).map(Supplier::get).collect(Collectors.toList());
    }

    @SafeVarargs
    public static <T> List<T> getListWith(T... tClasses) {
        return Arrays.stream(tClasses).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static <A, U extends Exception> A newInstance(String cName,
                                                         Class<A> exp,
                                                         BiFunction<String, Exception, U> s) throws U {
        try {
            Class<A> aClass = (Class<A>) getCurrentThreadClassLoader().loadClass(cName);
            return newInstance(aClass, s);
        } catch (Exception e) {
            throw s.apply("Unable to load class " + cName, e);
        }
    }

    public static ClassLoader getCurrentThreadClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static <R, U extends Exception> R newInstance(Class<R> t, BiFunction<String, Exception, U> s) throws U {
        try {
            return t.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw s.apply("Unable to create an instance of " + t.getSimpleName() + ".class", e);
        }
    }

    public static boolean isJetBrainsIdeTestNGPluginRun() {
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            if (stackTraceElement.toString().contains("com.intellij.rt.testng")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isJetBrainsIdeConsoleRun() {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        String xpcServiceName = System.getenv("XPC_SERVICE_NAME");
        if (xpcServiceName != null && xpcServiceName.contains("com.jetbrains.intellij")) {
            return true;
        }
        return arguments.stream()
                .anyMatch(v -> v.startsWith("-javaagent") &&
                        (v.contains("JetBrains") || v.contains("IDEA") || v.contains("idea")));
    }

    public static String getJetBrainsIdeConsoleRunTargetPath() {
        String runDir = System.getProperty("user.dir");
        if (isJetBrainsIdeConsoleRun()) {
            String javaClassPath = System.getProperty("java.class.path");
            if (javaClassPath != null && !javaClassPath.isEmpty()) {
                String firstClassPath = javaClassPath.split(":")[0];
                if (firstClassPath.contains("/target/") && firstClassPath.contains(runDir)) {
                    String moduleTarget = firstClassPath.replace(runDir, "")
                            .replace("classes", "");
                    if (moduleTarget.startsWith("/")) {
                        moduleTarget = moduleTarget.replaceFirst("/", "");
                    }
                    while (moduleTarget.endsWith("/")) {
                        moduleTarget = moduleTarget.substring(0, moduleTarget.length() - 1);
                    }
                    return moduleTarget;
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
        } catch (ClassNotFoundException ignore) {
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

    public static String getSimpleNameC(Object o) {
        return getSimpleNameC(o.getClass());
    }

    public static String getSimpleNameC(Class<?> c) {
        return getSimpleName(c) + ".class";
    }

    public static String getSimpleName(Object o) {
        return getSimpleName(o.getClass());
    }

    public static String getSimpleName(Class<?> c) {
        if (c != null) {
            return c.getSimpleName();
        }
        return "<null>";
    }

}
