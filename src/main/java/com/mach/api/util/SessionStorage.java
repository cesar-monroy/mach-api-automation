package com.mach.api.util;

import io.restassured.response.Response;

/**
 * Utility class to store session data like last response.
 * Useful for accessing the last response in tests or library usage.
 * Thread-safe implementation.
 */
public class SessionStorage {

    private static Response lastResponse;

    private SessionStorage() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Store the last response (thread-safe)
     */
    public static synchronized void setLastResponse(Response response) {
        lastResponse = response;
    }

    /**
     * Get the last stored response (thread-safe)
     */
    public static synchronized Response getLastResponse() {
        return lastResponse;
    }

    /**
     * Clear the stored response (thread-safe)
     */
    public static synchronized void clear() {
        lastResponse = null;
    }
}

