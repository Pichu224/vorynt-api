package com.vorynt.vorynt_api.security;

import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import javax.crypto.SecretKey;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static String SECRET;

    private JwtService jwtService;

    private User user;

    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {

        jwtService = new JwtService();

        SecretKey key = Jwts.SIG.HS512.key().build();

        String SECRET = Encoders.BASE64.encode(
                key.getEncoded()
        );

        ReflectionTestUtils.setField(
                jwtService,
                "secret",
                SECRET
        );

        ReflectionTestUtils.setField(
                jwtService,
                "expiration",
                60_000L
        );

        user = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "passwordHash"
        );

        userDetails = CustomUserDetails.from(user);
    }

    @Test
    void shouldGenerateValidToken() {

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractUsernameFromToken() {

        String token = jwtService.generateToken(user);

        String username = jwtService.extractUsername(token);

        assertEquals(
                user.getEmail().getValue(),
                username
        );
    }

    @Test
    void shouldValidateTokenSuccessfully() {

        String token = jwtService.generateToken(user);

        assertTrue(
                jwtService.isTokenValid(
                        token,
                        userDetails
                )
        );
    }

    @Test
    void shouldReturnFalseWhenTokenBelongsToAnotherUser() {

        String token = jwtService.generateToken(user);

        User anotherUser = User.create(
                "Juan",
                "Perez",
                Email.of("juan@gmail.com"),
                "passwordHash"
        );

        CustomUserDetails anotherUserDetails =
                CustomUserDetails.from(anotherUser);

        assertFalse(
                jwtService.isTokenValid(
                        token,
                        anotherUserDetails
                )
        );
    }

    @Test
    void shouldExtractExpirationDate() {

        String token = jwtService.generateToken(user);

        OffsetDateTime expiration =
                jwtService.extractExpiration(token);

        assertTrue(
                expiration.isAfter(
                        OffsetDateTime.now()
                )
        );

        assertTrue(
                expiration.isBefore(
                        OffsetDateTime.now().plusMinutes(2)
                )
        );
    }
}