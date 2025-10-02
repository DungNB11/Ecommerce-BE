package com.dungnb.be.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInResponse {
    private String accessToken;
    private UserResponse user;
}
