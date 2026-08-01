package com.vorynt.vorynt_api.services.auth;

import com.vorynt.vorynt_api.domain.exceptions.InvalidCredentialsException;
import com.vorynt.vorynt_api.domain.user.Role;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import com.vorynt.vorynt_api.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @Mock
    private UserRepository repository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private LoginUserUseCase useCase;

    @Test
    void shouldLoginSuccessfully() {

        // Arrange

        User user = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "hash",
                Role.USER
        );

        when(repository.findByEmail(Email.of("alan@gmail.com")))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        // Act

        String token = useCase.execute(
                "alan@gmail.com",
                "123456"
        );

        // Assert

        assertEquals("jwt-token", token);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(
                        "alan@gmail.com",
                        "123456"
                )
        );

        verify(repository)
                .findByEmail(Email.of("alan@gmail.com"));

        verify(jwtService)
                .generateToken(user);
    }

    @Test
    void shouldThrowWhenCredentialsAreInvalid() {

        // Arrange

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act & Assert

        assertThrows(
                InvalidCredentialsException.class,
                () -> useCase.execute(
                        "alan@gmail.com",
                        "123456"
                )
        );

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verifyNoInteractions(repository);
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldThrowWhenUserDoesNotExistAfterAuthentication() {

        // Arrange

        when(repository.findByEmail(Email.of("alan@gmail.com")))
                .thenReturn(Optional.empty());

        // Act & Assert

        assertThrows(
                InvalidCredentialsException.class,
                () -> useCase.execute(
                        "alan@gmail.com",
                        "123456"
                )
        );

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(repository)
                .findByEmail(Email.of("alan@gmail.com"));

        verifyNoInteractions(jwtService);
    }
}