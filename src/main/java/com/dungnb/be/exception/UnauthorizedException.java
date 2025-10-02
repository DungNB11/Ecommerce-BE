package com.dungnb.be.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BusinessException {
    
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION_ERROR.getCode());
    }
    
    public UnauthorizedException() {
        super("Authentication required", HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION_ERROR.getCode());
    }
}
