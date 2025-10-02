package com.dungnb.be.controller;

import com.dungnb.be.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Simple base controller with common response methods
 */
public abstract class BaseController {
    
    // ========== GET Requests ==========
    
    /**
     * Create success response with data (for GET requests)
     */
    protected <T> ResponseEntity<ApiResponse<T>> successResponse(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }
    
    /**
     * Create success response with data and message
     */
    protected <T> ResponseEntity<ApiResponse<T>> successResponse(T data, String message) {
        return ResponseEntity.ok(ApiResponse.success(data, message));
    }
    
    // ========== POST Requests ==========
    
    /**
     * Create created response with data (for POST requests)
     */
    protected <T> ResponseEntity<ApiResponse<T>> createdResponse(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(data, "Created successfully"));
    }
    
    /**
     * Create created response with data and custom message
     */
    protected <T> ResponseEntity<ApiResponse<T>> createdResponse(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(data, message));
    }
    
    // ========== PUT Requests ==========
    
    /**
     * Create updated response with data (for PUT requests)
     */
    protected <T> ResponseEntity<ApiResponse<T>> updatedResponse(T data) {
        return ResponseEntity.ok(ApiResponse.success(data, "Updated successfully"));
    }
    
    /**
     * Create updated response with data and custom message
     */
    protected <T> ResponseEntity<ApiResponse<T>> updatedResponse(T data, String message) {
        return ResponseEntity.ok(ApiResponse.success(data, message));
    }
    
    // ========== DELETE Requests ==========
    
    /**
     * Create no content response (for DELETE requests)
     */
    protected ResponseEntity<Void> noContentResponse() {
        return ResponseEntity.noContent().build();
    }
    
    // ========== Message Only Responses ==========
    
    /**
     * Create success response with message only (no data)
     */
    protected ResponseEntity<ApiResponse<String>> successMessage(String message) {
        return ResponseEntity.ok(ApiResponse.success(message));
    }
}
