package com.vorynt.vorynt_api.controllers.user;

import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.dtos.user.CreateUserRequest;
import com.vorynt.vorynt_api.dtos.user.UpdateUserRequest;
import com.vorynt.vorynt_api.dtos.user.UserResponse;
import com.vorynt.vorynt_api.mappers.UserMapper;
import com.vorynt.vorynt_api.services.user.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponse> create(
            @Valid @RequestBody CreateUserRequest request
    ) {

        User user = registerUserUseCase.execute(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password()
        );

        UserResponse response = userMapper.toResponse(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateById(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) {

        User user =  updateUserUseCase.execute(
                id,
                request.firstName(),
                request.lastName()
        );

        UserResponse response = userMapper.toResponse(user);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {
        deleteUserUseCase.execute(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(
    ) {
        List<User> users = getAllUsersUseCase.execute();

        List<UserResponse> response = userMapper.toResponseList(users);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {
        User user = getUserByIdUseCase.execute(id);

        UserResponse response = userMapper.toResponse(user);

        return ResponseEntity.ok(response);
    }
}
