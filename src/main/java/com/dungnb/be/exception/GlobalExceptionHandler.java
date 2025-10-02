package com.dungnb.be.exception;

import com.dungnb.be.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý Business Exception
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        
        log.warn("Business exception: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .error(ex.getErrorCode())
                .status(ex.getStatus().value())
                .timestamp(java.time.LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    // Xử lý Validation Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        log.warn("Validation exception: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            Object rejectedValue = ((FieldError) error).getRejectedValue();
            
            fieldErrors.add(ErrorResponse.FieldError.builder()
                    .field(fieldName)
                    .rejectedValue(rejectedValue)
                    .message(errorMessage)
                    .build());
        });
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Validation failed")
                .error("VALIDATION_ERROR")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(java.time.LocalDateTime.now())
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Xử lý Constraint Violation Exception
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        
        log.warn("Constraint violation: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Constraint violation: " + ex.getMessage())
                .error("CONSTRAINT_VIOLATION")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(java.time.LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Xử lý Security Exception
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            Exception ex, HttpServletRequest request) {
        
        log.warn("Authentication exception: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Authentication failed")
                .error("AUTHENTICATION_ERROR")
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(java.time.LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {
        
        log.warn("Access denied: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Access denied")
                .error("ACCESS_DENIED")
                .status(HttpStatus.FORBIDDEN.value())
                .timestamp(java.time.LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // Xử lý Method Argument Type Mismatch
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        log.warn("Type mismatch: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(), ex.getName(), ex.getRequiredType().getSimpleName());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(message)
                .error("TYPE_MISMATCH")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(java.time.LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Xử lý Illegal Argument Exception
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        
        log.warn("Illegal argument: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .error("ILLEGAL_ARGUMENT")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(java.time.LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Xử lý Generic Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        log.error("Unexpected error occurred: {} - Path: {}", ex.getMessage(), 
                request.getRequestURI(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("An unexpected error occurred")
                .error("INTERNAL_SERVER_ERROR")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(java.time.LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
