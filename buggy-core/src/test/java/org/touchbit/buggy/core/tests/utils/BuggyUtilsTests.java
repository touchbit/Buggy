package org.touchbit.buggy.core.tests.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.config.TestComponent;
import org.touchbit.buggy.core.config.TestInterface;
import org.touchbit.buggy.core.config.TestService;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.tests.BaseUnitTest;
import org.touchbit.buggy.core.utils.IOHelper;
import org.touchbit.buggy.core.utils.JUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.jar.Manifest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by Oleg Shaburov on 15.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("JUtils Tests")
class JUtilsTests extends BaseUnitTest {

    private static final Suite SUITE_1 = new Suite() {

        @Override
        public Class<? extends Annotation> annotationType() {
            return Suite.class;
        }

        @Override
        public Class<? extends Component> component() {
            return TestComponent.class;
        }

        @Override
        public Class<? extends Service> service() {
            return TestService.class;
        }

        @Override
        public Class<? extends Interface> interfaze() {
            return TestInterface.class;
        }

        @Override
        public String purpose() {
            return "TestTask";
        }
    };
    private static final Suite SUITE_2 = new Suite() {

        @Override
        public Class<? extends Annotation> annotationType() {
            return Suite.class;
        }

        @Override
        public Class<? extends Component> component() {
            return TestComponent.class;
        }

        @Override
        public Class<? extends Service> service() {
            return TestService.class;
        }

        @Override
        public Class<? extends Interface> interfaze() {
            return TestInterface.class;
        }

        @Override
        public String purpose() {
            return "TestTask";
        }
    };
    private static final Suite PRIVATE_SUITE = new Suite() {

        @Override
        public Class<? extends Annotation> annotationType() {
            return Suite.class;
        }

        @Override
        public Class<? extends Component> component() {
            return PrivateTestComponent.class;
        }

        @Override
        public Class<? extends Service> service() {
            return PrivateTestService.class;
        }

        @Override
        public Class<? extends Interface> interfaze() {
            return PrivateTestInterface.class;
        }

        @Override
        public String purpose() {
            return "TestTask";
        }
    };

    @Test
    @DisplayName("GIVEN InputStream == null WHEN readManifest THEN return empty Manifest")
    void unitTest_20181029013041() throws Exception {
        Class<JUtils> JUtilsClass = JUtils.class;
        Method readManifest = JUtilsClass.getDeclaredMethod("readManifest", InputStream.class);
        readManifest.setAccessible(true);
        InputStream inputStream = null;
        Manifest manifest = (Manifest) readManifest.invoke(JUtilsClass, inputStream);
        assertThat(manifest.getMainAttributes().entrySet(), is(empty()));
    }

    @Test
    @DisplayName("GIVEN InputStream WHEN readManifest THEN return Manifest")
    void unitTest_20181029013955() throws Exception {
        Class<JUtils> JUtilsClass = JUtils.class;
        Method readManifest = JUtilsClass.getDeclaredMethod("readManifest", InputStream.class);
        readManifest.setAccessible(true);
        InputStream inputStream = IOHelper.getResourceAsStream("./META-INF/MANIFEST.MF");
        if (inputStream == null) {
            inputStream = IOHelper.getResourceAsStream("META-INF/MANIFEST.MF");
        }
        Manifest manifest = (Manifest) readManifest.invoke(JUtilsClass, inputStream);
        assertThat(manifest.getMainAttributes().entrySet(), is(not(empty())));
    }

    @Test
    @DisplayName("GIVEN InputStream IOException WHEN readManifest THEN return empty Manifest")
    void unitTest_20181029014220() throws Exception {
        Class<JUtils> JUtilsClass = JUtils.class;
        Method readManifest = JUtilsClass.getDeclaredMethod("readManifest", InputStream.class);
        readManifest.setAccessible(true);
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("unitTest_20181029014220");
            }
        };
        Manifest manifest = (Manifest) readManifest.invoke(JUtilsClass, inputStream);
        assertThat(manifest.getMainAttributes().entrySet(), is(empty()));
    }

    @Test
    @DisplayName("Check JUtils constructor")
    void unitTest_20180915192539() throws NoSuchMethodException {
        checkUtilityClassConstructor(JUtils.class);
    }

    public interface ParentInterface {
    }

    public static class Parent implements ParentInterface {
    }

    public static class Child extends Parent {
    }

    @Suite(component = TestComponent.class, service = TestService.class, interfaze = TestInterface.class, purpose = "unit_test")
    private static class PrivateTestClass {
    }

    @Suite(component = TestComponent.class, service = TestService.class, interfaze = TestInterface.class, purpose = "unit_test")
    public abstract static class AbstractTestClass {
    }

    private static class PrivateTestInterface extends Interface {

        @Override
        public String getDescription() {
            return "PrivateTestInterface";
        }
    }

    private static class PrivateTestService extends Service {

        @Override
        public String getDescription() {
            return "PrivateTestService";
        }
    }

    private static class PrivateTestComponent extends Component {

        @Override
        public String getDescription() {
            return "PrivateTestComponent";
        }
    }

}
