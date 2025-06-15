package com.artere.ecommerce.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object for CartItem entity
 */
public record CartItemDTO(
    Long id,
    Long cartId,
    Long productId,
    String productName,
    BigDecimal productPrice,
    Integer quantity,
    BigDecimal subtotal
) {
    /**
     * Default constructor
     */
    public CartItemDTO() {
        this(null, null, null, null, null, null, null);
    }
}
