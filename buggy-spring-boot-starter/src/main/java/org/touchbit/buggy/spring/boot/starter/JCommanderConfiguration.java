package org.touchbit.buggy.spring.boot.starter;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.*;
import org.touchbit.buggy.core.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.touchbit.buggy.core.utils.StringUtils.DOT;

@Configuration
@ConditionalOnNotWebApplication
@ComponentScan(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {IBuggyConfig.class}))
public class JCommanderConfiguration extends BaseConfiguration {

    private final List<IBuggyConfig> list;
    private final String[] args;
    private Map<Class<? extends IBuggyConfig>, IBuggyConfig> buggyConfigurations;

    public JCommanderConfiguration(List<IBuggyConfig> list, ApplicationArguments args) {
        this.list = list;
        this.args = args.getSourceArgs();
    }

    @Bean("initAndGetBuggyConfigurations")
    public synchronized Map<Class<? extends IBuggyConfig>, IBuggyConfig> initAndGetBuggyConfigurations() {
        if (buggyConfigurations == null) {
            StringUtils.bPrint();
            StringUtils.println("JCommander configuration construction");
            StringUtils.sPrint();
            buggyConfigurations = new HashMap<>();
            for (IBuggyConfig config : list) {
                buggyConfigurations.put(config.getClass(), config);
                StringUtils.println(StringUtils
                        .dotFiller(config.getClass().getSimpleName(), "OK"));
            }
            JCommander jc = new JCommander();
            addConfiguration(jc, buggyConfigurations.values());
            try {
                jc.parse(args);
                StringUtils.sPrint();
                StringUtils.fPrint("Parsing arguments", DOT, "OK");
                printConfigurationParams(buggyConfigurations);
            } catch (ParameterException e) {
                BuggyConfig.verbose = true;
                StringUtils.sPrint();
                StringUtils.fPrint("Parsing arguments", DOT, "FAIL");
                printConfigurationParams(buggyConfigurations);
                exitRun(e);
            }
        }
        return buggyConfigurations;
    }

    @Bean("isBuggyConfigured")
    public boolean isBuggyConfigured() {
        return buggyConfigurations.containsKey(BuggyConfig.class);
    }

    public void printConfigurationParams(Map<Class<? extends IBuggyConfig>, IBuggyConfig> configs) {
        for (IBuggyConfig autowiredConfig : list) {
            IBuggyConfig config = configs.get(autowiredConfig.getClass());
            String params = configurationToString(config);
            if (!params.isEmpty()) {
                StringUtils.sPrint();
                StringUtils.cPrint(config.getClass().getSimpleName());
                StringUtils.println(params);
            }
        }
    }

    private void addConfiguration(JCommander jc, Collection<IBuggyConfig> configs) {
        for (IBuggyConfig c : configs) {
            if (c.getClass().isAnnotationPresent(Parameters.class) &&
                    c.getClass().getAnnotation(Parameters.class).commandNames().length > 0) {
                jc.addCommand(c);
            } else {
                jc.addObject(c);
            }
        }
    }

    static String configurationToString(IBuggyConfig config) {
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

    static void addFieldValuesToMap(IBuggyConfig config, Map<String, Object> map) {
        Class<? extends IBuggyConfig> cClass = config.getClass();
        Field[] fields = ArrayUtils.addAll(cClass.getDeclaredFields(),
                cClass.getSuperclass().getDeclaredFields());
        for (Field field : fields) {
            Parameter parameter = field.getAnnotation(Parameter.class);
            if (parameter != null && (!parameter.hidden() || BuggyConfig.verbose)) {
                String[] names = parameter.names();
                try {
                    if (parameter.password()) {
                        map.put(Arrays.toString(names), "********");
                    } else {
                        field.setAccessible(true);
                        map.put(Arrays.toString(names), field.get(config));
                    }
                } catch (Exception e) {
                    if (BuggyConfig.verbose) {
                        StringUtils.println(e.getMessage());
                    }
                } finally {
                    field.setAccessible(false);
                }
            }
        }
    }

    static void addMethodsValuesToMap(IBuggyConfig config, Map<String, Object> map) {
        Class<? extends IBuggyConfig> cClass = config.getClass();
        List<Class<?>> interfaces = ClassUtils.getAllInterfaces(cClass);
        Set<Method> setMethods = new HashSet<>();
        Collections.addAll(setMethods, cClass.getDeclaredMethods());
        Collections.addAll(setMethods, cClass.getMethods());
        interfaces.forEach(i -> Collections.addAll(setMethods, i.getMethods()));
        interfaces.forEach(i -> Collections.addAll(setMethods, i.getDeclaredMethods()));
        Map<List<String>, Method> getMethodsMap = new HashMap<>();
        for (Method method : setMethods) {
            Parameter parameter = method.getAnnotation(Parameter.class);
            if (parameter != null && (!parameter.hidden() || BuggyConfig.verbose)) {
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
            } catch (Exception e) {
                if (BuggyConfig.verbose) {
                    StringUtils.println(e.getMessage());
                }
            }
        }
    }
}
