package com.dungnb.be.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String email;
    private String fullName;
    private Boolean active;
}
