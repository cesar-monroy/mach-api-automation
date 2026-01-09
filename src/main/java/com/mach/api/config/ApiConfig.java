package com.mach.api.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration manager for API testing framework.
 * Reads configuration from system properties and environment variables.
 * 
 * Priority: System Properties > Environment Variables > Defaults
 */
public class ApiConfig {

    private static ApiConfig instance;
    private final Map<String, String> config;

    private ApiConfig() {
        this.config = loadConfig();
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
     * Load configuration with priority: System Properties > Environment Variables > Defaults
     */
    private Map<String, String> loadConfig() {
        Map<String, String> config = new HashMap<>();

        // Load from environment variables
        String baseUri = System.getenv("API_BASE_URI");
        if (baseUri != null) {
            config.put("api.base.uri", baseUri);
        }

        String basePath = System.getenv("API_BASE_PATH");
        if (basePath != null) {
            config.put("api.base.path", basePath);
        }

        String bearerToken = System.getenv("API_BEARER_TOKEN");
        if (bearerToken != null) {
            config.put("api.bearer.token", bearerToken);
        }

        String username = System.getenv("API_USERNAME");
        if (username != null) {
            config.put("api.username", username);
        }

        String password = System.getenv("API_PASSWORD");
        if (password != null) {
            config.put("api.password", password);
        }

        // Override with system properties (highest priority)
        System.getProperties().forEach((key, value) -> {
            if (key.toString().startsWith("api.")) {
                config.put(key.toString(), value.toString());
            }
        });

        return config;
    }

    /**
     * Get property value
     */
    public String getProperty(String key, String defaultValue) {
        return config.getOrDefault(key, defaultValue);
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

