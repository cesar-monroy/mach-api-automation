package com.mach.api.test;

import com.mach.api.client.RestClient;
import com.mach.api.config.ApiConfig;
import io.restassured.http.Method;
import io.restassured.response.ValidatableResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.Map;

/**
 * Base class for API testing.
 * Extend this class to create your test classes.
 */
public abstract class BaseApiTest {

    protected static final Logger LOG = LoggerFactory.getLogger(BaseApiTest.class);
    protected RestClient restClient;
    protected ApiConfig config;

    @BeforeClass
    public void setUpClass() {
        config = ApiConfig.getInstance();
        
        if (config.hasAuth()) {
            if (config.getBearerToken() != null) {
                restClient = new RestClient(config.getBaseUri(), config.getBasePath(), config.getBearerToken());
            } else if (config.getUsername() != null && config.getPassword() != null) {
                restClient = new RestClient(config.getBaseUri(), config.getBasePath(), 
                                          config.getUsername(), config.getPassword());
            }
        } else {
            restClient = new RestClient(config.getBaseUri(), config.getBasePath());
        }

        LOG.info("API Test setup completed. Base URI: {}", config.getBaseUri());
    }

    @BeforeMethod
    public void setUpMethod() {
        LOG.debug("Starting test method execution");
    }

    /**
     * Execute simple web service (GET, DELETE, etc. without body)
     */
    protected ValidatableResponse runSimpleWebService(Method httpMethod, String resource) {
        return restClient.runSimpleWebService(httpMethod, resource);
    }

    /**
     * Execute simple web service with headers
     */
    protected ValidatableResponse runSimpleWebService(Method httpMethod, Map<String, ?> headers, String resource) {
        return restClient.runSimpleWebService(httpMethod, headers, resource);
    }

    /**
     * Execute simple web service with headers and query parameters
     */
    protected ValidatableResponse runSimpleWebService(Method httpMethod, Map<String, ?> headers, 
                                                      Map<String, ?> parameters, String resource) {
        return restClient.runSimpleWebService(httpMethod, headers, parameters, resource);
    }

    /**
     * Execute simple web service with headers and path parameters
     */
    protected ValidatableResponse runSimpleWebService(Method httpMethod, Map<String, ?> headers, 
                                                      String resource, Map<String, ?> pathParameters) {
        return restClient.runSimpleWebService(httpMethod, headers, resource, pathParameters);
    }

    /**
     * Execute web service with body (POST, PUT, PATCH)
     */
    protected ValidatableResponse runWebServiceWithBody(Method httpMethod, String resource, Object requestBody) {
        return restClient.runWebServiceWithBody(httpMethod, resource, requestBody);
    }

    /**
     * Execute web service with body and custom headers
     */
    protected ValidatableResponse runWebServiceWithBody(Method httpMethod, Map<String, ?> headers, 
                                                         String resource, Object requestBody) {
        return restClient.runWebServiceWithBody(httpMethod, headers, resource, requestBody);
    }

    /**
     * Execute web service with body and path parameters
     */
    protected ValidatableResponse runWebServiceWithBodyAndPathParams(Method httpMethod, Map<String, ?> headers,
                                                                     String resource, Map<String, ?> pathParameters,
                                                                     Object requestBody) {
        return restClient.runWebServiceWithBodyAndPathParams(httpMethod, headers, resource, pathParameters, requestBody);
    }

    /**
     * Get RestClient instance (for advanced usage)
     */
    protected RestClient getRestClient() {
        return restClient;
    }
}

