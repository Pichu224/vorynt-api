package com.vorynt.vorynt_api.domain.exceptions;

import com.vorynt.vorynt_api.domain.user.valueObjects.Email;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(Email email) {
        super("Email '" + email + "' is already registered.");
    }
}
