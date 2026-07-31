package com.vorynt.vorynt_api.controllers.auth;

import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.dtos.auth.AuthResponse;
import com.vorynt.vorynt_api.dtos.auth.LoginRequest;
import com.vorynt.vorynt_api.dtos.auth.RegisterRequest;
import com.vorynt.vorynt_api.services.auth.LoginUseCase;
import com.vorynt.vorynt_api.services.auth.RegisterUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = loginUseCase.execute(loginRequest.email(),  loginRequest.password());

        return new AuthResponse(token);
    }

    @PostMapping("/register")
    public AuthResponse auth(@Valid @RequestBody RegisterRequest registerRequest) {
        registerUserUseCase.execute(
                registerRequest.firstName(),
                registerRequest.lastName(),
                registerRequest.email(),
                registerRequest.password()
        );

        String token = loginUseCase.execute(
                registerRequest.email(),
                registerRequest.password()
        );

        return new AuthResponse(token);
    }
}
