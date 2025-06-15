package com.artere.ecommerce.mapper;

import com.artere.ecommerce.dto.CartDTO;
import com.artere.ecommerce.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class to convert between Cart entity and CartDTO
 */
@Component
public class CartMapper {

    private final CartItemMapper cartItemMapper;

    @Autowired
    public CartMapper(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }

    /**
     * Convert a Cart entity to a CartDTO
     * @param cart the entity to convert
     * @return the corresponding DTO
     */
    public CartDTO toDTO(Cart cart) {
        if (cart == null) {
            return null;
        }

        // Create a new CartDTO using the constructor
        return new CartDTO(
            cart.getId(),
            cart.getUserId(),
            cartItemMapper.toDTOList(cart.getItems()),
            cart.getTotal()
        );
    }

    /**
     * Convert a list of Cart entities to a list of CartDTOs
     * @param carts the list of entities to convert
     * @return the list of corresponding DTOs
     */
    public List<CartDTO> toDTOList(List<Cart> carts) {
        if (carts == null) {
            return null;
        }

        return carts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert a CartDTO to a Cart entity
     * Note: This method does not set relationships (items)
     * Those should be handled separately in the service layer
     * @param dto the DTO to convert
     * @return the corresponding entity
     */
    public Cart toEntity(CartDTO dto) {
        if (dto == null) {
            return null;
        }

        Cart cart = new Cart();
        cart.setId(dto.id());
        cart.setUserId(dto.userId());

        return cart;
    }

    /**
     * Update an existing Cart entity with data from a CartDTO
     * Note: This method does not update relationships (items)
     * Those should be handled separately in the service layer
     * @param cart the entity to update
     * @param dto the DTO containing the new data
     * @return the updated entity
     */
    public Cart updateEntityFromDTO(Cart cart, CartDTO dto) {
        if (cart == null || dto == null) {
            return cart;
        }

        if (dto.userId() != null) {
            cart.setUserId(dto.userId());
        }

        return cart;
    }
}
