package com.dungnb.be.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Business Logic Errors (4xx)
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "Resource not found"),
    DUPLICATE_RESOURCE("DUPLICATE_RESOURCE", "Resource already exists"),
    VALIDATION_ERROR("VALIDATION_ERROR", "Validation failed"),
    BUSINESS_ERROR("BUSINESS_ERROR", "Business logic error"),
    INVALID_OPERATION("INVALID_OPERATION", "Invalid operation"),
    INSUFFICIENT_PERMISSIONS("INSUFFICIENT_PERMISSIONS", "Insufficient permissions"),
    
    // Authentication & Authorization (4xx)
    AUTHENTICATION_ERROR("AUTHENTICATION_ERROR", "Authentication failed"),
    ACCESS_DENIED("ACCESS_DENIED", "Access denied"),
    TOKEN_EXPIRED("TOKEN_EXPIRED", "Token has expired"),
    INVALID_TOKEN("INVALID_TOKEN", "Invalid token"),
    
    // Data & Format Errors (4xx)
    CONSTRAINT_VIOLATION("CONSTRAINT_VIOLATION", "Constraint violation"),
    TYPE_MISMATCH("TYPE_MISMATCH", "Type mismatch"),
    ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", "Illegal argument"),
    BAD_REQUEST("BAD_REQUEST", "Bad request"),
    
    // Server Errors (5xx)
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error"),
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", "Service unavailable"),
    EXTERNAL_SERVICE_ERROR("EXTERNAL_SERVICE_ERROR", "External service error"),
    DATABASE_ERROR("DATABASE_ERROR", "Database error"),
    
    // Rate Limiting & Throttling (4xx)
    RATE_LIMIT_EXCEEDED("RATE_LIMIT_EXCEEDED", "Rate limit exceeded"),
    TOO_MANY_REQUESTS("TOO_MANY_REQUESTS", "Too many requests");
    
    private final String code;
    private final String defaultMessage;
    
    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
