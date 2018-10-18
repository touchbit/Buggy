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

package org.touchbit.buggy.example.min.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.test.BaseBuggyTest;
import org.touchbit.buggy.example.min.goals.API;
import org.touchbit.buggy.example.min.goals.GitLab;

/**
 * Created by Oleg Shaburov on 18.09.2018
 * shaburov.o.a@gmail.com
 */
@Suite(service = GitLab.class, interfaze = API.class, task = "common")
@SuppressWarnings("squid:S00100")
public class GitLabTests extends BaseBuggyTest {

    private static final String DO_SOMETHING = "do something";

    @BeforeClass(description = "Example description for configuration method")
    public void beforeClassConfigurationMethod() {
        step("Prepare domain");
        log.info(DO_SOMETHING);
        step("Prepare company");
        log.info(DO_SOMETHING);
        step("Prepare admin account");
        log.info(DO_SOMETHING);
    }

    @Test(description = "Example description for test_20180918045754")
    @Details(status = Status.CORRUPTED)
    public void test_20180918045754() {
        step("Example step");
        log.info(DO_SOMETHING);
    }

}
