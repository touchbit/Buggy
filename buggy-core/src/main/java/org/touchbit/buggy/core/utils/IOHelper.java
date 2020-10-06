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

package org.touchbit.buggy.core.utils;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.touchbit.buggy.core.exceptions.BuggyException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Utility class work with OC.
 * <p>
 * Created by Oleg Shaburov on 17.05.2018
 * shaburov.o.a@gmail.com
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnstableApiUsage", "WeakerAccess"})
public class IOHelper {

    private IOHelper() {
        throw new IllegalStateException("Utility class. Prohibit instantiation.");
    }

    public static void copyFile(String sourceFile, String destFile) throws IOException {
        File from = new File(sourceFile);
        File to = new File(destFile);
        copyFile(from, to);
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        makeParentDirs(destFile);
        Files.copy(sourceFile, destFile);
    }

    public static void moveFile(File sourceFile, File destFile) throws IOException {
        makeParentDirs(destFile);
        Files.move(sourceFile, destFile);
    }

    public static void writeFile(File file, String body) {
        makeParentDirs(file);
        try {
            Files.write(body.getBytes(StandardCharsets.UTF_8), file);
        } catch (Exception e) {
            throw new BuggyException("Error writing file: " + file, e);
        }
    }

    public static void makeParentDirs(File file) {
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
    }

    /**
     * Reads a text file from resources to a string.
     *
     * @param file - {@link File}
     * @return string with the content of the text file
     */
    public static String readFile(File file) {
        try {
            return Files.asCharSource(file, Charsets.UTF_8).read();
        } catch (Exception e) {
            throw new BuggyException("Cannot read file: " + file, e);
        }
    }

    /**
     * Returns the file {@link InputStream} from the resources.
     *
     * @param path - relative path to file
     * @return {@link InputStream}
     */
    public static InputStream getResourceAsStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    /**
     * Returns the file {@link File} from the resources.
     *
     * @param file - relative path to file
     * @return {@link File}
     */
    @SuppressWarnings("UnstableApiUsage")
    public static File getFileFromResource(final String file) {
        try (InputStream inputStream = getResourceAsStream(file)) {
            if (inputStream == null) {
                throw new BuggyException("The following file can not be found in the project resources: " + file);
            }
            final File tempFile = File.createTempFile(file + ".", null);
            final byte[] targetArray = ByteStreams.toByteArray(inputStream);
            Files.write(targetArray, tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new BuggyException("Error reading file from resources: " + file, e);
        }
    }

    /**
     * Returns the file {@link File} from the resources.
     *
     * @param file - relative path to file
     * @return {@link File}
     */
    public static Properties readPropertiesFileFromResource(final String file) {
        try (InputStream inputStream = getResourceAsStream(file)) {
            if ((inputStream == null)) {
                throw new BuggyException("The following file can not be found in the project resources: " + file);
            }
            final Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new BuggyException("Error reading file [" + file + "] from project resources", e);
        }
    }

}
