package com.vorynt.vorynt_api.services.user;

import com.vorynt.vorynt_api.domain.exceptions.UserNotFoundException;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import com.vorynt.vorynt_api.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetCurrentUserUseCase {

    private final UserRepository userRepository;

    public User execute() {
        Long userId = ((CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getId();

        return userRepository.findByIdAndEnabledTrue(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
