package com.mach.api.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for Account API.
 * Used to create new user accounts.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {
    private AccountAction[] actions;
}

