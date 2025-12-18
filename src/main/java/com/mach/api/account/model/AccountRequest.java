package com.mach.api.account.model;

/**
 * Request model for Account API.
 * Used to create new user accounts.
 */
public class AccountRequest {
    private AccountAction[] actions;

    public AccountAction[] getActions() {
        return actions;
    }

    public void setActions(AccountAction[] actions) {
        this.actions = actions;
    }
}

