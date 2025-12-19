package com.mach.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Configuration manager for API testing framework.
 * Reads configuration from system properties, environment variables, or properties file.
 */
public class ApiConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ApiConfig.class);
    private static ApiConfig instance;
    private final Properties properties;

    private ApiConfig() {
        this.properties = loadProperties();
    }

    /**
     * Get singleton instance
     */
    public static synchronized ApiConfig getInstance() {
        if (instance == null) {
            instance = new ApiConfig();
        }
        return instance;
    }

    /**
     * Load properties with priority: System Properties > Environment Variables > Properties File
     */
    private Properties loadProperties() {
        Properties props = new Properties();

        // Load from properties file
        try {
            java.io.InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream("api.properties");
            if (inputStream != null) {
                props.load(inputStream);
                LOG.debug("Loaded properties from api.properties file");
            }
        } catch (Exception e) {
            LOG.debug("No api.properties file found, using defaults");
        }

        // Override with environment variables
        String baseUri = System.getenv("API_BASE_URI");
        if (baseUri != null) {
            props.setProperty("api.base.uri", baseUri);
        }

        String basePath = System.getenv("API_BASE_PATH");
        if (basePath != null) {
            props.setProperty("api.base.path", basePath);
        }

        String bearerToken = System.getenv("API_BEARER_TOKEN");
        if (bearerToken != null) {
            props.setProperty("api.bearer.token", bearerToken);
        }

        String username = System.getenv("API_USERNAME");
        if (username != null) {
            props.setProperty("api.username", username);
        }

        String password = System.getenv("API_PASSWORD");
        if (password != null) {
            props.setProperty("api.password", password);
        }

        // Override with system properties (highest priority)
        System.getProperties().forEach((key, value) -> {
            if (key.toString().startsWith("api.")) {
                props.setProperty(key.toString(), value.toString());
            }
        });

        return props;
    }

    /**
     * Get property value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get base URI
     */
    public String getBaseUri() {
        return getProperty("api.base.uri", "http://localhost:8080");
    }

    /**
     * Get base path
     */
    public String getBasePath() {
        return getProperty("api.base.path", "/api/v1");
    }

    /**
     * Get bearer token
     */
    public String getBearerToken() {
        return getProperty("api.bearer.token", null);
    }

    /**
     * Get username for basic auth
     */
    public String getUsername() {
        return getProperty("api.username", null);
    }

    /**
     * Get password for basic auth
     */
    public String getPassword() {
        return getProperty("api.password", null);
    }

    /**
     * Check if authentication is configured
     */
    public boolean hasAuth() {
        return getBearerToken() != null || (getUsername() != null && getPassword() != null);
    }
}

