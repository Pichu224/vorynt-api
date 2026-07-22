package com.vorynt.vorynt_api.dtos.error;

public record FieldErrorResponse(
        String field,
        String message
) {}
