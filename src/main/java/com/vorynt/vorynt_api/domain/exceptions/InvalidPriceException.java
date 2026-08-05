package com.vorynt.vorynt_api.domain.exceptions;

public class InvalidPriceException extends DomainException {
    public InvalidPriceException() {
        super("the price must be greater than zero.");
    }
}
