package com.vorynt.vorynt_api.services.auth;

import com.vorynt.vorynt_api.domain.exceptions.InvalidCredentialsException;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import com.vorynt.vorynt_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String execute(
            String rawEmail,
            String rawPassword
    ) {
        Email email = Email.of(rawEmail);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            rawEmail,
                            rawPassword
                    )
            );
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        return jwtService.generateToken(user);
    }
}
