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

package org.touchbit.buggy.core.testng.listeners;

import org.testng.ITestNGListener;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.config.PrimaryConfig;
import org.touchbit.buggy.core.utils.ExitHandlerExpectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.touchbit.buggy.core.utils.IOHelper.readPropertiesFileFromResource;

/**
 * Intellij IDEA TestNG Plugin Listener
 * <p>
 * Created by Oleg Shaburov on 15.05.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings("WeakerAccess")
public final class IntellijIdeaTestNgPluginListener extends BuggyExecutionListener implements ITestNGListener {

    public static final String INTELLIJ_IDEA_TEST_RUN = "intellij.idea.test.run";

    @SuppressWarnings("StatementWithEmptyBody")
    public IntellijIdeaTestNgPluginListener() {
        System.setProperty(INTELLIJ_IDEA_TEST_RUN, Boolean.TRUE.toString());
        Properties properties = readPropertiesFileFromResource("buggy.properties");
        List<String> list = new ArrayList<>();
        properties.forEach((k, v) -> {
            String key = String.valueOf(k);
            String val = String.valueOf(v);
            // option key
            if (key.startsWith("-")) {
                list.add(key);
                // option value
                if (!val.isEmpty()) {
                    list.add(val);
                }
            } else {
                if (key.startsWith("buggy.primary.config.class")) {
                    Buggy.setPrimaryConfigClass(getPrimaryConfig(val));
                }
            }
        });
        String[] args = list.toArray(new String[0]);
        Buggy.prepare(args);
    }

    @SuppressWarnings("unchecked")
    public Class<PrimaryConfig> getPrimaryConfig(String val) {
        try {
            return  (Class<PrimaryConfig>) this.getClass().getClassLoader().loadClass(val);
        } catch (Exception e) {
            Buggy.getExitHandler().exitRun(1, "Unable to create a new instance of PrimaryConfig class", e);
        }
        throw new ExitHandlerExpectedException();
    }

    @Override
    public void onExecutionStart() {
        super.onExecutionStart();
        // fix incorrect Intellij Idea Test Run Console output
        consoleLog.info("");
    }

    @Override
    public final boolean isEnable() {
        return false;
    }

    public static Boolean isIntellijIdeaTestRun() {
        return Boolean.valueOf(System.getProperty(INTELLIJ_IDEA_TEST_RUN, Boolean.FALSE.toString()));
    }

}
