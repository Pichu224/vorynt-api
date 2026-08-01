package com.vorynt.vorynt_api.security;

import com.vorynt.vorynt_api.domain.user.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {

    public boolean canModifyUser(Long targetUserId) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails currentUser =
                (CustomUserDetails) auth.getPrincipal();

        return currentUser.getRole() == Role.ADMIN
                || currentUser.getId().equals(targetUserId);
    }
}
