package org.touchbit.buggy.feign;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.tests.BaseUnitTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

/**
 * Created by Oleg Shaburov on 13.10.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("FeignCallLogger class tests")
class FeignCallLoggerTests extends BaseUnitTest {

    @Test
    @DisplayName("Check FeignCallLogger().log")
    void unitTest_20181013222513() {
        FeignCallLogger feignCallLogger = new FeignCallLogger(UNIT_TEST_LOGGER);
        feignCallLogger.log("Class.method()", "format %s", "object");
        assertThat(UNIT_TEST_LOGGER.takeLoggedMessages(), contains("[Class.method] format object"));
    }

}
