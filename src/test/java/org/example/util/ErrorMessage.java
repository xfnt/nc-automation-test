package org.example.util;

public enum ErrorMessage {
    INVALID_ACTION("action: invalid action '%s'. Allowed: LOGIN, LOGOUT, ACTION"),
    TOKEN_ERROR_MESSAGE("token: must match \"^[0-9A-Z]{32}$\""),
    TOKEN_ALREADY_EXISTS("Token '%s' already exists"),
    TOKEN_NOT_FOUND("Token '%s' not found"),
    TOKEN_MUST_NOT_BE_NULL("token: must not be null");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
