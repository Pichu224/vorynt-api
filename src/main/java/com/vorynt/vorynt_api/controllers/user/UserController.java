package com.vorynt.vorynt_api.controllers.user;

import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.dtos.user.CreateUserRequest;
import com.vorynt.vorynt_api.dtos.user.UserResponse;
import com.vorynt.vorynt_api.mappers.UserMapper;
import com.vorynt.vorynt_api.services.user.CreateUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    @PostMapping
    public ResponseEntity<UserResponse> create(
            @Valid @RequestBody CreateUserRequest request
    ) {

        User user = createUserUseCase.execute(
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
