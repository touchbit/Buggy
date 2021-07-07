package org.touchbit.buggy.core.tests.model;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.model.Status;
import org.touchbit.buggy.core.model.Type;
import org.touchbit.buggy.core.tests.BaseUnitTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by Oleg Shaburov on 16.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("Buggy models tests")
class ModelTests extends BaseUnitTest {

    @Nested()
    @DisplayName("Status enum tests")
    class StatusTests extends BaseUnitTest {

        @Test
        @DisplayName("Check statuses")
        void unitTest_20180916221521() {
            MatcherAssert.assertThat(Status.valueOf("EXP_IMPL"), is(notNullValue()));
            assertThat(Status.valueOf("EXP_FIX"), is(notNullValue()));
            assertThat(Status.valueOf("BLOCKED"), is(notNullValue()));
            assertThat(Status.valueOf("CORRUPTED"), is(notNullValue()));
            assertThat(Status.valueOf("FIXED"), is(notNullValue()));
            assertThat(Status.valueOf("IMPLEMENTED"), is(notNullValue()));
            assertThat(Status.valueOf("UNTESTED"), is(notNullValue()));
            assertThat(Status.valueOf("SUCCESS"), is(notNullValue()));
            assertThat(Status.valueOf("SKIP"), is(notNullValue()));
            assertThat(Status.valueOf("FAILED"), is(notNullValue()));
            assertThat(Status.values().length, is(10));
        }

    }

    @Nested()
    @DisplayName("Type enum tests")
    class TypeTests extends BaseUnitTest {

        @Test
        @DisplayName("Check types")
        void unitTest_20180916221800() {
            MatcherAssert.assertThat(Type.valueOf("SMOKE"), is(notNullValue()));
            assertThat(Type.valueOf("MODULE"), is(notNullValue()));
            assertThat(Type.valueOf("INTEGRATION"), is(notNullValue()));
            assertThat(Type.valueOf("SYSTEM"), is(notNullValue()));
            assertThat(Type.values().length, is(4));
        }

    }

}
