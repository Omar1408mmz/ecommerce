package com.artere.ecommerce.dto;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

/**
 * Data Transfer Object for Product entity
 */
public record ProductDTO(
    Long id,
    String name,
    BigDecimal price,
    Integer stockQuantity,
    Set<Long> categoryIds
) {
    /**
     * Canonical constructor that ensures categoryIds is never null
     */
    public ProductDTO(Long id, String name, BigDecimal price, Integer stockQuantity, Set<Long> categoryIds) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryIds = categoryIds == null ? new HashSet<>() : new HashSet<>(categoryIds);
    }

    /**
     * Constructor without categoryIds
     */
    public ProductDTO(Long id, String name, BigDecimal price, Integer stockQuantity) {
        this(id, name, price, stockQuantity, new HashSet<>());
    }

    /**
     * Default constructor
     */
    public ProductDTO() {
        this(null, null, null, null, new HashSet<>());
    }

    /**
     * Override the accessor to ensure the returned set is unmodifiable
     */
    @Override
    public Set<Long> categoryIds() {
        return Collections.unmodifiableSet(categoryIds);
    }
}
