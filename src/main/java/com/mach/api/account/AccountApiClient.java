package com.mach.api.account;

import com.mach.api.account.model.AccountAction;
import com.mach.api.account.model.AccountRequest;
import com.mach.api.client.RestClient;
import io.restassured.http.Method;
import io.restassured.response.ValidatableResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Client for Account API - User creation service.
 * Provides methods to create new user accounts.
 */
public class AccountApiClient {

    private static final Logger LOG = LoggerFactory.getLogger(AccountApiClient.class);
    private static final String API_KEY = System.getenv("ACCOUNT_API_KEY") != null 
            ? System.getenv("ACCOUNT_API_KEY") 
            : System.getProperty("account.api.key", "ii92khXeY#q<t38W!8m{");
    private static final String BASE_URI = System.getenv("ACCOUNT_API_BASE_URI") != null 
            ? System.getenv("ACCOUNT_API_BASE_URI") 
            : System.getProperty("account.api.base.uri", "https://account-faker-api-staging-stg.soymach.com");
    
    private final RestClient restClient;

    /**
     * Constructor with Bearer token
     */
    public AccountApiClient(String bearerToken) {
        this.restClient = new RestClient(BASE_URI, "", bearerToken);
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
        headers.put("X-Api-Key", API_KEY);
        headers.put("Content-Type", "application/json");
        return headers;
    }

}

