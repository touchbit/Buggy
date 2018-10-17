package org.touchbit.buggy.feign;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.BaseUnitTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Oleg Shaburov on 13.10.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("FeignCallLogger class tests")
class FeignCallLoggerTests extends BaseUnitTest {

    @Test
    @DisplayName("Check FeignCallLogger().log")
    void unitTest_20181013222513() {
        Log log = new Log();
        FeignCallLogger feignCallLogger = new FeignCallLogger(log);
        feignCallLogger.log("Class.method()", "format %s", "object");
        assertThat(log.msg, is("[Class.method] format object"));
    }

}
