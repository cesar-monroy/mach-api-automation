package com.mach.api.account.model;

import java.util.Map;

/**
 * Response model for Account API.
 * Represents the response received after creating a user account.
 */
public class AccountResponse {
    private String accountId;
    private String status;
    private Map<String, Object> data;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}

