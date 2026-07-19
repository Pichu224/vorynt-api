package com.vorynt.vorynt_api.services.user;

import com.vorynt.vorynt_api.domain.exceptions.EmailAlreadyExistsException;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;

    @Transactional
    public User execute(
            String firstName,
            String lastName,
            String email,
            String passwordHash
    ) {

        Email userEmail = Email.of(email);

        this.verifyEmailAvailability(userEmail);

        User newUser = User.create(firstName, lastName, userEmail, passwordHash);

        try {
            return userRepository.save(newUser);

        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException(userEmail);
        }
    }

    private void verifyEmailAvailability(Email email) {
        if (userRepository.existsByEmailAndEnabledTrue(email))
            throw new EmailAlreadyExistsException(email);
    }
}