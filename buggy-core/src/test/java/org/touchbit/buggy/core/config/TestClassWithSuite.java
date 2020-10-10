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
import org.touchbit.buggy.core.BaseBuggyTest;
import org.touchbit.buggy.core.model.Buggy;
import org.touchbit.buggy.core.model.Suite;

/**
 * Created by Oleg Shaburov on 20.09.2018
 * shaburov.o.a@gmail.com
 */
@Suite(component = TestComponent.class, service = TestService.class, interfaze = TestInterface.class, purpose = "unit_test")
public class TestClassWithSuite extends BaseBuggyTest {

    @Test(description = "test_20181021171954")
    @Buggy
    public void test_20181021171954() {
        step("Example step test_20181021171954");
    }

}
