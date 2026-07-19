package com.vorynt.vorynt_api.services.user;

import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllUsersUseCaseTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private GetAllUsersUseCase useCase;

    @Test
    void shouldReturnAllUsers() {

        // Arrange
        List<User> users = List.of(
                User.create("Alan", "acuna", Email.of("alan@gmail.com"), "hash"),
                User.create("Juan", "Perez", Email.of("juan@gmail.com"), "hash")
        );

        when(repository.findAllByEnabledTrue()).thenReturn(users);

        // Act
        List<User> result = useCase.execute();

        // Assert
        assertEquals(2, result.size());
        verify(repository).findAllByEnabledTrue();
    }
}