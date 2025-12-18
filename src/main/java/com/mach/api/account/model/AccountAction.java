package com.mach.api.account.model;

import java.util.Map;

/**
 * Action model for Account API requests.
 * Represents an action to be executed during account creation.
 */
public class AccountAction {
    private String name;
    private Map<String, Object> args;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }
}

