package com.vorynt.vorynt_api.services.user;

import com.vorynt.vorynt_api.domain.exceptions.EmailAlreadyExistsException;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    @Transactional
    public User execute(
            String firstName,
            String lastName,
            String email,
            String passwordHash
    ) {

        Email userEmail = Email.of(email);

        User existingUser = userRepository.findByEmail(userEmail)
                .orElse(null);

        if (existingUser == null) {
            return createNewUser(
                    firstName,
                    lastName,
                    userEmail,
                    passwordHash
            );
        }

        if (existingUser.isEnabled()) {
            throw new EmailAlreadyExistsException(userEmail);
        }

        return reactivateUser(
                existingUser,
                firstName,
                lastName,
                passwordHash
        );
    }

    private User createNewUser(
            String firstName,
            String lastName,
            Email email,
            String passwordHash
    ) {

        User user = User.create(
                firstName,
                lastName,
                email,
                passwordHash
        );

        return userRepository.save(user);
    }

    private User reactivateUser(
            User user,
            String firstName,
            String lastName,
            String passwordHash
    ) {

        user.reactivate(
                firstName,
                lastName,
                passwordHash
        );

        return userRepository.save(user);
    }
}