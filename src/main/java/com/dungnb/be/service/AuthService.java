package com.dungnb.be.service;

import com.dungnb.be.dto.auth.SignInRequest;
import com.dungnb.be.dto.auth.SignInResponse;
import com.dungnb.be.dto.auth.SignUpRequest;
import com.dungnb.be.dto.auth.UserResponse;
import com.dungnb.be.entity.Role;
import com.dungnb.be.entity.User;
import com.dungnb.be.exception.DuplicateResourceException;
import com.dungnb.be.exception.ResourceNotFoundException;
import com.dungnb.be.mapper.AuthMapper;
import com.dungnb.be.repository.RoleRepository;
import com.dungnb.be.repository.UserRepository;
import com.dungnb.be.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthMapper authMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserResponse signUp(SignUpRequest request) {
        log.info("Attempting to sign up user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Sign up failed: Email already exists - {}", request.getEmail());
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        User user = authMapper.toUser(request);

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("USER"));
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        log.info("User signed up successfully with email: {}", savedUser.getEmail());

        return authMapper.toUserResponse(savedUser);
    }

    public SignInResponse signIn(SignInRequest request) {
        log.info("Attempting to sign in user with email: {}", request.getEmail());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            log.warn("Sign in failed: User not found with email: {}", request.getEmail());
            throw new ResourceNotFoundException("User", "email", request.getEmail());
        }

        if (!user.isActive()) {
            log.warn("Sign in failed: User account is inactive for email: {}", request.getEmail());
            throw new com.dungnb.be.exception.BusinessException("User account is inactive");
        }

        String token = jwtUtil.generateToken(user);

        log.info("User signed in successfully with email: {}", user.getEmail());
        return SignInResponse.builder().accessToken(token).user(authMapper.toUserResponse(user)).build();
    }
}
