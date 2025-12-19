package com.mach.api.config;

/**
 * Configuration for a specific API service.
 * Supports multiple services with different URIs, paths, and authentication.
 * 
 * Priority: System Properties > Environment Variables > Properties File > Defaults
 * 
 * Example usage:
 * <pre>
 * ServiceConfig accountConfig = ServiceConfig.forService("account");
 * String baseUri = accountConfig.getBaseUri();
 * </pre>
 * 
 * Environment variables pattern: {SERVICE}_API_{PROPERTY}
 * Example: ACCOUNT_API_BASE_URI, ACCOUNT_API_BEARER_TOKEN
 */
public class ServiceConfig {

    private final String serviceName;
    private final String baseUri;
    private final String basePath;
    private final String bearerToken;
    private final String apiKey;
    private final String username;
    private final String password;

    private ServiceConfig(String serviceName, String baseUri, String basePath, 
                        String bearerToken, String apiKey, String username, String password) {
        this.serviceName = serviceName;
        this.baseUri = baseUri;
        this.basePath = basePath;
        this.bearerToken = bearerToken;
        this.apiKey = apiKey;
        this.username = username;
        this.password = password;
    }

    /**
     * Create configuration for a specific service
     * 
     * @param serviceName Service name (e.g., "account", "payment", "user")
     * @return ServiceConfig instance
     */
    public static ServiceConfig forService(String serviceName) {
        String normalizedService = serviceName.toUpperCase().replaceAll("[^A-Z0-9]", "_");
        
        // Priority: System Property > Environment Variable > Properties File > Default
        String baseUri = getConfigValue(normalizedService, "BASE_URI", 
                getProperty("api." + serviceName.toLowerCase() + ".base.uri", null));
        
        String basePath = getConfigValue(normalizedService, "BASE_PATH", 
                getProperty("api." + serviceName.toLowerCase() + ".base.path", ""));
        
        String bearerToken = getConfigValue(normalizedService, "BEARER_TOKEN", 
                getProperty("api." + serviceName.toLowerCase() + ".bearer.token", null));
        
        String apiKey = getConfigValue(normalizedService, "API_KEY", 
                getProperty("api." + serviceName.toLowerCase() + ".key", null));
        
        String username = getConfigValue(normalizedService, "USERNAME", 
                getProperty("api." + serviceName.toLowerCase() + ".username", null));
        
        String password = getConfigValue(normalizedService, "PASSWORD", 
                getProperty("api." + serviceName.toLowerCase() + ".password", null));

        return new ServiceConfig(serviceName, baseUri, basePath, bearerToken, apiKey, username, password);
    }

    /**
     * Get configuration value with priority: System Property > Environment Variable > Default
     */
    private static String getConfigValue(String serviceName, String property, String defaultValue) {
        // Try system property first (highest priority)
        String systemPropKey = "api." + serviceName.toLowerCase() + "." + property.toLowerCase().replace("_", ".");
        String systemProp = System.getProperty(systemPropKey);
        if (systemProp != null && !systemProp.isEmpty()) {
            return systemProp;
        }

        // Try environment variable
        String envKey = serviceName + "_API_" + property;
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }

        // Return default or null
        return defaultValue;
    }

    /**
     * Get property from properties file (lowest priority)
     */
    private static String getProperty(String key, String defaultValue) {
        ApiConfig config = ApiConfig.getInstance();
        return config.getProperty(key, defaultValue);
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public String getBasePath() {
        return basePath != null ? basePath : "";
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Check if Bearer token authentication is configured
     */
    public boolean hasBearerToken() {
        return bearerToken != null && !bearerToken.isEmpty();
    }

    /**
     * Check if API Key is configured
     */
    public boolean hasApiKey() {
        return apiKey != null && !apiKey.isEmpty();
    }

    /**
     * Check if Basic Auth is configured
     */
    public boolean hasBasicAuth() {
        return username != null && password != null && !username.isEmpty() && !password.isEmpty();
    }

    /**
     * Check if any authentication is configured
     */
    public boolean hasAuth() {
        return hasBearerToken() || hasApiKey() || hasBasicAuth();
    }

    /**
     * Validate that required configuration is present
     * 
     * @throws IllegalStateException if base URI is missing
     */
    public void validate() {
        if (baseUri == null || baseUri.isEmpty()) {
            throw new IllegalStateException(
                String.format("Base URI is required for service '%s'. " +
                    "Set %s_API_BASE_URI environment variable or api.%s.base.uri system property.",
                    serviceName, serviceName.toUpperCase(), serviceName.toLowerCase()));
        }
    }
}

