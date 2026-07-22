package com.vorynt.vorynt_api.persistence.repositories;

import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndEnabledTrue(Long id);

    Optional<User> findByEmail(Email email);

    List<User> findAllByEnabledTrue();
}
