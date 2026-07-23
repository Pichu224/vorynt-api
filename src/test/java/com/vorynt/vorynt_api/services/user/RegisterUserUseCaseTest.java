package com.vorynt.vorynt_api.services.user;

import com.vorynt.vorynt_api.domain.exceptions.EmailAlreadyExistsException;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import com.vorynt.vorynt_api.services.auth.RegisterUserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUserUseCase useCase;

    @Test
    void shouldCreateUserSuccessfully() {

        // Arrange
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(passwordEncoder.encode(any()))
                .thenReturn("password");
        // Act
        User user = useCase.execute(
                "Alan",
                "Acuna",
                "alan@gmail.com",
                "passwordHash"
        );

        // Assert
        assertAll(
                () -> assertEquals("Alan", user.getFirstName()),
                () -> assertEquals("Acuna", user.getLastName()),
                () -> assertEquals("alan@gmail.com", user.getEmail().getValue()),
                () -> assertEquals("password", user.getPasswordHash()),
                () -> assertTrue(user.isEnabled())
        );

        verify(userRepository).findByEmail(any());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(any());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExistsAndUserIsEnabled() {

        // Arrange
        User existingUser = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "hash"
        );

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(
                EmailAlreadyExistsException.class,
                () -> useCase.execute(
                        "Juan",
                        "Perez",
                        "alan@gmail.com",
                        "passwordHash"
                )
        );

        verify(userRepository).findByEmail(any());
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void shouldReactivateDisabledUser() {

        // Arrange
        User existingUser = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "oldHash"
        );

        existingUser.deactivate();

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(passwordEncoder.encode(any()))
                .thenReturn("password");

        // Act
        User user = useCase.execute(
                "Juan",
                "Perez",
                "alan@gmail.com",
                "newHash"
        );

        // Assert
        assertAll(
                () -> assertTrue(user.isEnabled()),
                () -> assertEquals("Juan", user.getFirstName()),
                () -> assertEquals("Perez", user.getLastName()),
                () -> assertEquals("password", user.getPasswordHash())
        );

        verify(userRepository).findByEmail(any());
        verify(userRepository).save(existingUser);
        verify(passwordEncoder).encode(any());
    }
}