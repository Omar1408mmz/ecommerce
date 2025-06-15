package com.artere.ecommerce.service.impl;

import com.artere.ecommerce.dto.CartDTO;
import com.artere.ecommerce.mapper.CartMapper;
import com.artere.ecommerce.model.Cart;
import com.artere.ecommerce.model.CartItem;
import com.artere.ecommerce.model.Product;
import com.artere.ecommerce.repository.CartItemRepository;
import com.artere.ecommerce.repository.CartRepository;
import com.artere.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private CartDTO cartDTO;
    private Product product;
    private CartItem cartItem;
    private String userId;
    private Long cartId;
    private Long productId;

    @BeforeEach
    void setUp() {
        // Initialize test data
        userId = "user123";
        cartId = 1L;
        productId = 1L;

        cart = new Cart(userId);
        cart.setId(cartId);

        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(99.99));
        product.setStockQuantity(10);

        cartItem = new CartItem(cart, product, 2);
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);
        cart.setItems(items);

        cartDTO = new CartDTO();
        // Set properties on cartDTO based on your actual DTO structure
    }

    @Test
    void getCartById_WhenCartExists_ShouldReturnCart() {
        // Arrange
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        // Act
        Optional<CartDTO> result = cartService.getCartById(cartId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(cartDTO, result.get());
        verify(cartRepository).findById(cartId);
        verify(cartMapper).toDTO(cart);
    }

    @Test
    void getCartById_WhenCartDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        // Act
        Optional<CartDTO> result = cartService.getCartById(cartId);

        // Assert
        assertFalse(result.isPresent());
        verify(cartRepository).findById(cartId);
        verify(cartMapper, never()).toDTO(any());
    }

    @Test
    void getCartByUserId_WhenCartExists_ShouldReturnCart() {
        // Arrange
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        // Act
        Optional<CartDTO> result = cartService.getCartByUserId(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(cartDTO, result.get());
        verify(cartRepository).findByUserId(userId);
        verify(cartMapper).toDTO(cart);
    }

    @Test
    void getCartByUserId_WhenCartDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act
        Optional<CartDTO> result = cartService.getCartByUserId(userId);

        // Assert
        assertFalse(result.isPresent());
        verify(cartRepository).findByUserId(userId);
        verify(cartMapper, never()).toDTO(any());
    }

    @Test
    void createCart_WhenCartDoesNotExist_ShouldCreateNewCart() {
        // Arrange
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        // Act
        CartDTO result = cartService.createCart(userId);

        // Assert
        assertEquals(cartDTO, result);
        verify(cartRepository).findByUserId(userId);
        verify(cartRepository).save(any(Cart.class));
        verify(cartMapper).toDTO(cart);
    }

    @Test
    void createCart_WhenCartExists_ShouldReturnExistingCart() {
        // Arrange
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        // Act
        CartDTO result = cartService.createCart(userId);

        // Assert
        assertEquals(cartDTO, result);
        verify(cartRepository).findByUserId(userId);
        verify(cartRepository, never()).save(any(Cart.class));
        verify(cartMapper).toDTO(cart);
    }

    @Test
    void addProductToCart_WhenProductNotInCart_ShouldAddNewItem() {
        // Arrange
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartIdAndProductId(cartId, productId)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        // Act
        CartDTO result = cartService.addProductToCart(cartId, productId, 1);

        // Assert
        assertEquals(cartDTO, result);
        verify(cartRepository).findById(cartId);
        verify(productRepository).findById(productId);
        verify(cartItemRepository).findByCartIdAndProductId(cartId, productId);
        verify(cartItemRepository).save(any(CartItem.class));
        verify(cartMapper).toDTO(cart);
    }

    @Test
    void addProductToCart_WhenProductInCart_ShouldUpdateQuantity() {
        // Arrange
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartIdAndProductId(cartId, productId)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        // Act
        CartDTO result = cartService.addProductToCart(cartId, productId, 1);

        // Assert
        assertEquals(cartDTO, result);
        verify(cartRepository).findById(cartId);
        verify(productRepository).findById(productId);
        verify(cartItemRepository).findByCartIdAndProductId(cartId, productId);
        verify(cartItemRepository).save(cartItem);
        verify(cartMapper).toDTO(cart);
    }

    @Test
    void updateProductQuantity_WhenQuantityPositive_ShouldUpdateQuantity() {
        // Arrange
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(cartId, productId)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        // Act
        CartDTO result = cartService.updateProductQuantity(cartId, productId, 5);

        // Assert
        assertEquals(cartDTO, result);
        verify(cartRepository).findById(cartId);
        verify(cartItemRepository).findByCartIdAndProductId(cartId, productId);
        verify(cartItemRepository).save(cartItem);
        verify(cartMapper).toDTO(cart);
    }

    @Test
    void updateProductQuantity_WhenQuantityZeroOrNegative_ShouldRemoveItem() {
        // Arrange
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(cartId, productId)).thenReturn(Optional.of(cartItem));
        doNothing().when(cartItemRepository).delete(cartItem);
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        // Act
        CartDTO result = cartService.updateProductQuantity(cartId, productId, 0);

        // Assert
        assertEquals(cartDTO, result);
        verify(cartRepository).findById(cartId);
        verify(cartItemRepository).findByCartIdAndProductId(cartId, productId);
        verify(cartItemRepository).delete(cartItem);
        verify(cartMapper).toDTO(cart);
    }

    @Test
    void removeProductFromCart_ShouldRemoveItem() {
        // Arrange
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(cartId, productId)).thenReturn(Optional.of(cartItem));
        doNothing().when(cartItemRepository).delete(cartItem);
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        // Act
        CartDTO result = cartService.removeProductFromCart(cartId, productId);

        // Assert
        assertEquals(cartDTO, result);
        verify(cartRepository).findById(cartId);
        verify(cartItemRepository).findByCartIdAndProductId(cartId, productId);
        verify(cartItemRepository).delete(cartItem);
        verify(cartMapper).toDTO(cart);
    }

    @Test
    void clearCart_ShouldClearAllItems() {
        // Arrange
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        doNothing().when(cartItemRepository).deleteByCartId(cartId);
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        // Act
        CartDTO result = cartService.clearCart(cartId);

        // Assert
        assertEquals(cartDTO, result);
        verify(cartRepository).findById(cartId);
        verify(cartItemRepository).deleteByCartId(cartId);
        verify(cartMapper).toDTO(cart);
    }

    @Test
    void deleteCart_ShouldDeleteCartAndItems() {
        // Arrange
        doNothing().when(cartItemRepository).deleteByCartId(cartId);
        doNothing().when(cartRepository).deleteById(cartId);

        // Act
        cartService.deleteCart(cartId);

        // Assert
        verify(cartItemRepository).deleteByCartId(cartId);
        verify(cartRepository).deleteById(cartId);
    }
}
