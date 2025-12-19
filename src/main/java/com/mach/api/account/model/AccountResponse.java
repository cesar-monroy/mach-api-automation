package com.mach.api.account.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * Response model for Account API.
 * Represents the response received after creating a user account.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private String accountId;
    private String status;
    private Map<String, Object> data;
}

