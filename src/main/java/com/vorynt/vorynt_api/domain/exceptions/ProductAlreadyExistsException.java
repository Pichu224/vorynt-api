package com.vorynt.vorynt_api.domain.exceptions;

public class ProductAlreadyExistsException extends DomainException {
    public ProductAlreadyExistsException(String name) {
        super("Product with name " + name + " already exists.");
    }
}
