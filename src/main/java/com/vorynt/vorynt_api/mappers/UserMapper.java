package com.vorynt.vorynt_api.mappers;

import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.dtos.user.UserResponse;

public final class UserMapper {

    private UserMapper() {}

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail().getValue()
        );
    }
}
