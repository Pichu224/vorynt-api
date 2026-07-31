package com.vorynt.vorynt_api.services.auth;

import com.vorynt.vorynt_api.domain.exceptions.EmailAlreadyExistsException;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User execute(
            String firstName,
            String lastName,
            String email,
            String rawPassword
    ) {

        Email userEmail = Email.of(email);

        User existingUser = userRepository.findByEmail(userEmail)
                .orElse(null);

        if (existingUser == null) {
            return createNewUser(
                    firstName,
                    lastName,
                    userEmail,
                    rawPassword
            );
        }

        if (existingUser.isEnabled()) {
            throw new EmailAlreadyExistsException(userEmail);
        }

        return reactivateUser(
                existingUser,
                firstName,
                lastName,
                rawPassword
        );
    }

    private User createNewUser(
            String firstName,
            String lastName,
            Email email,
            String rawPassword
    ) {
        String encodedPassword =
                passwordEncoder.encode(rawPassword);

        User user = User.create(
                firstName,
                lastName,
                email,
                encodedPassword
        );

        return userRepository.save(user);
    }

    private User reactivateUser(
            User user,
            String firstName,
            String lastName,
            String rawPassword
    ) {
        String encodedPassword =
                passwordEncoder.encode(rawPassword);

        user.reactivate(
                firstName,
                lastName,
                encodedPassword
        );

        return userRepository.save(user);
    }
}