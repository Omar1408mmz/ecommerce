package com.artere.ecommerce.exception;

/**
 * Exception thrown when validation of input data fails.
 */
public class ValidationException extends RuntimeException {

    private final String field;
    private final Object rejectedValue;

    /**
     * Constructs a new ValidationException with the specified message.
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
        this.field = null;
        this.rejectedValue = null;
    }

    /**
     * Constructs a new ValidationException with the specified field and rejected value.
     *
     * @param field the field that failed validation
     * @param rejectedValue the value that was rejected
     * @param message the detail message
     */
    public ValidationException(String field, Object rejectedValue, String message) {
        super(message);
        this.field = field;
        this.rejectedValue = rejectedValue;
    }

    /**
     * Gets the field that failed validation.
     *
     * @return the field name
     */
    public String getField() {
        return field;
    }

    /**
     * Gets the value that was rejected.
     *
     * @return the rejected value
     */
    public Object getRejectedValue() {
        return rejectedValue;
    }
}