package com.group.practic;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;


public class PropertyLoader {

    public final boolean initialized;

    Properties prop = new Properties();


    public PropertyLoader(String file, boolean string) {
        initialized = string ? loadPropertiesFromString(prop, file) : loadProperties(prop, file);
    }


    public PropertyLoader(String file) {
        initialized = loadProperties(prop, file);
    }


    public PropertyLoader(ClassLoader classLoader, String file) {
        initialized = loadProperties(classLoader, file);
    }


    private boolean loadPropertiesFromString(Properties prop, String file) {
        try {
            prop.load(new InputStreamReader(new ByteArrayInputStream(file.getBytes()),
                    StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    protected boolean loadProperties(ClassLoader classLoader, String file) {
        if (loadProperties(prop, file)) {
            return true;
        } else {
            try {
                prop.load(new InputStreamReader(classLoader.getResourceAsStream(file),
                        StandardCharsets.UTF_8));
                return true;
            } catch (IOException e1) {
                return false;
            }
        }
    }


    public static boolean loadProperties(Properties prop, String file) {
        try (FileReader fr = new FileReader(
                Path.of(CoursesInitializator.COURSE_PROPERTY_FOLDER, file).toString(),
                StandardCharsets.UTF_8)) {
            prop.load(fr);
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public String getProperty(String key, String defaultValue) {
        return initialized ? prop.getProperty(key, defaultValue) : null;
    }


    public String getProperty(String key) {
        return initialized ? prop.getProperty(key) : null;
    }


    public Set<Object> getKeySet() {
        return initialized ? prop.keySet() : null;
    }


    public Set<Entry<Object, Object>> getEntrySet() {
        return initialized ? prop.entrySet() : null;
    }


    public static boolean isComprehendedString(String string) {
        return string != null && !string.isBlank();
    }

}
