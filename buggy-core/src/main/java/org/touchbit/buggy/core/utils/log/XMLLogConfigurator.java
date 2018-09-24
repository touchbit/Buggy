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

package org.touchbit.buggy.core.utils.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.touchbit.buggy.core.utils.IOHelper.getFileFromResource;

/**
 * The class is designed to programmatically add loggers to the existing log4j2 configuration
 * <p>
 * Created by Oleg Shaburov on 17.05.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings({"WeakerAccess"})
public class XMLLogConfigurator {

    private static final LoggerContext CONTEXT = (LoggerContext) LogManager.getContext(false);

    public static Configuration reloadXMLConfiguration(String log4j2ConfigFile) {
        try {
            File file = getFileFromResource(log4j2ConfigFile);
            CONTEXT.setConfigLocation(file.toURI());
            try (InputStream targetStream = new FileInputStream(file)) {
                Config configuration = new Config(CONTEXT, new ConfigurationSource(targetStream));
                return configuration.getLoggerContext().getConfiguration();
            }
        } catch (Exception e) {
            throw new BuggyConfigurationException("Reload log4j2 configuration is failed. Configuration path: " +
                    log4j2ConfigFile, e);
        }
    }

    public static LoggerConfig getRootLoggerConfig() {
        return CONTEXT.getRootLogger().get();
    }

    private static class Config extends XmlConfiguration {

        private Config(final LoggerContext loggerContext, final ConfigurationSource configSource) {
            super(loggerContext, configSource);
            doConfigure();
        }

    }

    private XMLLogConfigurator() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }

}
