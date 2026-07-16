package com.vorynt.vorynt_api.controllers.user;

import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.dtos.user.CreateUserRequest;
import com.vorynt.vorynt_api.dtos.user.UserResponse;
import com.vorynt.vorynt_api.mappers.UserMapper;
import com.vorynt.vorynt_api.services.user.CreateUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserService createUserService;

    @PostMapping
    public ResponseEntity<UserResponse> create(
            @Valid @RequestBody CreateUserRequest request
    ) {

        User user = createUserService.execute(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password()
        );

        UserResponse response = UserMapper.toResponse(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
