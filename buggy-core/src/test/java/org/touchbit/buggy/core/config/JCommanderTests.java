package org.touchbit.buggy.core.config;

import com.beust.jcommander.ParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.BaseUnitTest;
import org.touchbit.buggy.core.config.jcommander.*;
import org.touchbit.buggy.core.exceptions.BuggyConfigurationException;
import org.touchbit.buggy.core.indirect.*;
import org.touchbit.buggy.core.process.Component;
import org.touchbit.buggy.core.process.Interface;
import org.touchbit.buggy.core.process.Service;
import org.touchbit.buggy.core.utils.IOHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.touchbit.buggy.core.config.Parameters.*;

/**
 * Created by Oleg Shaburov on 16.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("JCommander util classes tests")
class JCommanderTests extends BaseUnitTest {

    @Test
    @DisplayName("Check ArraySplitter.split(\"\")")
    void unitTest_20180916231916() {
        ArraySplitter splitter = new ArraySplitter();
        List<String> result = splitter.split("");
        assertThat(result, is(new ArrayList()));
    }

    @Test
    @DisplayName("Check ArraySplitter.split(\"[]\")")
    void unitTest_20180916232212() {
        ArraySplitter splitter = new ArraySplitter();
        List<String> result = splitter.split("[]");
        assertThat(result, is(new ArrayList()));
    }

    @Test
    @DisplayName("Check ArraySplitter.split(\" [  v1 , v2 ] \")")
    void unitTest_20180916232243() {
        ArraySplitter splitter = new ArraySplitter();
        List<String> result = splitter.split(" [  v1 , v2 ,, ] ");
        assertThat(result, is(new ArrayList<String>() {{ add("V1"); add("V2"); }}));
    }

    @Test
    @DisplayName("Check GoalConverter.convert(\"TestComponent\")")
    void unitTest_20180916233351() {
        GoalConverter<Component> converter = new GoalConverter<>(Component.class);
        Component component = converter.convert("TestComponent");
        assertThat(component, is(notNullValue()));
    }

    @Test
    @DisplayName("Check GoalConverter.convert(\"FakeComponent\")")
    void unitTest_20180916233835() {
        GoalConverter<Component> converter = new GoalConverter<>(Component.class);
        BuggyConfigurationException e = execute(() ->
                converter.convert("FakeComponent"), BuggyConfigurationException.class);
        assertThat(e.getMessage(), is("No Component found with name FakeComponent"));
    }

    @Test
    @DisplayName("Check GoalConverter.convert(\"TestService\")")
    void unitTest_20180916234404() {
        GoalConverter<Service> converter = new GoalConverter<>(Service.class);
        Service service = converter.convert("TestService");
        assertThat(service, is(notNullValue()));
    }

    @Test
    @DisplayName("Check GoalConverter.convert(\"TestService\")")
    void unitTest_20180916234519() {
        GoalConverter<Service> converter = new GoalConverter<>(Service.class);
        BuggyConfigurationException e = execute(() ->
                converter.convert("FakeService"), BuggyConfigurationException.class);
        assertThat(e.getMessage(), is("No Service found with name FakeService"));
    }

    @Test
    @DisplayName("Check GoalConverter.convert(\"TestService\")")
    void unitTest_20180916234623() {
        GoalConverter<Interface> converter = new GoalConverter<>(Interface.class);
        Interface testInterface = converter.convert("TestInterface");
        assertThat(testInterface, is(notNullValue()));
    }
    
    @Test
    @DisplayName("Check GoalConverter.convert(\"FakeInterface\")")
    void unitTest_20180916234700() {
        GoalConverter<Interface> converter = new GoalConverter<>(Interface.class);
        BuggyConfigurationException e = execute(() ->
                converter.convert("FakeInterface"), BuggyConfigurationException.class);
        assertThat(e.getMessage(), is("No Interface found with name FakeInterface"));
    }

    @Test
    @DisplayName("Check GoalConverter.convert(\"PrivateTestInterfaceJC\")")
    void unitTest_20180916234947() {
        GoalConverter<Interface> converter = new GoalConverter<>(Interface.class);
        BuggyConfigurationException e = execute(() ->
                converter.convert("PrivateTestInterfaceJC"), BuggyConfigurationException.class);
        assertThat(e.getMessage(),
                is("Can not create a new instance of " + PrivateTestInterfaceJC.class +
                        " inheriting from class Interface"));
    }

    @Test
    @DisplayName("Check GoalConverter.convert(\"PrivateTestServiceJC\")")
    void unitTest_20180916235242() {
        GoalConverter<Service> converter = new GoalConverter<>(Service.class);
        BuggyConfigurationException e = execute(() ->
                converter.convert("PrivateTestServiceJC"), BuggyConfigurationException.class);
        assertThat(e.getMessage(),
                is("Can not create a new instance of " + PrivateTestServiceJC.class +
                        " inheriting from class Service"));
    }

    @Test
    @DisplayName("Check Check GoalConverter.convert(\"PrivateTestComponentJC\")")
    void unitTest_20180916235302() {
        GoalConverter<Component> converter = new GoalConverter<>(Component.class);
        BuggyConfigurationException e = execute(() ->
                converter.convert("PrivateTestComponentJC"), BuggyConfigurationException.class);
        assertThat(e.getMessage(),
                is("Can not create a new instance of " + PrivateTestComponentJC.class +
                        " inheriting from class Component"));
    }

    @Test
    @DisplayName("Check ComponentConverter.convert(\"TestComponent\")")
    void unitTest_20180917001139() {
        ComponentConverter converter = new ComponentConverter();
        List<Component> components = converter.convert("TestComponent");
        assertThat(components.size(), is(1));
        assertThat(components.get(0), instanceOf(TestComponent.class));
    }

    @Test
    @DisplayName("Check InterfaceConverter.convert(\"TestInterface\")")
    void unitTest_20180917001424() {
        InterfaceConverter converter = new InterfaceConverter();
        List<Interface> list = converter.convert("TestInterface");
        assertThat(list.size(), is(1));
        assertThat(list.get(0), instanceOf(TestInterface.class));
    }

    @Test
    @DisplayName("Check ServiceConverter.convert(\"TestService\")")
    void unitTest_20180917001526() {
        ServiceConverter converter = new ServiceConverter();
        List<Service> list = converter.convert("TestService");
        assertThat(list.size(), is(1));
        assertThat(list.get(0), instanceOf(TestService.class));
    }

    @Test
    @DisplayName("Check ValueValidator.validate(QUESTION_MARK & HELP)")
    void unitTest_20180917001718() {
        ValueValidator validator = new ValueValidator();
        validator.validate(QUESTION_MARK, "true");
        assertExitCode(0);
        validator.validate(HELP, "true");
        assertExitCode(0);
        validator.validate(QUESTION_MARK, "false");
        assertExitCode(null);
        validator.validate(HELP, "false");
        assertExitCode(null);
    }

    @Test
    @DisplayName("Check ValueValidator.validate(VERSION & V)")
    void unitTest_20180917003731() throws IOException {
        File testManifest = new File(TEST_CLASSES + "/META-INF", "MANIFEST.MF");
        File srcManifest = new File(CLASSES + "/META-INF", "MANIFEST.MF");
        IOHelper.copyFile(testManifest, srcManifest);
        ValueValidator validator = new ValueValidator();
        validator.validate(VERSION, "true");
        assertExitCode(0);
        validator.validate(V, "true");
        assertExitCode(0);
        validator.validate(VERSION, "false");
        assertExitCode(null);
        validator.validate(V, "false");
        assertExitCode(null);
    }

    @Test
    @DisplayName("Check ParameterValidator.validate(SERVICES & S) with value")
    void unitTest_20180917003944() {
        ParameterValidator validator = new ParameterValidator();
        validator.validate(SERVICES, "false");
        validator.validate(S, "false");
    }

    @Test
    @DisplayName("Check ParameterValidator.validate(SERVICES & S) without value")
    void unitTest_20180917004113() {
        ParameterValidator validator = new ParameterValidator();
        ParameterException e = execute(() -> validator.validate(SERVICES, null), ParameterException.class);
        assertThat(e.getMessage(), is("Parameter " + SERVICES + " can not be empty"));
        e = execute(() -> validator.validate(S, ""), ParameterException.class);
        assertThat(e.getMessage(), is("Parameter " + S + " can not be empty"));
    }

    @Test
    @DisplayName("Check ParameterValidator.validate(INTERFACE & I) with value")
    void unitTest_20180917005308() {
        ParameterValidator validator = new ParameterValidator();
        validator.validate(INTERFACE, "false");
        validator.validate(I, "false");
    }

    @Test
    @DisplayName("Check ParameterValidator.validate(INTERFACE & I) without value")
    void unitTest_20180917005351() {
        ParameterValidator validator = new ParameterValidator();
        ParameterException e = execute(() -> validator.validate(INTERFACE, null), ParameterException.class);
        assertThat(e.getMessage(), is("Parameter " + INTERFACE + " can not be empty"));
        e = execute(() -> validator.validate(I, ""), ParameterException.class);
        assertThat(e.getMessage(), is("Parameter " + I + " can not be empty"));
    }

    @Test
    @DisplayName("Check ParameterValidator.validate(TYPE & T) with value")
    void unitTest_20180917005429() {
        ParameterValidator validator = new ParameterValidator();
        validator.validate(TYPE, "false");
        validator.validate(T, "false");
    }

    @Test
    @DisplayName("Check Check ParameterValidator.validate(TYPE & T) without value")
    void unitTest_20180917005500() {
        ParameterValidator validator = new ParameterValidator();
        ParameterException e = execute(() -> validator.validate(TYPE, null), ParameterException.class);
        assertThat(e.getMessage(), is("Parameter " + TYPE + " can not be empty"));
        e = execute(() -> validator.validate(T, ""), ParameterException.class);
        assertThat(e.getMessage(), is("Parameter " + T + " can not be empty"));
    }

    @Test
    @DisplayName("Check Check ParameterValidator.validate(FAKE) without value")
    void unitTest_20180917005537() {
        ParameterValidator validator = new ParameterValidator();
        validator.validate("--fake", "");
        validator.validate("--fake", null);
    }

    private static class PrivateTestInterfaceJC extends Interface {

        @Override
        public String getDescription() {
            return "PrivateTestInterfaceJC";
        }
    }

    private static class PrivateTestServiceJC extends Service {

        @Override
        public String getDescription() {
            return "PrivateTestServiceJC";
        }
    }

    private static class PrivateTestComponentJC extends Component {

        @Override
        public List<Service> getServices() {
            return new ArrayList<Service>() {{add(new TestService());}};
        }

        @Override
        public String getDescription() {
            return "PrivateTestComponentJC";
        }
    }

}
