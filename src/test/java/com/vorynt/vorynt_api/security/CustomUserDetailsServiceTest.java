package com.vorynt.vorynt_api.security;

import com.vorynt.vorynt_api.domain.user.Role;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {

        user = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "passwordHash",
                Role.USER
        );
    }

    @Test
    void shouldLoadUserSuccessfully() {

        when(userRepository.findByEmail(
                Email.of("alan@gmail.com")
        )).thenReturn(Optional.of(user));

        CustomUserDetails userDetails =
                (CustomUserDetails) userDetailsService
                        .loadUserByUsername("alan@gmail.com");

        assertEquals(
                user.getId(),
                userDetails.getId()
        );

        assertEquals(
                user.getEmail().getValue(),
                userDetails.getUsername()
        );

        assertEquals(
                user.getPasswordHash(),
                userDetails.getPassword()
        );

        assertEquals(
                user.isEnabled(),
                userDetails.isEnabled()
        );

        assertEquals(
                user.getRole(),
                userDetails.getRole()
        );

        verify(userRepository)
                .findByEmail(Email.of("alan@gmail.com"));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepository.findByEmail(
                Email.of("alan@gmail.com")
        )).thenReturn(Optional.empty());

        UsernameNotFoundException exception =
                assertThrows(
                        UsernameNotFoundException.class,
                        () -> userDetailsService
                                .loadUserByUsername("alan@gmail.com")
                );

        assertEquals(
                "User not found.",
                exception.getMessage()
        );

        verify(userRepository)
                .findByEmail(Email.of("alan@gmail.com"));
    }
}