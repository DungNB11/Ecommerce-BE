package com.dungnb.be.controller;

import com.dungnb.be.dto.ApiResponse;
import com.dungnb.be.dto.auth.SignInRequest;
import com.dungnb.be.dto.auth.SignInResponse;
import com.dungnb.be.dto.auth.SignUpRequest;
import com.dungnb.be.dto.auth.UserResponse;
import com.dungnb.be.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController extends BaseController {
    
    private final AuthService authService;
    
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
        log.info("Sign up request received for email: {}", request.getEmail());
        UserResponse user = authService.signUp(request);
        log.info("User signed up successfully: {}", user.getEmail());
        return createdResponse(user, "User registered successfully");
    }
    
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<SignInResponse>> signIn(@Valid @RequestBody SignInRequest request) {
        log.info("Sign in request received for email: {}", request.getEmail());
        SignInResponse user = authService.signIn(request);
        return successResponse(user, "Login successful");
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        log.info("Auth test endpoint accessed");
        return successMessage("Auth endpoint is working!");
    }
}
