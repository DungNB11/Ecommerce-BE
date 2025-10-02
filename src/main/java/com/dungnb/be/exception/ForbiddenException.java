package com.dungnb.be.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BusinessException {
    
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED.getCode());
    }
    
    public ForbiddenException() {
        super("Access denied", HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED.getCode());
    }
}
