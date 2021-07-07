package org.touchbit.buggy.core.snakeyaml;

import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.FieldProperty;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Allow
 * <p>
 * Created by Oleg Shaburov on 10.10.2020
 * shaburov.o.a@gmail.com
 */
public class BuggyYamlPropertyUtils extends PropertyUtils {

    protected Map<String, Property> getPropertiesMap(Class<?> type, BeanAccess bAccess) {
        Map<String, Property> properties = new LinkedHashMap<String, Property>();
        if (bAccess == BeanAccess.FIELD) {
            for (Class<?> c = type; c != null; c = c.getSuperclass()) {
                for (Field field : c.getDeclaredFields()) {
                    int modifiers = field.getModifiers();
                    if (!Modifier.isTransient(modifiers) && !properties.containsKey(field.getName())) {
                        properties.put(field.getName(), new FieldProperty(field));
                    }
                }
            }
        } else {
            throw new BuggyConfigurationException("Supported only BeanAccess.FIELD");
        }
        return properties;
    }

}
