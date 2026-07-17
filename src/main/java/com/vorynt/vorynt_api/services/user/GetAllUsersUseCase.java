package com.vorynt.vorynt_api.services.user;

import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.persistence.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class GetAllUsersUseCase {

    private final UserRepository userRepository;

    public List<User> execute() {
        return userRepository.findAll();
    }
}
