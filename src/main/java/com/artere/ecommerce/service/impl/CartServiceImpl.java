package com.artere.ecommerce.service.impl;

import com.artere.ecommerce.dto.CartDTO;
import com.artere.ecommerce.exception.ResourceNotFoundException;
import com.artere.ecommerce.mapper.CartMapper;
import com.artere.ecommerce.model.Cart;
import com.artere.ecommerce.model.CartItem;
import com.artere.ecommerce.model.Product;
import com.artere.ecommerce.repository.CartItemRepository;
import com.artere.ecommerce.repository.CartRepository;
import com.artere.ecommerce.repository.ProductRepository;
import com.artere.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
                           ProductRepository productRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public Optional<CartDTO> getCartById(Long id) {
        return cartRepository.findByIdWithItems(id)
                .map(cartMapper::toDTO);
    }

    @Override
    public Optional<CartDTO> getCartByUserId(String userId) {
        return cartRepository.findByUserIdWithItems(userId)
                .map(cartMapper::toDTO);
    }

    @Override
    @Transactional
    public CartDTO createCart(String userId) {

        Optional<Cart> existingCart = cartRepository.findByUserId(userId);
        if (existingCart.isPresent()) {
            return cartMapper.toDTO(existingCart.get());
        }

        Cart cart = new Cart(userId);
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toDTO(savedCart);
    }

    @Override
    @Transactional
    public CartDTO addProductToCart(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findByIdWithItems(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductIdWithProduct(cartId, productId);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(cart, product, quantity);
            cart.addItem(newItem);
            cartItemRepository.save(newItem);
        }

        return cartMapper.toDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantity(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findByIdWithItems(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        CartItem item = cartItemRepository.findByCartIdAndProductIdWithProduct(cartId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "productId", productId));

        if (quantity <= 0) {
            cart.removeItem(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        return cartMapper.toDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO removeProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findByIdWithItems(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        CartItem item = cartItemRepository.findByCartIdAndProductIdWithProduct(cartId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "productId", productId));

        cart.removeItem(item);
        cartItemRepository.delete(item);

        return cartMapper.toDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO clearCart(Long cartId) {
        Cart cart = cartRepository.findByIdWithItems(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        cart.getItems().clear();
        cartItemRepository.deleteByCartId(cartId);

        return cartMapper.toDTO(cart);
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId) {
        // No need to fetch the cart with items since we're just deleting it
        cartItemRepository.deleteByCartId(cartId);
        cartRepository.deleteById(cartId);
    }
}
