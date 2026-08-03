package com.vorynt.vorynt_api.services.user;

import com.vorynt.vorynt_api.domain.exceptions.EmailAlreadyExistsException;
import com.vorynt.vorynt_api.domain.exceptions.UserNotFoundException;
import com.vorynt.vorynt_api.domain.user.Role;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User execute (
            String firstName,
            String lastName,
            String email,
            String rawPassword
    ) throws EmailAlreadyExistsException {

        Email userEmail = Email.of(email);

        if (userRepository.existsByEmail(userEmail))
            throw new EmailAlreadyExistsException(userEmail);

        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = User.create(
                firstName,
                lastName,
                userEmail,
                encodedPassword,
                Role.USER
        );

        return userRepository.save(user);
    }
}
