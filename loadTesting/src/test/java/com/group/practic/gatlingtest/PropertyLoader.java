package com.group.practic.gatlingtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyLoader {
    public static final Logger logger = LoggerFactory.getLogger(PropertyLoader.class);
    private static final String PROPERTY = "simulation.properties";

    public Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream is = GatlingTest.class.getClassLoader().getResourceAsStream(PROPERTY)) {
            Reader reader = new InputStreamReader(
                    Objects.requireNonNull(is), StandardCharsets.UTF_8);
            properties.load(reader);
        } catch (IOException e) {
            logger.error("cannot load property ", e);
        }
        return properties;
    }
}
