/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.touchbit.buggy.core.exceptions;

import org.touchbit.buggy.core.model.Details;

/**
 * Created by Oleg Shaburov on 18.05.2018
 * shaburov.o.a@gmail.com
 */
public class ExpectedImplementationException extends BaseBuggyTestException {

    private static final String MSG = "The autotest is waiting for the implementation of the related test-case";

    public ExpectedImplementationException(Details details) {
        super(getMsg(MSG, details));
    }

    public ExpectedImplementationException() {
        super(MSG);
    }

    public ExpectedImplementationException(String msg) {
        super(msg);
    }

    public ExpectedImplementationException(String msg, Throwable e) {
        super(msg, e);
    }

    public ExpectedImplementationException(Throwable e) {
        super(e);
    }
}
