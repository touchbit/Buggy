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

package org.touchbit.buggy.core.config;

import com.beust.jcommander.IDefaultProvider;
import com.beust.jcommander.Parameter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.atteo.classindex.IndexSubclasses;
import org.touchbit.buggy.core.config.jcommander.InterfaceConverter;
import org.touchbit.buggy.core.config.jcommander.ParameterValidator;
import org.touchbit.buggy.core.config.jcommander.ServiceConverter;
import org.touchbit.buggy.core.config.jcommander.ValueValidator;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.process.Interface;
import org.touchbit.buggy.core.process.Service;
import org.touchbit.buggy.core.utils.BuggyUtils;
import org.touchbit.buggy.core.utils.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.touchbit.buggy.core.config.BParameters.*;

/**
 * Primary Config for jCommander
 * <p>
 * Created by Oleg Shaburov on 30.05.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings({"unused", "squid:S2696"})
@IndexSubclasses
public interface PrimaryConfig {

    default DefaultValueProvider getDefaultValueProvider() {
        return new DefaultValueProvider();
    }

    @Parameter(names = {QUESTION_MARK, HELP}, help = true, validateValueWith = ValueValidator.class,
            description = "Print usage.")
    default void setHelp(Boolean help) {
        // Do nothing
    }

    default Boolean isHelp() {
        return false;
    }

    @Parameter(names = {SMOKE}, description = "Running only smoke-tests.")
    default void setSmoke(Boolean smoke) {
        DefaultValueProvider.smoke = smoke;
    }

    default Boolean isSmoke() {
        return DefaultValueProvider.smoke;
    }

    @Parameter(names = {ALL}, hidden = true, description = "Print all configuration parameters.")
    default void setPrintAllParameters(Boolean printAllParameters) {
        DefaultValueProvider.printAll = printAllParameters;
    }

    default Boolean isPrintAllParameters() {
        return DefaultValueProvider.printAll;
    }

    @Parameter(names = {F, FORCE}, description = "Running all tests, including those that fall.")
    default void setForceRun(Boolean force) {
        DefaultValueProvider.force = force;
    }

    default Boolean isForceRun() {
        return DefaultValueProvider.force;
    }

    @Parameter(names = {THREADS}, description = "The number of threads to run the test methods.")
    default void setThreads(Integer threads) {
        DefaultValueProvider.threads = threads;
    }

    default Integer getThreads() {
        return DefaultValueProvider.threads;
    }

    @Parameter(names = {LOG}, hidden = true, description = "Absolute path to the directory for test logs.")
    default void setLogPath(String logDir) {
        DefaultValueProvider.logPath = logDir;
    }

    default String getLogPath() {
        return DefaultValueProvider.logPath;
    }

    @Parameter(names = {S, SERVICES}, description = "List of tested services in the format: NAME,NAME,NAME.",
            validateWith = ParameterValidator.class, listConverter = ServiceConverter.class)
    default void setServices(List<Service> services) {
        DefaultValueProvider.services = services;
    }

    default List<Service> getServices() {
        return DefaultValueProvider.services;
    }

    @Parameter(names = {I, INTERFACE}, description = "List of tested interfaces in the format: NAME,NAME,NAME.",
            validateWith = ParameterValidator.class, listConverter = InterfaceConverter.class)
    default void setInterfaces(List<Interface> interfaces) {
        DefaultValueProvider.interfaces = interfaces;
    }

    default List<Interface> getInterfaces() {
        return DefaultValueProvider.interfaces;
    }

    @Parameter(names = {T, TYPE}, description = "Type of tests to run.", validateWith = ParameterValidator.class)
    default void setType(Type type) {
        DefaultValueProvider.type = type;
    }

    default Type getType() {
        return DefaultValueProvider.type;
    }

    @Parameter(names = {STATUS}, hidden = true, description = "Completion with the specified status.")
    default void setStatus(Integer status) {
        DefaultValueProvider.status = status;
    }

    default Integer getStatus() {
        return DefaultValueProvider.status;
    }

    @Parameter(names = {N, NOTIFICATION}, description = "Notification in ...")
    default void setNotify(Boolean notify) {
        DefaultValueProvider.notify = notify;
    }

    default boolean isNotify() {
        return DefaultValueProvider.notify;
    }

    @Parameter(names = {ARTIFACTS_URL}, description = "The storage address for the builds (artifacts).")
    default void setArtifactsUrl(String artifactsUrl) {
        DefaultValueProvider.artifactsUrl = artifactsUrl;
    }

    default String getArtifactsUrl() {
        return DefaultValueProvider.artifactsUrl;
    }

    @Parameter(names = {PRINT_SUITE}, description = "Display information on the Suite in the console log.")
    default void setPrintSuite(Boolean printSuite) {
        DefaultValueProvider.printSuite = printSuite;
    }

    default Boolean isPrintSuite() {
        return DefaultValueProvider.printSuite;
    }

    @Parameter(names = {PRINT_CAUSE}, description = "Print the cause of a fail or skip test in the console log.")
    default void setPrintCause(Boolean printCause) {
        DefaultValueProvider.printCause = printCause;
    }

    default Boolean isPrintCause() {
        return DefaultValueProvider.printCause;
    }

    @Parameter(names = {PRINT_LOG}, description = "Print the test log file path in the console log")
    default void setPrintLogFile(Boolean printLog) {
        DefaultValueProvider.printLog = printLog;
    }

    default Boolean isPrintLogFile() {
        return DefaultValueProvider.printLog;
    }

    @Parameter(names = {V, VERSION}, description = "Print program version", validateValueWith = ValueValidator.class)
    default void setVersion(Boolean version) {
        // Do nothing
    }

    default boolean isVersion() {
        return false;
    }

    @Parameter(names = {SELF_CHECK}, description = "Check buggy configuration without test run.")
    default void setCheck(Boolean check) {
        DefaultValueProvider.check = check;
    }

    default Boolean isCheck() {
        return DefaultValueProvider.check;
    }

    default void setRunDir(String path) {
        DefaultValueProvider.runDir = path;
    }

    default String getRunDir() {
        return DefaultValueProvider.runDir;
    }

    default void setAbsoluteLogPath(String path) {
        DefaultValueProvider.absoluteLogPath = path;
    }

    default String getAbsoluteLogPath() {
        return DefaultValueProvider.absoluteLogPath;
    }

    default File getErrorLogDir() {
        return new File(getAbsoluteLogPath(), "errors");
    }

    default File getTestLogDir() {
        return new File(getAbsoluteLogPath(), "tests");
    }

    default File getFixedLogDir() {
        return new File(getAbsoluteLogPath(), "fixed");
    }

    default File getImplementedLogDir() {
        return new File(getAbsoluteLogPath(), "implemented");
    }

    default File getNewErrorLogDir() {
        return new File(getErrorLogDir(), "new");
    }

    default File getExpFixErrorLogDir() {
        return new File(getErrorLogDir(), "exp_fix");
    }

    default File getExpImplErrorLogDir() {
        return new File(getErrorLogDir(), "exp_impl");
    }

    default File getCorruptedErrorLogDir() {
        return new File(getErrorLogDir(), "corrupted");
    }

    default File getBlockedErrorLogDir() {
        return new File(getErrorLogDir(), "blocked");
    }

    @SuppressWarnings("WeakerAccess")
    class DefaultValueProvider implements IDefaultProvider {

        private static Boolean force = false;
        private static Boolean smoke = false;
        private static Boolean printAll = false;
        private static Boolean notify = false;
        private static Boolean printSuite = false;
        private static Boolean printCause = false;
        private static Boolean printLog = false;
        private static Boolean check = false;
        private static Integer threads = 50;
        private static Integer status;
        private static String  logPath = "logs";
        private static String  absoluteLogPath;
        private static String  runDir;
        private static String artifactsUrl;
        private static List<Service> services = BuggyUtils.findServices();
        private static List<Interface> interfaces = BuggyUtils.findInterfaces();
        private static Type type = Type.INTEGRATION;

        @Override
        public String getDefaultValueFor(String optionName) {
            PrimaryConfig p = new PrimaryConfig() {};
            switch (optionName) {
                case S:
                case SERVICES:
                    return String.valueOf(p.getServices());
                case I:
                case INTERFACE:
                    return String.valueOf(p.getInterfaces());
                case FORCE:
                case F:
                return String.valueOf(force);
                case SMOKE:
                return String.valueOf(smoke);
                case ALL:
                return String.valueOf(printAll);
                case N:
                case NOTIFICATION:
                return String.valueOf(notify);
                case THREADS:
                return String.valueOf(threads);
                case LOG:
                return String.valueOf(logPath);
                case ARTIFACTS_URL:
                return String.valueOf(artifactsUrl);
                case T:
                case TYPE:
                    return String.valueOf(type);
                case PRINT_SUITE:
                    return String.valueOf(printSuite);
                case PRINT_CAUSE:
                    return String.valueOf(printCause);
                case PRINT_LOG:
                    return String.valueOf(printLog);
                case SELF_CHECK:
                    return String.valueOf(check);
                default:
                    return null;
            }
        }
    }

    static <T extends PrimaryConfig> String configurationToString(T config) {
        Map<String, Object> map = new HashMap<>();
        addFieldValuesToMap(config, map);
        addMethodsValuesToMap(config, map);
        Map<String, Object> sorted = new LinkedHashMap<>();
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(k -> sorted.put(k.getKey(), k.getValue()));
        StringJoiner sj = new StringJoiner("\n");
        sorted.forEach((k, v) -> sj.add(StringUtils.dotFiller(k, 47, v)));
        return sj.toString();
    }

    static <T extends PrimaryConfig> void addFieldValuesToMap(T config, Map<String, Object> map) {
        Class<? extends PrimaryConfig> cClass = config.getClass();
        Field[] fields = ArrayUtils.addAll(cClass.getDeclaredFields(), cClass.getSuperclass().getDeclaredFields());
        for (Field field : fields) {
            Parameter parameter = field.getAnnotation(Parameter.class);
            if (parameter != null && (!parameter.hidden() || config.isPrintAllParameters())) {
                String[] names = parameter.names();
                try {
                    if (parameter.password()) {
                        map.put(Arrays.toString(names), "********");
                    } else {
                        map.put(Arrays.toString(names), field.get(config.getClass()));
                    }
                } catch (Exception ignore) { }
            }
        }
    }

    static <T extends PrimaryConfig> void addMethodsValuesToMap(T config, Map<String, Object> map) {
        Class<? extends PrimaryConfig> cClass = config.getClass();
        List<Class<?>> interfaces = ClassUtils.getAllInterfaces(cClass);
        Set<Method> setMethods = new HashSet<>();
        Collections.addAll(setMethods, cClass.getDeclaredMethods());
        Collections.addAll(setMethods, cClass.getMethods());
        interfaces.forEach(i -> Collections.addAll(setMethods, i.getMethods()));
        interfaces.forEach(i -> Collections.addAll(setMethods, i.getDeclaredMethods()));
        Map<List<String>, Method> getMethodsMap = new HashMap<>();
        for (Method method : setMethods) {
            Parameter parameter = method.getAnnotation(Parameter.class);
            if (parameter != null && (!parameter.hidden() || config.isPrintAllParameters())) {
                String[] names = parameter.names();
                if (parameter.password()) {
                    map.put(Arrays.toString(names), "********");
                } else {
                    setMethods.stream().filter(m ->
                            (m.getName().equalsIgnoreCase("is" + method.getName().substring(3)) ||
                                    m.getName().equalsIgnoreCase("get" + method.getName().substring(3))))
                            .forEach(k -> getMethodsMap.put(new ArrayList<>(Arrays.asList(names)), k));
                }
            }
        }
        for (Map.Entry<List<String>, Method> getMethod : getMethodsMap.entrySet()) {
            try {
                map.put(getMethod.getKey().toString(), getMethod.getValue().invoke(config));
            } catch (Exception ignore) { }
        }
    }

}
