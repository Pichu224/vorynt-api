package com.vorynt.vorynt_api.services.user;

import com.vorynt.vorynt_api.domain.exceptions.UserNotFoundException;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UpdateUserUseCase useCase;

    @Test
    void shouldUpdateUser() {

        // Arrange
        User user = User.create(
                "Alan",
                "acuna",
                Email.of("alan@gmail.com"),
                "hash"
        );

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User updated = useCase.execute(
                1L,
                "Carlos",
                "Gomez"
        );

        // Assert
        assertEquals("Carlos", updated.getFirstName());
        assertEquals("Gomez", updated.getLastName());
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {

        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UserNotFoundException.class,
                () -> useCase.execute(1L, "A", "B")
        );
    }
}