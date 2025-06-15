package com.artere.ecommerce.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

/**
 * Global exception handler for the application.
 * Provides centralized exception handling across all controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle ResourceNotFoundException. Triggered when a resource is not found.
     *
     * @param ex the exception
     * @param request the current request
     * @return a ResponseEntity with the appropriate status and body
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle ValidationException. Triggered when validation of input data fails.
     *
     * @param ex the exception
     * @param request the current request
     * @return a ResponseEntity with the appropriate status and body
     */
    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValidation(
            ValidationException ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Validation error");

        if (ex.getField() != null) {
            apiError.addSubError(new ApiError.ApiValidationError(
                    request.getDescription(false),
                    ex.getField(),
                    ex.getRejectedValue(),
                    ex.getMessage()
            ));
        } else {
            apiError.setMessage(ex.getMessage());
        }

        return buildResponseEntity(apiError);
    }

    /**
     * Handle IllegalArgumentException. Triggered when an illegal argument is passed to a method.
     *
     * @param ex the exception
     * @param request the current request
     * @return a ResponseEntity with the appropriate status and body
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle MethodArgumentTypeMismatchException. Triggered when a method argument is not the expected type.
     *
     * @param ex the exception
     * @param request the current request
     * @return a ResponseEntity with the appropriate status and body
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }


    /**
     * Handle HttpMessageNotReadableException. Triggered when the request JSON is malformed.
     *
     * @param ex the exception
     * @param request the current request
     * @return a ResponseEntity with the appropriate status and body
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Malformed JSON request");
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex the exception
     * @param request the current request
     * @return a ResponseEntity with the appropriate status and body
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.setDebugMessage(ex.getMessage());

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            apiError.addSubError(new ApiError.ApiValidationError(
                    fieldError.getObjectName(),
                    fieldError.getField(),
                    fieldError.getRejectedValue(),
                    fieldError.getDefaultMessage()
            ));
        });

        ex.getBindingResult().getGlobalErrors().forEach(objectError -> {
            apiError.addSubError(new ApiError.ApiValidationError(
                    objectError.getObjectName(),
                    null,
                    null,
                    objectError.getDefaultMessage()
            ));
        });

        return buildResponseEntity(apiError);
    }

    /**
     * Handle NoHandlerFoundException. Triggered when no handler is found for the request.
     *
     * @param ex the exception
     * @param request the current request
     * @return a ResponseEntity with the appropriate status and body
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle all other exceptions. Triggered by any exception not handled by more specific handlers.
     *
     * @param ex the exception
     * @param request the current request
     * @return a ResponseEntity with the appropriate status and body
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setMessage("An unexpected error occurred");
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Build a ResponseEntity with the given ApiError.
     *
     * @param apiError the ApiError to include in the response
     * @return a ResponseEntity with the appropriate status and body
     */
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
