package com.vorynt.vorynt_api.mappers;

import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.dtos.user.UserResponse;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public final class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail().getValue()
        );
    }

    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream().map(this::toResponse).toList();
    }
}
