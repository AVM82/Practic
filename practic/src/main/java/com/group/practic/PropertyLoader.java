package com.group.practic;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;


public class PropertyLoader {

    public final boolean initialized;

    Properties prop = new Properties();


    public PropertyLoader(String string) {
        initialized = loadPropertiesFromString(prop, string);
    }


    public PropertyLoader(File file) {
        initialized = loadProperties(prop, file);
    }


    public PropertyLoader(ClassLoader classLoader, File file) {
        initialized = loadProperties(classLoader, file);
    }


    private boolean loadPropertiesFromString(Properties prop, String string) {
        try {
            prop.load(new InputStreamReader(new ByteArrayInputStream(string.getBytes()),
                    StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    protected boolean loadProperties(ClassLoader classLoader, File file) {
        if (loadProperties(prop, file)) {
            return true;
        } else {
            try {
                prop.load(new InputStreamReader(classLoader.getResourceAsStream(file.getPath()),
                        StandardCharsets.UTF_8));
                return true;
            } catch (IOException e1) {
                return false;
            }
        }
    }


    protected boolean loadProperties(Properties prop, File file) {
        try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8)) {
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
