package com.vorynt.vorynt_api.services.user;

import com.vorynt.vorynt_api.domain.exceptions.UserNotFoundException;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DeleteUserUseCase {

    private final UserRepository userRepository;

    @Transactional
    public void execute(Long id) {
        User user = userRepository.findByIdAndEnabledTrue(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.deactivate();
    }
}
