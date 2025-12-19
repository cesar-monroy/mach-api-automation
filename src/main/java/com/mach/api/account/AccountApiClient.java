package com.mach.api.account;

import com.mach.api.account.model.AccountAction;
import com.mach.api.account.model.AccountRequest;
import com.mach.api.client.RestClient;
import com.mach.api.config.ServiceConfig;
import io.restassured.http.Method;
import io.restassured.response.ValidatableResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Client for Account API - User creation service.
 * Provides methods to create new user accounts.
 * 
 * Configuration is loaded from ServiceConfig using the "account" service name.
 * Supports configuration via environment variables (ACCOUNT_API_*), system properties, or properties file.
 */
public class AccountApiClient {

    private static final Logger LOG = LoggerFactory.getLogger(AccountApiClient.class);
    private static final String SERVICE_NAME = "account";
    
    private final RestClient restClient;
    private final ServiceConfig config;

    /**
     * Default constructor - loads configuration from ServiceConfig
     * Configuration priority: System Properties > Environment Variables > Properties File
     * 
     * @throws IllegalStateException if required configuration (base URI or bearer token) is missing
     */
    public AccountApiClient() {
        this.config = ServiceConfig.forService(SERVICE_NAME);
        this.config.validate();
        
        if (!this.config.hasBearerToken()) {
            throw new IllegalStateException(
                String.format("Bearer token is required for %s service. " +
                    "Set %s_API_BEARER_TOKEN environment variable or api.%s.bearer.token system property.",
                    SERVICE_NAME, SERVICE_NAME.toUpperCase(), SERVICE_NAME.toLowerCase()));
        }
        
        this.restClient = new RestClient(
            this.config.getBaseUri(),
            this.config.getBasePath(),
            this.config.getBearerToken()
        );
        
        LOG.info("AccountApiClient initialized with base URI: {}", this.config.getBaseUri());
    }

    /**
     * Constructor with explicit Bearer token (overrides configuration)
     * 
     * @param bearerToken Bearer token to use for authentication
     */
    public AccountApiClient(String bearerToken) {
        this.config = ServiceConfig.forService(SERVICE_NAME);
        this.config.validate();
        
        this.restClient = new RestClient(
            this.config.getBaseUri(),
            this.config.getBasePath(),
            bearerToken
        );
        
        LOG.info("AccountApiClient initialized with base URI: {} and explicit bearer token", this.config.getBaseUri());
    }

    /**
     * Create a new user account
     * 
     * @param request Account request with actions
     * @return ValidatableResponse for validation
     */
    public ValidatableResponse createAccount(AccountRequest request) {
        Map<String, Object> headers = buildHeaders();
        
        LOG.info("Creating new user account");
        ValidatableResponse response = restClient.runWebServiceWithBody(
                Method.POST, headers, "/account", request);
        
        LOG.debug("Account creation response status: {}", response.extract().statusCode());
        return response;
    }

    /**
     * Create a new user account with default createAccountAction
     * 
     * @return ValidatableResponse for validation
     */
    public ValidatableResponse createAccount() {
        AccountRequest request = new AccountRequest();
        AccountAction action = new AccountAction();
        action.setName("createAccountAction");
        action.setArgs(new HashMap<>());
        request.setActions(new AccountAction[]{action});
        
        return createAccount(request);
    }

    /**
     * Create a new user account with custom action arguments
     * 
     * @param actionArgs Custom arguments for the createAccountAction
     * @return ValidatableResponse for validation
     */
    public ValidatableResponse createAccount(Map<String, Object> actionArgs) {
        AccountRequest request = new AccountRequest();
        AccountAction action = new AccountAction();
        action.setName("createAccountAction");
        action.setArgs(actionArgs != null ? actionArgs : new HashMap<>());
        request.setActions(new AccountAction[]{action});
        
        return createAccount(request);
    }

    /**
     * Build headers for Account API requests
     */
    private Map<String, Object> buildHeaders() {
        Map<String, Object> headers = new HashMap<>();
        
        // Add API Key if configured
        if (config.hasApiKey()) {
            headers.put("X-Api-Key", config.getApiKey());
        }
        
        headers.put("Content-Type", "application/json");
        return headers;
    }
    
    /**
     * Get the service configuration (for advanced usage)
     * 
     * @return ServiceConfig instance
     */
    public ServiceConfig getConfig() {
        return config;
    }

}

