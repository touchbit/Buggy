package org.touchbit.buggy.core.tests.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.touchbit.buggy.core.exceptions.BuggyException;
import org.touchbit.buggy.core.tests.BaseUnitTest;
import org.touchbit.buggy.core.utils.IOHelper;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by Oleg Shaburov on 15.09.2018
 * shaburov.o.a@gmail.com
 */
@DisplayName("IOHelper Tests")
class IOHelperTests extends BaseUnitTest {

//    @Test
//    @DisplayName("Check readPropertiesFileFromResource(\"unit_test.properties\")")
//    void unitTest_20180915215434() {
//        IOHelper.writeFile(new File(TEST_CLASSES, "unit_test.properties"),
//                "--single\n--with_value=value");
//        Properties properties = IOHelper.readPropertiesFileFromResource("unit_test.properties");
//        assertThat(properties.getProperty("--single"), is(""));
//        assertThat(properties.getProperty("--with_value"), is("value"));
//    }

    @Test
    @DisplayName("Check readPropertiesFileFromResource(\"not_exist.properties\")")
    void unitTest_20180915215744() {
        Exception exception = null;
        String fileName = "not_exist.properties";
        try {
            IOHelper.readPropertiesFileFromResource(fileName);
        } catch (Exception e) {
            exception = e;
        }
        assertThat("BuggyException", exception, is(notNullValue()));
        assertThat(exception.getClass(), sameInstance(BuggyException.class));
        assertThat(exception.getMessage(),
                is("The following file can not be found in the project resources: " + fileName));
    }

//    @Test
//    @DisplayName("Check getFileFromResource(exists)")
//    void unitTest_20180915220703() {
//        String fileName = "unitTest_20180915220703.txt";
//        IOHelper.writeFile(new File(TEST_CLASSES, fileName), fileName);
//        File file = IOHelper.getFileFromResource(fileName);
//        assertThat(file.exists(), is(true));
//        assertThat(IOHelper.readFile(file), is(fileName));
//    }

    @Test
    @DisplayName("Check getFileFromResource(not exists)")
    void unitTest_20180915233309() {
        String fileName = "unitTest_20180915233309.txt";
        Exception exception = null;
        try {
            IOHelper.getFileFromResource(fileName);
        } catch (Exception e) {
            exception = e;
        }
        assertThat("BuggyException", exception, is(notNullValue()));
        assertThat(exception.getClass(), sameInstance(BuggyException.class));
        assertThat(exception.getMessage(),
                is("The following file can not be found in the project resources: " + fileName));
    }

    @Test
    @DisplayName("Check readFile(notExistFile)")
    void unitTest_20180915235133() {
        Exception exception = null;
        String fileName = "unitTest_20180915235133.txt";
        try {
            File notExistFile = new File(fileName);
            IOHelper.readFile(notExistFile);
        } catch (Exception e) {
            exception = e;
        }
        assertThat("BuggyException", exception, is(notNullValue()));
        assertThat(exception.getClass(), sameInstance(BuggyException.class));
        assertThat(exception.getMessage(),
                is("Cannot read file: " + fileName));
    }

    @Test
    @DisplayName("Check writeFile(notExistFile)")
    void unitTest_20180915235442() {
        Exception exception = null;
        String fileName = "/unitTest_20180915235442.txt";
        try {
            File notExistFile = new File(fileName);
            IOHelper.writeFile(notExistFile, "");
        } catch (Exception e) {
            exception = e;
        }
        assertThat("BuggyException", exception, is(notNullValue()));
        assertThat(exception.getClass(), sameInstance(BuggyException.class));
        assertThat(exception.getMessage(),
                is("Error writing file: " + fileName));
    }
//
//    @Test
//    @DisplayName("Check writeFile & readFile")
//    void unitTest_20180916000548() {
//        String fileName = "unitTest_20180916000548.txt";
//        File existFile = new File(WASTE, fileName);
//        IOHelper.writeFile(existFile, "body");
//        String body = IOHelper.readFile(existFile);
//        assertThat(body, is("body"));
//    }
//
//    @Test
//    @DisplayName("Check copyFile(existFile, target)")
//    void unitTest_20180916000532() throws IOException {
//        String fileName = "unitTest_20180916000532.txt";
//        File existFile = new File(WASTE, fileName);
//        IOHelper.writeFile(existFile, "body");
//        File target = new File(WASTE + "/unitTest_20180916000532/", "copy.txt");
//        IOHelper.copyFile(existFile, target);
//        assertThat("target.exists()", target.exists(), is(true));
//    }
//
//    @Test
//    @DisplayName("Check copyFile(existFile.getAbsoluteLogPath(), target.getAbsoluteLogPath()")
//    void unitTest_20180916001820() throws IOException {
//        String fileName = "unitTest_20180916001820.txt";
//        File existFile = new File(WASTE, fileName);
//        IOHelper.writeFile(existFile, "body");
//        File target = new File(WASTE + "/unitTest_20180916001820/", "copy.txt");
//        IOHelper.copyFile(existFile.getAbsolutePath(), target.getAbsolutePath());
//        assertThat("target.exists()", target.exists(), is(true));
//    }
//
//    @Test
//    @DisplayName("Check makeParentDirs(file)")
//    void unitTest_20180916213553() {
//        File file1 = new File(WASTE);
//        File file2 = new File(WASTE + "/p1/p2/p3", "file.txt");
//        IOHelper.makeParentDirs(file1);
//        IOHelper.makeParentDirs(file2);
//        assertThat("makeParentDirs exists", file1.exists(), is(true));
//        assertThat("makeParentDirs exists", file2.getParentFile().exists(), is(true));
//    }

    @Test
    @DisplayName("Check IOHelper constructor")
    void unitTest_20180915234042() throws NoSuchMethodException {
        checkUtilityClassConstructor(IOHelper.class);
    }
}
