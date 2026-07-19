package com.vorynt.vorynt_api.handlers;

import com.vorynt.vorynt_api.domain.exceptions.*;
import com.vorynt.vorynt_api.dtos.error.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiErrorResponse> buildError(
            HttpStatus status,
            Exception ex
    ) {
        return buildError(status, ex.getMessage());
    }

    private ResponseEntity<ApiErrorResponse> buildError(
            HttpStatus status,
            String message
    ) {
        ApiErrorResponse error = new ApiErrorResponse(
                status.value(),
                status.name(),
                message,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
            Exception ex
    ) {
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred."
        );
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> handleDomainException(
            DomainException ex
    ) {
        return buildError(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex
    ) {
        return buildError(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidEmailException(
            InvalidEmailException ex
    ) {
        return buildError(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(RequiredFieldException.class)
    public ResponseEntity<ApiErrorResponse> handleRequiredFieldException(
        RequiredFieldException ex
    ) {
        return buildError(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex
    ) {
        return buildError(HttpStatus.NOT_FOUND, ex);
    }
}
