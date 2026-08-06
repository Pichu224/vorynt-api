package com.vorynt.vorynt_api.domain.exceptions;

public class CategoryHasProductsException extends DomainException {
    public CategoryHasProductsException(Long id) {
        super("Category with id " + id + " has products.");
    }
}
