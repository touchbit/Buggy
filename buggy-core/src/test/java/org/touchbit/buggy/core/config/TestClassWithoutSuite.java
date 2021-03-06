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

import org.testng.annotations.Test;
import org.touchbit.buggy.core.model.Details;
import org.touchbit.buggy.core.test.BaseBuggyTest;

/**
 * Created by Oleg Shaburov on 20.09.2018
 * shaburov.o.a@gmail.com
 */
public class TestClassWithoutSuite extends BaseBuggyTest {

    @Test(description = "test_20180920150514")
    @Details
    public void test_20180920150514() {
        step("Example step test_20180920150514");
    }

}
