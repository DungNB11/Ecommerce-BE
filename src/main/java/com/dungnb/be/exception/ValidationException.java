package com.dungnb.be.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends BusinessException {
    
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR.getCode());
    }
    
    public ValidationException(String field, String message) {
        super(String.format("Validation failed for field '%s': %s", field, message), 
              HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR.getCode());
    }
}
