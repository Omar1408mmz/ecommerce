package com.artere.ecommerce.mapper;

import com.artere.ecommerce.dto.CartItemDTO;
import com.artere.ecommerce.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class to convert between CartItem entity and CartItemDTO
 */
@Component
public class CartItemMapper {

    /**
     * Convert a CartItem entity to a CartItemDTO
     * @param cartItem the entity to convert
     * @return the corresponding DTO
     */
    public CartItemDTO toDTO(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        // Create a new CartItemDTO using the constructor
        return new CartItemDTO(
            cartItem.getId(),
            cartItem.getCart().getId(),
            cartItem.getProduct().getId(),
            cartItem.getProduct().getName(),
            cartItem.getProduct().getPrice(),
            cartItem.getQuantity(),
            cartItem.getSubtotal()
        );
    }

    /**
     * Convert a list of CartItem entities to a list of CartItemDTOs
     * @param cartItems the list of entities to convert
     * @return the list of corresponding DTOs
     */
    public List<CartItemDTO> toDTOList(List<CartItem> cartItems) {
        if (cartItems == null) {
            return null;
        }

        return cartItems.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert a CartItemDTO to a CartItem entity
     * Note: This method does not set relationships (cart, product)
     * Those should be handled separately in the service layer
     * @param dto the DTO to convert
     * @return the corresponding entity
     */
    public CartItem toEntity(CartItemDTO dto) {
        if (dto == null) {
            return null;
        }

        CartItem cartItem = new CartItem();
        cartItem.setId(dto.id());
        cartItem.setQuantity(dto.quantity());

        return cartItem;
    }

    /**
     * Update an existing CartItem entity with data from a CartItemDTO
     * Note: This method does not update relationships (cart, product)
     * Those should be handled separately in the service layer
     * @param cartItem the entity to update
     * @param dto the DTO containing the new data
     * @return the updated entity
     */
    public CartItem updateEntityFromDTO(CartItem cartItem, CartItemDTO dto) {
        if (cartItem == null || dto == null) {
            return cartItem;
        }

        if (dto.quantity() != null) {
            cartItem.setQuantity(dto.quantity());
        }

        return cartItem;
    }
}
