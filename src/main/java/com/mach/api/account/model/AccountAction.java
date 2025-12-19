package com.mach.api.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Action model for Account API requests.
 * Represents an action to be executed during account creation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountAction {
    private String name;
    private Map<String, Object> args;
}

