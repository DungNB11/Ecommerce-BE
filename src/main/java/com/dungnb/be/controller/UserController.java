package com.dungnb.be.controller;

import com.dungnb.be.entity.User;
import com.dungnb.be.exception.ResourceNotFoundException;
import com.dungnb.be.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    
    private final UserRepository userRepository;
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable @Min(value = 1, message = "User ID must be positive") Long id) {
        log.info("Getting user by ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        return ResponseEntity.ok(user);
    }
    
        @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserByEmail(@PathVariable @NotBlank String email) {
        log.info("Getting user by email: {}", email);
        
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User", "email", email);
        }
        
        return ResponseEntity.ok(user);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        userRepository.delete(user);
        log.info("User deleted successfully with ID: {}", id);
        
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/test-validation")
    public ResponseEntity<String> testValidation(@RequestParam @Min(value = 1, message = "Value must be positive") Long value) {
        return ResponseEntity.ok("Validation passed with value: " + value);
    }
}
