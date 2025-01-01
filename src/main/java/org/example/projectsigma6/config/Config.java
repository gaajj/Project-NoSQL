package org.example.projectsigma6.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static volatile Properties properties;

    private Config() {}

    private static Properties loadProperties() {
        if (properties == null) {
            synchronized (Config.class) {
                if (properties == null) {
                    properties = new Properties();
                    try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
                        if (input ==null) {
                            throw new IOException("Config file not found.");
                        }
                        properties.load(input);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to load config properties.", e);
                    }
                }
            }
        }
        return properties;
    }

    public static String getMongoUri() {
        String mongoUri = loadProperties().getProperty("mongo.uri");
        if (mongoUri == null || mongoUri.isEmpty()) {
            throw new IllegalStateException("Mongo URI not found in config.properties.");
        }
        return mongoUri;
    }

}
