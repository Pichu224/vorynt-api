package com.vorynt.vorynt_api.domain.exceptions;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(Long id) {
        super("Don't exist an User with id: " + id);
    }
}
