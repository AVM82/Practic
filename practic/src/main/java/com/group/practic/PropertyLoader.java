package com.group.practic;

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

	PropertyLoader(String file) {
		initialized = loadProperties(prop, file);
	}

	PropertyLoader(ClassLoader classLoader, String file) {
		initialized = loadProperties(classLoader, file);
	}

	protected boolean loadProperties(ClassLoader classLoader, String file) {
		if (loadProperties(prop, file))
			return true;
		else
			try {
				prop.load(new InputStreamReader(classLoader.getResourceAsStream(file), StandardCharsets.UTF_8));
//				logger.debug("Internal property-file file is loaded");
				return true;
			} catch (IOException e1) {
//				logger.error("Property-file is not loaded.");
				return false;
			}
	}

	public static boolean loadProperties(Properties prop, String file) {
		try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8)) {
			prop.load(fr);
//			logger.debug("External property-file file is loaded");
			return true;
		} catch (IOException e) {
//			logger.debug("No such file");
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
