package com.vorynt.vorynt_api.dtos.error;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp,
        List<FieldErrorResponse> errors
) {}
