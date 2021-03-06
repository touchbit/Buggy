package org.touchbit.buggy.feign;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.tests.BaseUnitTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
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
        FeignCallLogger feignCallLogger = new FeignCallLogger(TEST_LOGGER);
        feignCallLogger.log("Class.method()", "format %s", "object");
        assertThat(TEST_LOGGER.takeLoggedMessages(), contains("[Class.method] format object"));
    }

    @Test
    @DisplayName("WHEN FeignCallLogger() THEN no exception")
    void unitTest_20181028160734() {
        FeignCallLogger feignCallLogger = new FeignCallLogger();
        feignCallLogger.log("Class.method()", "format %s", "object");
        assertThat(TEST_LOGGER.takeLoggedMessages(), is(empty()));
    }

}
