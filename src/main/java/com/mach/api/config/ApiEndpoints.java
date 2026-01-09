package com.mach.api.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized configuration for API endpoints base URIs.
 * Provides default values that can be overridden by environment variables or system properties.
 * 
 * Priority: System Properties > Environment Variables > Default Constants
 * 
 * Usage:
 * <pre>
 * String accountUri = ApiEndpoints.getBaseUri("account");
 * // Returns: ACCOUNT_API_BASE_URI env var, or system property, or default constant
 * </pre>
 */
public class ApiEndpoints {

    /**
     * Default base URIs for each service.
     * These are fallback values used when environment variables or system properties are not set.
     */
    private static final Map<String, String> DEFAULT_BASE_URIS = new HashMap<>();
    
    static {
        // Account Service
        DEFAULT_BASE_URIS.put("account", "https://account-faker-api-staging-stg.soymach.com");
        
        // Add more services as needed
        // DEFAULT_BASE_URIS.put("payment", "https://payment-api.example.com");
        // DEFAULT_BASE_URIS.put("user", "https://user-api.example.com");
    }

    /**
     * Default base paths for each service.
     */
    private static final Map<String, String> DEFAULT_BASE_PATHS = new HashMap<>();
    
    static {
        DEFAULT_BASE_PATHS.put("account", "");
        // DEFAULT_BASE_PATHS.put("payment", "/api/v1");
        // DEFAULT_BASE_PATHS.put("user", "/api/v2");
    }

    /**
     * Default API keys for each service.
     * These are fallback values used when environment variables or system properties are not set.
     */
    private static final Map<String, String> DEFAULT_API_KEYS = new HashMap<>();
    
    static {
        // Account Service
        DEFAULT_API_KEYS.put("account", "ii92khXeY#q<t38W!8m{");
        
        // Add more services as needed
        // DEFAULT_API_KEYS.put("payment", "payment-api-key-here");
    }

    /**
     * Get base URI for a service.
     * Priority: System Property > Environment Variable > Default Constant
     * 
     * @param serviceName Service name (e.g., "account", "payment")
     * @return Base URI for the service
     */
    public static String getBaseUri(String serviceName) {
        String normalizedService = serviceName.toUpperCase().replaceAll("[^A-Z0-9]", "_");
        
        // Try system property first (highest priority)
        String systemPropKey = "api." + serviceName.toLowerCase() + ".base.uri";
        String systemProp = System.getProperty(systemPropKey);
        if (systemProp != null && !systemProp.isEmpty()) {
            return systemProp;
        }

        // Try environment variable
        String envKey = normalizedService + "_API_BASE_URI";
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }

        // Return default constant
        return DEFAULT_BASE_URIS.getOrDefault(serviceName.toLowerCase(), null);
    }

    /**
     * Get base path for a service.
     * Priority: System Property > Environment Variable > Default Constant
     * 
     * @param serviceName Service name (e.g., "account", "payment")
     * @return Base path for the service (empty string if not set)
     */
    public static String getBasePath(String serviceName) {
        String normalizedService = serviceName.toUpperCase().replaceAll("[^A-Z0-9]", "_");
        
        // Try system property first (highest priority)
        String systemPropKey = "api." + serviceName.toLowerCase() + ".base.path";
        String systemProp = System.getProperty(systemPropKey);
        if (systemProp != null && !systemProp.isEmpty()) {
            return systemProp;
        }

        // Try environment variable
        String envKey = normalizedService + "_API_BASE_PATH";
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }

        // Return default constant
        return DEFAULT_BASE_PATHS.getOrDefault(serviceName.toLowerCase(), "");
    }

    /**
     * Check if a service has a default base URI configured
     * 
     * @param serviceName Service name
     * @return true if default URI exists
     */
    public static boolean hasDefaultBaseUri(String serviceName) {
        return DEFAULT_BASE_URIS.containsKey(serviceName.toLowerCase());
    }

    /**
     * Get API key for a service.
     * Priority: System Property > Environment Variable > Default Constant
     * 
     * @param serviceName Service name (e.g., "account", "payment")
     * @return API key for the service, or null if not configured
     */
    public static String getApiKey(String serviceName) {
        String normalizedService = serviceName.toUpperCase().replaceAll("[^A-Z0-9]", "_");
        
        // Try system property first (highest priority)
        String systemPropKey = "api." + serviceName.toLowerCase() + ".key";
        String systemProp = System.getProperty(systemPropKey);
        if (systemProp != null && !systemProp.isEmpty()) {
            return systemProp;
        }

        // Try environment variable
        String envKey = normalizedService + "_API_KEY";
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }

        // Return default constant
        return DEFAULT_API_KEYS.getOrDefault(serviceName.toLowerCase(), null);
    }

    /**
     * Get all configured service names
     * 
     * @return Set of service names with default URIs
     */
    public static java.util.Set<String> getConfiguredServices() {
        return DEFAULT_BASE_URIS.keySet();
    }
}

