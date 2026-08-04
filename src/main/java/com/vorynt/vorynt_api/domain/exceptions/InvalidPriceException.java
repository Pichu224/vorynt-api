package com.vorynt.vorynt_api.domain.exceptions;

public class InvalidPriceException extends RuntimeException {
    public InvalidPriceException() {
        super("the price must be greater than zero.");
    }
}
