package com.vorynt.vorynt_api.handlers;

import com.vorynt.vorynt_api.domain.exceptions.*;
import com.vorynt.vorynt_api.dtos.error.ApiErrorResponse;
import com.vorynt.vorynt_api.dtos.error.FieldErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Helper methods

    private ResponseEntity<ApiErrorResponse> buildError(
            HttpStatus status,
            Exception ex
    ) {
        return buildError(status, ex.getMessage(), null);
    }

    private ResponseEntity<ApiErrorResponse> buildError(
            HttpStatus status,
            String message
    ) {
        return buildError(status, message, null);
    }

    private ResponseEntity<ApiErrorResponse> buildError(
            HttpStatus status,
            Exception ex,
            List<FieldErrorResponse> errors
    ) {
        return buildError(status, ex.getMessage(), errors);
    }

    private ResponseEntity<ApiErrorResponse> buildError(
            HttpStatus status,
            String message,
            List<FieldErrorResponse> errors
    ) {
        ApiErrorResponse error = new ApiErrorResponse(
                status.value(),
                status.name(),
                message,
                LocalDateTime.now(),
                errors
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    // Domain exceptions

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> userNotFoundException(
            UserNotFoundException ex
    ) {
        return buildError(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> emailAlreadyExistsException(
            EmailAlreadyExistsException ex
    ) {
        return buildError(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ApiErrorResponse> invalidEmailException(
            InvalidEmailException ex
    ) {
        return buildError(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(RequiredFieldException.class)
    public ResponseEntity<ApiErrorResponse> requiredFieldException(
            RequiredFieldException ex
    ) {
        return buildError(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> domainException(
            DomainException ex
    ) {
        return buildError(HttpStatus.BAD_REQUEST, ex);
    }

    // Validation exceptions

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> notValidException(
            MethodArgumentNotValidException ex
    ) {
        List<FieldErrorResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .distinct()
                .toList();

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Validation failed.",
                errors
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> typeMismatch(
            MethodArgumentTypeMismatchException ex
    ) {
        Class<?> requiredType = ex.getRequiredType();

        String expectedType = requiredType != null
                ? requiredType.getSimpleName()
                : "valid type";

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Parameter '" + ex.getName() +
                        "' must be a " + expectedType + "."
        );
    }

    // HTTP exceptions

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> notReadableException(
            HttpMessageNotReadableException ex
    ) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Malformed JSON request."
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> methodNotSupportedException(
            HttpRequestMethodNotSupportedException ex
    ) {
        return buildError(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Request method '" + ex.getMethod() + "' is not supported."
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> noHandlerFoundException(
            NoHandlerFoundException ex
    ) {
        return buildError(
                HttpStatus.NOT_FOUND,
                "No endpoint " + ex.getHttpMethod() +
                        " " + ex.getRequestURL() + "."
        );
    }

    // Fallback exception

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> unexpectedException(
            Exception ex
    ) {
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred."
        );
    }
}