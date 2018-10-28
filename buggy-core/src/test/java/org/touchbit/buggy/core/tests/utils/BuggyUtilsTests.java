package org.touchbit.buggy.core.tests.utils;

import org.atteo.classindex.IndexSubclasses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.testng.xml.Parameters;
import org.touchbit.buggy.core.config.*;
import org.touchbit.buggy.core.tests.BaseUnitTest;
import org.touchbit.buggy.core.Buggy;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.model.Suite;
import org.touchbit.buggy.core.process.Component;
import org.touchbit.buggy.core.process.Interface;
import org.touchbit.buggy.core.process.Service;
import org.touchbit.buggy.core.testng.TestSuite;
import org.touchbit.buggy.core.utils.BuggyUtils;
import org.touchbit.buggy.core.utils.IOHelper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by Oleg Shaburov on 15.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("BuggyUtils Tests")
class BuggyUtilsTests extends BaseUnitTest {

    @Test
    @DisplayName("Check getComponent(Suite suite)")
    void unitTest_20180915175811() {
        Component component = BuggyUtils.getComponent(SUITE_1);
        assertThat(component, instanceOf(TestComponent.class));
    }

    @Test
    @DisplayName("Check getComponent(Suite suite) Configuration Exception")
    void unitTest_20180915181738() {
        Executable executable = () -> BuggyUtils.getComponent(PRIVATE_SUITE);
        assertThrows(BuggyConfigurationException.class, executable,
                "Can not create a new instance of the Component class " +
                        PRIVATE_SUITE.component());
    }

    @Test
    @DisplayName("Check getInterface(Suite suite)")
    void unitTest_20180915181351() {
        Interface anInterface = BuggyUtils.getInterface(SUITE_1);
        assertThat(anInterface, instanceOf(TestInterface.class));
    }

    @Test
    @DisplayName("Check getInterface(Suite suite) Configuration Exception")
    void unitTest_20180915184751() {
        Executable executable = () -> BuggyUtils.getInterface(PRIVATE_SUITE);
        assertThrows(BuggyConfigurationException.class, executable,
                "Can not create a new instance of the Interface class " +
                        PRIVATE_SUITE.interfaze());
    }

    @Test
    @DisplayName("Check getService(Suite suite)")
    void unitTest_20180915181442() {
        Service service = BuggyUtils.getService(SUITE_1);
        assertThat(service, instanceOf(TestService.class));
    }

    @Test
    @DisplayName("Check getService(Suite suite) Configuration Exception")
    void unitTest_20180915184905() {
        Executable executable = () -> BuggyUtils.getService(PRIVATE_SUITE);
        assertThrows(BuggyConfigurationException.class, executable,
                "Can not create a new instance of the Service class " +
                        PRIVATE_SUITE.service());
    }

    @Test
    @DisplayName("Check equalsSuites(null, SUITE_1)")
    void unitTest_20180915185244() {
        assertThat(BuggyUtils.equalsSuites(null, SUITE_1), is(false));
    }

    @Test
    @DisplayName("Check equalsSuites(SUITE_1, null)")
    void unitTest_20180915185356() {
        assertThat(BuggyUtils.equalsSuites(SUITE_1, null), is(false));
    }

    @Test
    @DisplayName("Check equalsSuites(null, null)")
    void unitTest_20180915185504() {
        assertThat(BuggyUtils.equalsSuites(null, null), is(false));
    }

    @Test
    @DisplayName("Check equalsSuites(SUITE_1, SUITE_1)")
    void unitTest_20180915185543() {
        assertThat(BuggyUtils.equalsSuites(SUITE_1, SUITE_1), is(true));
    }

    @Test
    @DisplayName("Check equalsSuites(SUITE_1, SUITE_2)")
    void unitTest_20180915185733() {
        assertThat(BuggyUtils.equalsSuites(SUITE_1, SUITE_2), is(true));
    }

    @Test
    @DisplayName("Check getManifest()")
    void unitTest_20180915185908() {
        assertThat(BuggyUtils.getManifest(), is(notNullValue()));
    }

    @Test
    @DisplayName("GIVEN InputStream == null WHEN readManifest THEN return empty Manifest")
    void unitTest_20181029013041() throws Exception {
        Class<BuggyUtils> buggyUtilsClass = BuggyUtils.class;
        Method readManifest = buggyUtilsClass.getDeclaredMethod("readManifest", InputStream.class);
        readManifest.setAccessible(true);
        InputStream inputStream = null;
        Manifest manifest = (Manifest) readManifest.invoke(buggyUtilsClass, inputStream);
        assertThat(manifest.getMainAttributes().entrySet(), is(empty()));
    }

    @Test
    @DisplayName("GIVEN InputStream WHEN readManifest THEN return Manifest")
    void unitTest_20181029013955() throws Exception {
        Class<BuggyUtils> buggyUtilsClass = BuggyUtils.class;
        Method readManifest = buggyUtilsClass.getDeclaredMethod("readManifest", InputStream.class);
        readManifest.setAccessible(true);
        InputStream inputStream = IOHelper.getResourceAsStream("./META-INF/MANIFEST.MF");
        if (inputStream == null) {
            inputStream = IOHelper.getResourceAsStream("META-INF/MANIFEST.MF");
        }
        Manifest manifest = (Manifest) readManifest.invoke(buggyUtilsClass, inputStream);
        assertThat(manifest.getMainAttributes().entrySet(), is(not(empty())));
    }

    @Test
    @DisplayName("GIVEN InputStream IOException WHEN readManifest THEN return empty Manifest")
    void unitTest_20181029014220() throws Exception {
        Class<BuggyUtils> buggyUtilsClass = BuggyUtils.class;
        Method readManifest = buggyUtilsClass.getDeclaredMethod("readManifest", InputStream.class);
        readManifest.setAccessible(true);
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("unitTest_20181029014220");
            }
        };
        Manifest manifest = (Manifest) readManifest.invoke(buggyUtilsClass, inputStream);
        assertThat(manifest.getMainAttributes().entrySet(), is(empty()));
    }

    @Test
    @DisplayName("Check getManifestAttributes()")
    void unitTest_20180915192200() {
        Attributes attributes = BuggyUtils.getManifestAttributes();
        assertThat(attributes, is(notNullValue()));
        assertThat(attributes.isEmpty(), is(false));
    }

    @Test
    @DisplayName("Check isListBaseSuiteContainsClass(null, null)")
    void unitTest_20180915204319() {
        assertThat(BuggyUtils.isListBaseSuiteContainsClass(null, null), is(false));
    }

    @Test
    @DisplayName("Check isListBaseSuiteContainsClass(new ArrayList<>(), Object.class)")
    void unitTest_20180915204615() {
        assertThat(BuggyUtils.isListBaseSuiteContainsClass(new ArrayList<>(), Object.class), is(false));
    }

    @Test
    @DisplayName("Check isListBaseSuiteContainsClass(notEmptyTestSuiteList, null)")
    void unitTest_20180915204623() {
        List<TestSuite> notEmptyTestSuiteList = new ArrayList<>();
        Buggy.setPrimaryConfigClass(UnitTestPrimaryConfig.class);
        notEmptyTestSuiteList.add(new TestSuite(SUITE_1));
        assertThat(BuggyUtils.isListBaseSuiteContainsClass(notEmptyTestSuiteList, null), is(false));
    }

    @Test
    @DisplayName("Check isListBaseSuiteContainsClass(notEmptyTestSuiteList, Object.class)")
    void unitTest_20180915204630() {
        List<TestSuite> notEmptyTestSuiteList = new ArrayList<>();
        Buggy.setPrimaryConfigClass(UnitTestPrimaryConfig.class);
        notEmptyTestSuiteList.add(new TestSuite(SUITE_1));
        assertThat(BuggyUtils.isListBaseSuiteContainsClass(notEmptyTestSuiteList, Object.class), is(false));
    }

    @Test
    @DisplayName("Check isListBaseSuiteContainsClass(suiteNotContainsTestPackage, BuggyUtilsTests.class)")
    void unitTest_20180915211848() {
        List<TestSuite> suiteNotContainsTestPackage = new ArrayList<>();
        Buggy.setPrimaryConfigClass(UnitTestPrimaryConfig.class);
        TestSuite suite = new TestSuite(SUITE_1);
        suiteNotContainsTestPackage.add(suite);
        assertThat(BuggyUtils
                .isListBaseSuiteContainsClass(suiteNotContainsTestPackage, BuggyUtilsTests.class), is(false));
    }

    @Test
    @DisplayName("Check isListBaseSuiteContainsClass(suiteNotContainsTestClass, BuggyUtilsTests.class)")
    void unitTest_20180915212041() {
        List<TestSuite> suiteNotContainsTestClass = new ArrayList<>();
        Buggy.setPrimaryConfigClass(UnitTestPrimaryConfig.class);
        TestSuite suite = new TestSuite(SUITE_1);
        suite.addTestPackage("test");
        suiteNotContainsTestClass.add(suite);
        assertThat(BuggyUtils
                .isListBaseSuiteContainsClass(suiteNotContainsTestClass, BuggyUtilsTests.class), is(false));
    }

    @Test
    @DisplayName("Check isListBaseSuiteContainsClass(suiteContainsTestClass, Object.class)")
    void unitTest_20180915212259() {
        List<TestSuite> suiteContainsTestClass = new ArrayList<>();
        Buggy.setPrimaryConfigClass(UnitTestPrimaryConfig.class);
        TestSuite suite = new TestSuite(SUITE_1);
        suite.addTestPackage("test", BuggyUtilsTests.class);
        suiteContainsTestClass.add(suite);
        assertThat(BuggyUtils
                .isListBaseSuiteContainsClass(suiteContainsTestClass, Object.class), is(false));
    }

    @Test
    @DisplayName("Check isListBaseSuiteContainsClass(notEmptyTestSuiteList, BuggyUtilsTests.class)")
    void unitTest_20180915204950() {
        List<TestSuite> suiteContainsTestClass = new ArrayList<>();
        Buggy.setPrimaryConfigClass(UnitTestPrimaryConfig.class);
        TestSuite suite = new TestSuite(SUITE_1);
        suite.addTestPackage("test", BuggyUtilsTests.class);
        suiteContainsTestClass.add(suite);
        assertThat(BuggyUtils
                .isListBaseSuiteContainsClass(suiteContainsTestClass, BuggyUtilsTests.class), is(true));
    }

    @Test
    @DisplayName("Check BuggyUtils constructor")
    void unitTest_20180915192539() throws NoSuchMethodException {
        checkUtilityClassConstructor(BuggyUtils.class);
    }

    @Test
    @DisplayName("GIVEN findAnnotatedInstantiatedClasses(Suite) WHEN public class THEN hasItem")
    void unitTest_20181021171954() {
        List<Class<?>> suites = BuggyUtils.findAnnotatedInstantiatedClasses(Suite.class);
        assertThat(suites, not(empty()));
        assertThat(suites, hasItem(sameInstance(TestClassWithSuite.class)));
    }

    @Test
    @DisplayName("GIVEN findAnnotatedInstantiatedClasses(Suite) WHEN not public class THEN not(hasItem)")
    void unitTest_20181021172745() {
        List<Class<?>> suites = BuggyUtils.findAnnotatedInstantiatedClasses(Suite.class);
        assertThat(suites, not(empty()));
        assertThat(suites, not(hasItem(sameInstance(PrivateTestClass.class))));
    }

    @Test
    @DisplayName("GIVEN findAnnotatedInstantiatedClasses(Suite) WHEN abstract class THEN not(hasItem)")
    void unitTest_20181021173208() {
        List<Class<?>> suites = BuggyUtils.findAnnotatedInstantiatedClasses(Suite.class);
        assertThat(suites, not(empty()));
        assertThat(suites, not(hasItem(sameInstance(AbstractTestClass.class))));
    }

    @Test
    @DisplayName("GIVEN findAnnotatedInstantiatedClasses(Suite) WHEN no annotated class THEN is(empty())")
    void unitTest_20181021174039() {
        List<Class<?>> suites = BuggyUtils.findAnnotatedInstantiatedClasses(BuggyUtilsTestsInterface.class);
        assertThat(suites, is(empty()));
    }

    @Test
    @DisplayName("GIVEN isAssignableFrom WHEN nested inheritance THEN assignable")
    void unitTest_20181021174629() {
        assertThat(BuggyUtils.isAssignableFrom(Child.class, Parent.class), is(true));
        assertThat(BuggyUtils.isAssignableFrom(Child.class, Child.class), is(true));
        assertThat(BuggyUtils.isAssignableFrom(Parent.class, Child.class), is(false));
        assertThat(BuggyUtils.isAssignableFrom(Child.class, ParentInterface.class), is(false));
    }

    @Test
    @DisplayName("GIVEN isAssignableFrom WHEN null THEN false")
    void unitTest_20181021175344() {
        assertThat(BuggyUtils.isAssignableFrom(null, Parent.class), is(false));
        assertThat(BuggyUtils.isAssignableFrom(Parent.class, null), is(false));
    }

    @Test
    @DisplayName("GIVEN isAssignableFrom WHEN Object THEN false")
    void unitTest_20181021175526() {
        assertThat(BuggyUtils.isAssignableFrom(Object.class, Parent.class), is(false));
    }

    @Test
    @DisplayName("WHEN findComponents() THEN hasItem")
    void unitTest_20181021175921() {
        assertThat(BuggyUtils.findComponents(), hasItem(instanceOf(TestComponent.class)));
    }

    @Test
    @DisplayName("WHEN findServices() THEN hasItem")
    void unitTest_20181021180113() {
        assertThat(BuggyUtils.findServices(), hasItem(instanceOf(TestService.class)));
    }

    @Test
    @DisplayName("WHEN findInterfaces() THEN hasItem")
    void unitTest_20181021180159() {
        assertThat(BuggyUtils.findInterfaces(), hasItem(instanceOf(TestInterface.class)));
    }

    @Test
    @DisplayName("GIVEN Class with exception WHEN getSubclassesNewObjectList THEN BuggyConfigurationException")
    void unitTest_20181021180411() {
        Executable executable = () -> BuggyUtils.getSubclassesNewObjectList(IndexClass.class);
        assertThrows(BuggyConfigurationException.class, executable,
                "Can not create a new instance of the IndexClass " + IndexedClass.class);
    }

    @IndexSubclasses
    public static abstract class IndexClass {}

    public static class IndexedClass extends IndexClass {

        public IndexedClass() {
            throw new RuntimeException("unitTest_20181021180411 exception");
        }
    }

    public interface ParentInterface {}

    public static class Parent implements ParentInterface {}

    public static class Child extends Parent {}

    public static @interface BuggyUtilsTestsInterface {}

    @Suite(component = TestComponent.class, service = TestService.class, interfaze = TestInterface.class, task = "unit_test")
    private static class PrivateTestClass {}

    @Suite(component = TestComponent.class, service = TestService.class, interfaze = TestInterface.class, task = "unit_test")
    public abstract static class AbstractTestClass {}

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
        public String task() {
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
        public String task() {
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
        public String task() {
            return "TestTask";
        }
    };

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
        public List<Service> getServices() {
            return new ArrayList<Service>() {{add(new TestService());}};
        }

        @Override
        public String getDescription() {
            return "PrivateTestComponent";
        }
    }

}
