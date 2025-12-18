package com.mach.api.client;

import com.mach.api.config.ApiConfig;
import com.mach.api.util.SessionStorage;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Reusable REST Client for consuming REST services.
 * Can be used as a library in any Java project.
 */
public class RestClient {

    private static final Logger LOG = LoggerFactory.getLogger(RestClient.class);

    /**
     * Default constructor using ApiConfig for configuration
     */
    public RestClient() {
        ApiConfig config = ApiConfig.getInstance();
        if (config.hasAuth()) {
            if (config.getBearerToken() != null) {
                RestAssured.baseURI = config.getBaseUri();
                RestAssured.basePath = config.getBasePath();
                RestAssured.authentication = RestAssured.oauth2(config.getBearerToken());
            } else if (config.getUsername() != null && config.getPassword() != null) {
                RestAssured.baseURI = config.getBaseUri();
                RestAssured.basePath = config.getBasePath();
                RestAssured.authentication = RestAssured.preemptive().basic(config.getUsername(), config.getPassword());
            } else {
                RestAssured.baseURI = config.getBaseUri();
                RestAssured.basePath = config.getBasePath();
            }
        } else {
            RestAssured.baseURI = config.getBaseUri();
            RestAssured.basePath = config.getBasePath();
        }
    }

    /**
     * Constructor with base URI and base path
     */
    public RestClient(String baseURI, String basePath) {
        RestAssured.baseURI = baseURI;
        RestAssured.basePath = basePath;
    }

    /**
     * Constructor with base URI, base path and Bearer token
     */
    public RestClient(String baseURI, String basePath, String token) {
        RestAssured.baseURI = baseURI;
        RestAssured.basePath = basePath;
        RestAssured.authentication = RestAssured.oauth2(token);
    }

    /**
     * Constructor with base URI, base path and Basic Auth credentials
     */
    public RestClient(String baseURI, String basePath, String username, String password) {
        RestAssured.baseURI = baseURI;
        RestAssured.basePath = basePath;
        RestAssured.authentication = RestAssured.preemptive().basic(username, password);
    }

    /**
     * Get default request specification with filters
     */
    private static RequestSpecification defaultRequestSpecification() {
        List<Filter> filters = new ArrayList<>();
        filters.add(new RequestLoggingFilter());
        filters.add(new ResponseLoggingFilter());
        filters.add(new AllureRestAssured());
        return new RequestSpecBuilder().addFilters(filters).build();
    }

    /**
     * Execute web service with body
     * 
     * @param httpMethod HTTP method (GET, POST, PUT, PATCH, DELETE)
     * @param resource Resource endpoint
     * @param requestBody Request body object
     * @return ValidatableResponse for assertions
     */
    public ValidatableResponse runWebServiceWithBody(Method httpMethod, String resource, Object requestBody) {
        RequestSpecification requestSpec = given().spec(defaultRequestSpecification())
                .contentType(ContentType.JSON)
                .body(requestBody);
        return call(httpMethod, resource, requestSpec).then();
    }

    /**
     * Execute web service with body and custom headers
     * 
     * @param httpMethod HTTP method
     * @param headers Custom headers map
     * @param resource Resource endpoint
     * @param requestBody Request body object
     * @return ValidatableResponse for assertions
     */
    public ValidatableResponse runWebServiceWithBody(Method httpMethod, Map<String, ?> headers, 
                                                     String resource, Object requestBody) {
        RequestSpecification requestSpec = given().spec(defaultRequestSpecification())
                .contentType(ContentType.JSON)
                .headers(headers)
                .body(requestBody);
        return call(httpMethod, resource, requestSpec).then();
    }

    /**
     * Execute simple web service (without body)
     * 
     * @param httpMethod HTTP method
     * @param resource Resource endpoint
     * @return ValidatableResponse for assertions
     */
    public ValidatableResponse runSimpleWebService(Method httpMethod, String resource) {
        RequestSpecification requestSpec = given().spec(defaultRequestSpecification());
        return call(httpMethod, resource, requestSpec).then();
    }

    /**
     * Execute simple web service with headers and query parameters
     * 
     * @param httpMethod HTTP method
     * @param headers Custom headers map
     * @param parameters Query parameters map
     * @param resource Resource endpoint
     * @return ValidatableResponse for assertions
     */
    public ValidatableResponse runSimpleWebService(Method httpMethod, Map<String, ?> headers, 
                                                    Map<String, ?> parameters, String resource) {
        RequestSpecification requestSpec = given().spec(defaultRequestSpecification())
                .headers(headers)
                .queryParams(parameters);
        return call(httpMethod, resource, requestSpec).then();
    }

    /**
     * Execute simple web service with headers only
     * 
     * @param httpMethod HTTP method
     * @param headers Custom headers map
     * @param resource Resource endpoint
     * @return ValidatableResponse for assertions
     */
    public ValidatableResponse runSimpleWebService(Method httpMethod, Map<String, ?> headers, String resource) {
        RequestSpecification requestSpec = given().spec(defaultRequestSpecification())
                .headers(headers);
        return call(httpMethod, resource, requestSpec).then();
    }

    /**
     * Execute simple web service with headers and path parameters
     * 
     * @param httpMethod HTTP method
     * @param headers Custom headers map
     * @param resource Resource endpoint (with path parameters placeholders)
     * @param pathParameters Path parameters map
     * @return ValidatableResponse for assertions
     */
    public ValidatableResponse runSimpleWebService(Method httpMethod, Map<String, ?> headers, 
                                                   String resource, Map<String, ?> pathParameters) {
        RequestSpecification requestSpec = given().spec(defaultRequestSpecification())
                .headers(headers)
                .pathParams(pathParameters);
        return call(httpMethod, resource, requestSpec).then();
    }

    /**
     * Execute web service with body and path parameters
     * 
     * @param httpMethod HTTP method
     * @param headers Custom headers map (can be null or empty)
     * @param resource Resource endpoint (with path parameters placeholders)
     * @param pathParameters Path parameters map
     * @param requestBody Request body object
     * @return ValidatableResponse for assertions
     */
    public ValidatableResponse runWebServiceWithBodyAndPathParams(Method httpMethod, Map<String, ?> headers,
                                                                  String resource, Map<String, ?> pathParameters,
                                                                  Object requestBody) {
        RequestSpecification requestSpec = given().spec(defaultRequestSpecification())
                .contentType(ContentType.JSON)
                .body(requestBody);
        
        if (headers != null && !headers.isEmpty()) {
            requestSpec.headers(headers);
        }
        
        if (pathParameters != null && !pathParameters.isEmpty()) {
            requestSpec.pathParams(pathParameters);
        }
        
        return call(httpMethod, resource, requestSpec).then();
    }

    /**
     * Core method to execute HTTP requests
     * 
     * @param httpMethod HTTP method
     * @param resource Resource endpoint
     * @param requestSpecification Request specification
     * @return Response object
     */
    private Response call(Method httpMethod, String resource, RequestSpecification requestSpecification) {
        Response response;
        switch (httpMethod) {
            case GET:
                response = requestSpecification.when().get(resource);
                break;
            case POST:
                response = requestSpecification.when().post(resource);
                break;
            case PUT:
                response = requestSpecification.when().put(resource);
                break;
            case DELETE:
                response = requestSpecification.when().delete(resource);
                break;
            case PATCH:
                response = requestSpecification.when().patch(resource);
                break;
            default:
                throw new InvalidParameterException("Invalid Http Method: " + httpMethod);
        }
        
        SessionStorage.setLastResponse(response);
        LOG.debug("{} {} - Status: {}", httpMethod, resource, response.getStatusCode());
        return response;
    }

    /**
     * Get the last response stored in SessionStorage
     * 
     * @return Last Response object or null if no request has been made
     */
    public static Response getLastResponse() {
        return SessionStorage.getLastResponse();
    }
}
