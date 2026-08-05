package com.vorynt.vorynt_api.domain.exceptions;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(Long id) {
        super("Don't exist an product with id: " + id);
    }
}
