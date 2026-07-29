package com.vorynt.vorynt_api.security;

import com.vorynt.vorynt_api.domain.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = new CustomUserDetails(
                1L,
                "alan@gmail.com",
                "passwordHash",
                true,
                Role.USER
        );
    }

    @Test
    void shouldReturnId() {

        assertEquals(
                1L,
                userDetails.getId()
        );
    }

    @Test
    void shouldReturnUsername() {

        assertEquals(
                "alan@gmail.com",
                userDetails.getUsername()
        );
    }

    @Test
    void shouldReturnPassword() {

        assertEquals(
                "passwordHash",
                userDetails.getPassword()
        );
    }

    @Test
    void shouldReturnEnabled() {

        assertTrue(
                userDetails.isEnabled()
        );
    }

    @Test
    void shouldReturnAuthorities() {

        Collection<? extends GrantedAuthority> authorities =
                userDetails.getAuthorities();

        assertEquals(
                1,
                authorities.size()
        );

        GrantedAuthority authority =
                authorities.iterator().next();

        assertEquals(
                "ROLE_USER",
                authority.getAuthority()
        );
    }

    @Test
    void shouldReturnAccountNonExpired() {

        assertTrue(
                userDetails.isAccountNonExpired()
        );
    }

    @Test
    void shouldReturnAccountNonLocked() {

        assertTrue(
                userDetails.isAccountNonLocked()
        );
    }

    @Test
    void shouldReturnCredentialsNonExpired() {

        assertTrue(
                userDetails.isCredentialsNonExpired()
        );
    }

    @Test
    void shouldReturnDisabledWhenUserIsDisabled() {

        CustomUserDetails disabledUser =
                new CustomUserDetails(
                        2L,
                        "juan@gmail.com",
                        "passwordHash",
                        false,
                        Role.ADMIN
                );

        assertFalse(
                disabledUser.isEnabled()
        );
    }

    @Test
    void shouldReturnAdminAuthority() {

        CustomUserDetails admin =
                new CustomUserDetails(
                        2L,
                        "admin@gmail.com",
                        "passwordHash",
                        true,
                        Role.ADMIN
                );

        GrantedAuthority authority =
                admin.getAuthorities()
                        .iterator()
                        .next();

        assertEquals(
                "ROLE_ADMIN",
                authority.getAuthority()
        );
    }
}