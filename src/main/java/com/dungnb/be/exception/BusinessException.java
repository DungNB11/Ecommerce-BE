package com.dungnb.be.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private final HttpStatus status;
    private final String errorCode;
    
    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = ErrorCode.BUSINESS_ERROR.getCode();
    }
    
    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = ErrorCode.BUSINESS_ERROR.getCode();
    }
    
    public BusinessException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
    
    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode.getCode();
        this.status = determineHttpStatus(errorCode);
    }
    
    private HttpStatus determineHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case RESOURCE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DUPLICATE_RESOURCE -> HttpStatus.CONFLICT;
            case VALIDATION_ERROR -> HttpStatus.BAD_REQUEST;
            case AUTHENTICATION_ERROR -> HttpStatus.UNAUTHORIZED;
            case ACCESS_DENIED -> HttpStatus.FORBIDDEN;
            case RATE_LIMIT_EXCEEDED, TOO_MANY_REQUESTS -> HttpStatus.TOO_MANY_REQUESTS;
            case SERVICE_UNAVAILABLE -> HttpStatus.SERVICE_UNAVAILABLE;
            case EXTERNAL_SERVICE_ERROR -> HttpStatus.BAD_GATEWAY;
            case DATABASE_ERROR, INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
