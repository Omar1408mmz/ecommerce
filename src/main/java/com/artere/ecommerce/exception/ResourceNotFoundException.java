package com.artere.ecommerce.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    /**
     * Constructs a new ResourceNotFoundException with the specified resource details.
     *
     * @param resourceName the name of the resource that was not found
     * @param fieldName the name of the field used in the search
     * @param fieldValue the value of the field used in the search
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Gets the name of the resource that was not found.
     *
     * @return the resource name
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Gets the name of the field used in the search.
     *
     * @return the field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Gets the value of the field used in the search.
     *
     * @return the field value
     */
    public Object getFieldValue() {
        return fieldValue;
    }
}