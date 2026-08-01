package com.vorynt.vorynt_api.services.user;

import com.vorynt.vorynt_api.domain.exceptions.EmailAlreadyExistsException;
import com.vorynt.vorynt_api.domain.user.Role;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCase useCase;

    @Test
    void shouldCreateUser() {

        // Arrange

        when(repository.existsByEmail(Email.of("alan@gmail.com")))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encoded-password");

        when(repository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act

        User created = useCase.execute(
                "Alan",
                "Acuna",
                "alan@gmail.com",
                "123456"
        );

        // Assert

        assertEquals("Alan", created.getFirstName());
        assertEquals("Acuna", created.getLastName());
        assertEquals("alan@gmail.com", created.getEmail().getValue());
        assertEquals("encoded-password", created.getPasswordHash());
        assertEquals(Role.USER, created.getRole());
        assertTrue(created.isEnabled());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        verify(repository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals("Alan", savedUser.getFirstName());
        assertEquals("Acuna", savedUser.getLastName());
        assertEquals("alan@gmail.com", savedUser.getEmail().getValue());
        assertEquals("encoded-password", savedUser.getPasswordHash());
        assertEquals(Role.USER, savedUser.getRole());
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {

        // Arrange

        when(repository.existsByEmail(Email.of("alan@gmail.com")))
                .thenReturn(true);

        // Act & Assert

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> useCase.execute(
                        "Alan",
                        "Acuna",
                        "alan@gmail.com",
                        "123456"
                )
        );

        verify(repository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }
}