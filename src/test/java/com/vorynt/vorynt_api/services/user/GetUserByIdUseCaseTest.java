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
class GetUserByIdUseCaseTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private GetUserByIdUseCase useCase;

    @Test
    void shouldReturnUser() {
        // Arrange
        User user = User.create(
                "Alan",
                "acuna",
                Email.of("alan@gmail.com"),
                "hash"
        );

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = useCase.execute(1L);

        // Assert
        assertEquals(user, result);
        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UserNotFoundException.class,
                () -> useCase.execute(1L)
        );
    }
}