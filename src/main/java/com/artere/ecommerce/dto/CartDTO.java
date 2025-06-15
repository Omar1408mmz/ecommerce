package com.artere.ecommerce.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Data Transfer Object for Cart entity
 */
public record CartDTO(
    Long id,
    String userId,
    List<CartItemDTO> items,
    BigDecimal total
) {
    /**
     * Canonical constructor that ensures items is never null
     */
    public CartDTO(Long id, String userId, List<CartItemDTO> items, BigDecimal total) {
        this.id = id;
        this.userId = userId;
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        this.total = total;
    }

    /**
     * Secondary constructor without items
     */
    public CartDTO(Long id, String userId, BigDecimal total) {
        this(id, userId, new ArrayList<>(), total);
    }

    /**
     * Default constructor
     */
    public CartDTO() {
        this(null, null, null, null);
    }

    /**
     * Override the accessor to ensure the returned list is unmodifiable
     */
    @Override
    public List<CartItemDTO> items() {
        return Collections.unmodifiableList(items);
    }
}
