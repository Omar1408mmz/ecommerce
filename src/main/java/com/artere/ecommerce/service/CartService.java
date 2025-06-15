package com.artere.ecommerce.service;

import com.artere.ecommerce.dto.CartDTO;
import com.artere.ecommerce.dto.CartItemDTO;

import java.util.Optional;

public interface CartService {

    /**
     * Get a cart by its ID
     * @param id the cart ID
     * @return the cart, if found
     */
    Optional<CartDTO> getCartById(Long id);

    /**
     * Get a cart by user ID
     * @param userId the user ID
     * @return the cart, if found
     */
    Optional<CartDTO> getCartByUserId(String userId);

    /**
     * Create a new cart for a user
     * @param userId the user ID
     * @return the created cart
     */
    CartDTO createCart(String userId);

    /**
     * Add a product to a cart
     * @param cartId the cart ID
     * @param productId the product ID
     * @param quantity the quantity to add
     * @return the updated cart
     */
    CartDTO addProductToCart(Long cartId, Long productId, Integer quantity);

    /**
     * Update the quantity of a product in a cart
     * @param cartId the cart ID
     * @param productId the product ID
     * @param quantity the new quantity
     * @return the updated cart
     */
    CartDTO updateProductQuantity(Long cartId, Long productId, Integer quantity);

    /**
     * Remove a product from a cart
     * @param cartId the cart ID
     * @param productId the product ID
     * @return the updated cart
     */
    CartDTO removeProductFromCart(Long cartId, Long productId);

    /**
     * Clear all items from a cart
     * @param cartId the cart ID
     * @return the updated cart
     */
    CartDTO clearCart(Long cartId);

    /**
     * Delete a cart
     * @param cartId the cart ID
     */
    void deleteCart(Long cartId);
}