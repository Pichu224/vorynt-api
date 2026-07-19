package com.vorynt.vorynt_api.services.user;

import com.vorynt.vorynt_api.domain.exceptions.UserNotFoundException;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetUserByIdUseCase {

    private final UserRepository userRepository;

    public User execute(Long id) {
        return userRepository.findByIdAndEnabledTrue(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
