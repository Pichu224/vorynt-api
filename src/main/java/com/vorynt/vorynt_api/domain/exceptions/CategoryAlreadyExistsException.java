package com.vorynt.vorynt_api.domain.exceptions;

public class CategoryAlreadyExistsException extends DomainException {
    public CategoryAlreadyExistsException(String name) {
        super("Category with name " + name + " already exists.");
    }
}
