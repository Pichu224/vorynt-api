package com.vorynt.vorynt_api.services.user;

import com.vorynt.vorynt_api.domain.exceptions.UserNotFoundException;
import com.vorynt.vorynt_api.domain.user.Role;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import com.vorynt.vorynt_api.security.CustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCurrentUserUseCaseTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private GetCurrentUserUseCase useCase;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnCurrentUser() {

        // Arrange

        CustomUserDetails userDetails =
                new CustomUserDetails(
                        1L,
                        "alan@gmail.com",
                        "hash",
                        true,
                        Role.USER
                );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                )
        );

        User user = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "hash",
                Role.USER
        );

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.of(user));

        // Act

        User result = useCase.execute();

        // Assert

        assertEquals(user, result);

        verify(repository)
                .findByIdAndEnabledTrue(1L);
    }

    @Test
    void shouldThrowWhenCurrentUserDoesNotExist() {

        // Arrange

        CustomUserDetails userDetails =
                new CustomUserDetails(
                        1L,
                        "alan@gmail.com",
                        "hash",
                        true,
                        Role.USER
                );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                )
        );

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.empty());

        // Act & Assert

        assertThrows(
                UserNotFoundException.class,
                () -> useCase.execute()
        );

        verify(repository)
                .findByIdAndEnabledTrue(1L);
    }
}