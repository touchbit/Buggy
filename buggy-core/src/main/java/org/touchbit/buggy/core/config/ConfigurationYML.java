package org.touchbit.buggy.core.config;

import com.google.common.base.CaseFormat;
import org.touchbit.buggy.core.snakeyaml.BuggyYamlPropertyUtils;
import org.touchbit.buggy.core.utils.JUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.util.Map;
import java.util.stream.Collectors;

import static org.yaml.snakeyaml.introspector.BeanAccess.FIELD;

/**
 * Created by Oleg Shaburov on 01.10.2020
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("unused")
public interface ConfigurationYML {

    String BUGGY_CONFIG_YML = "buggy-config.yml";

    static void initConfigurationYml(Class<? extends ConfigurationYML> configClass) {
        InputStream inputStream = JUtils.getCurrentThreadClassLoader().getResourceAsStream(BUGGY_CONFIG_YML);
        getYamlLoader(configClass).load(inputStream);
    }

    static Yaml getYamlLoader(Class<? extends ConfigurationYML> configClass) {
        Representer representer = new Representer();
        BuggyYamlPropertyUtils propertyUtils = new BuggyYamlPropertyUtils();
        propertyUtils.setSkipMissingProperties(true);
        propertyUtils.setBeanAccess(FIELD);
        representer.setPropertyUtils(propertyUtils);
        Constructor constructor = new Constructor(configClass);
        return new Yaml(constructor, representer);
    }

}
