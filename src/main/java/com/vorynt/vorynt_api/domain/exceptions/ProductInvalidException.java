package com.vorynt.vorynt_api.domain.exceptions;

public class ProductInvalidException extends DomainException {
    public ProductInvalidException() {
        super("Product does not be null.");
    }
}
