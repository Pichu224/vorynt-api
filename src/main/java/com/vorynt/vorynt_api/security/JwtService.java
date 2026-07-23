package com.vorynt.vorynt_api.security;

import com.vorynt.vorynt_api.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getEmail().getValue())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis() + expiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    public boolean isTokenValid(
            String token,
            User user
    ) {
        String username = extractUsername(token);

        return username.equals(user.getEmail().getValue())
                && !isTokenExpired(token);
    }

    public OffsetDateTime extractExpiration(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .toInstant()
                .atOffset(ZoneOffset.UTC);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token)
                .isBefore(OffsetDateTime.now());
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
