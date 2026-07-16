package com.vorynt.vorynt_api.services.user;

import com.vorynt.vorynt_api.domain.exceptions.EmailAlreadyExistsException;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUserService service;

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User user = service.execute(
                "Alan",
                "acuna",
                "alan@gmail.com",
                "passwordHash"
        );

        // Assert
        assertNotNull(user);
        assertEquals("Alan", user.getFirstName());
        assertEquals("acuna", user.getLastName());
        assertEquals("alan@gmail.com", user.getEmail().getValue());

        verify(userRepository).existsByEmail(any());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByEmail(any())).thenReturn(true);

        // Act & Assert
        assertThrows(
                EmailAlreadyExistsException.class,
                () -> service.execute(
                        "Alan",
                        "Perez",
                        "alan@gmail.com",
                        "passwordHash"
                )
        );

        verify(userRepository).existsByEmail(any());
        verify(userRepository, never()).save(any());
    }
}