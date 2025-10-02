package com.dungnb.be.mapper;

import com.dungnb.be.dto.auth.SignUpRequest;
import com.dungnb.be.dto.auth.UserResponse;
import com.dungnb.be.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target="roles", ignore = true)
    @Mapping(target="active", constant = "true")
    User toUser(SignUpRequest signUpRequest);

    UserResponse toUserResponse(User user);
}
