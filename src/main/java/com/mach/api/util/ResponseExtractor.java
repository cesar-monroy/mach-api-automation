package com.mach.api.util;

import io.restassured.response.Response;

/**
 * Utility class for extracting data from REST API responses.
 * Useful when using the framework as a library.
 */
public class ResponseExtractor {

    /**
     * Extract response body as specified class
     */
    public static <T> T asObject(Response response, Class<T> clazz) {
        return response.getBody().as(clazz);
    }

    /**
     * Extract JSON path value as String
     */
    public static String extractJsonPath(Response response, String jsonPath) {
        return response.jsonPath().getString(jsonPath);
    }
}

