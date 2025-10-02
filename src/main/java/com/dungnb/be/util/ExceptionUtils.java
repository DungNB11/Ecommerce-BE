package com.dungnb.be.util;

import com.dungnb.be.exception.BusinessException;
import com.dungnb.be.exception.ResourceNotFoundException;

/**
 * Simple utility class for common exception operations
 */
public class ExceptionUtils {
    
    /**
     * Throws ResourceNotFoundException if object is null
     */
    public static <T> T throwIfNull(T object, String resourceName, String fieldName, Object fieldValue) {
        if (object == null) {
            throw new ResourceNotFoundException(resourceName, fieldName, fieldValue);
        }
        return object;
    }
    
    /**
     * Throws BusinessException if condition is true
     */
    public static void throwIfTrue(boolean condition, String message) {
        if (condition) {
            throw new BusinessException(message);
        }
    }
}
