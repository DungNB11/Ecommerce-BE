package com.dungnb.be.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends BusinessException {
    
    private final String serviceName;
    
    public ExternalServiceException(String serviceName, String message) {
        super(String.format("External service '%s' error: %s", serviceName, message), 
              HttpStatus.BAD_GATEWAY, ErrorCode.EXTERNAL_SERVICE_ERROR.getCode());
        this.serviceName = serviceName;
    }
    
    public ExternalServiceException(String serviceName, String message, Throwable cause) {
        super(String.format("External service '%s' error: %s", serviceName, message), 
              HttpStatus.BAD_GATEWAY, ErrorCode.EXTERNAL_SERVICE_ERROR.getCode());
        this.serviceName = serviceName;
        initCause(cause);
    }
    
    public String getServiceName() {
        return serviceName;
    }
}
