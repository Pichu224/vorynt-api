package com.vorynt.vorynt_api.domain.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Don't exist a category with id: " + id);
    }
}
