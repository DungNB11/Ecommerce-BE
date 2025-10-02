package com.dungnb.be.config;

import com.dungnb.be.entity.Role;
import com.dungnb.be.entity.User;
import com.dungnb.be.repository.RoleRepository;
import com.dungnb.be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (!userRepository.existsByEmail("admin@admin.com")) {
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByName("ADMIN"));
                User user = User.builder()
                        .email("admin@admin.com")
                        .password(passwordEncoder.encode("123123"))
                        .fullName("Admin")
                        .roles(roles)
                        .active(true)
                        .build();
                userRepository.save(user);
                log.info("Admin user has been created");

            }
        };
    }
}
