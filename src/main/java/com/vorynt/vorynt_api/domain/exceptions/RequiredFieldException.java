package com.vorynt.vorynt_api.domain.exceptions;

public class RequiredFieldException extends DomainException {

    public RequiredFieldException(String fieldName) {
        super(fieldName + " cannot be blank.");
    }
}
