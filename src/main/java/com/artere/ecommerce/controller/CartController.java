package com.artere.ecommerce.controller;

import com.artere.ecommerce.dto.CartDTO;
import com.artere.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@Tag(name = "Shopping Cart Management", description = "APIs for managing shopping carts in the e-commerce system")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Get cart by ID", description = "Retrieves a specific cart by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the cart",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartById(
            @Parameter(description = "ID of the cart to retrieve", required = true) @PathVariable Long id) {
        return cartService.getCartById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get cart by user ID", description = "Retrieves a cart for a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the cart",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cart not found for this user", content = @Content)
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDTO> getCartByUserId(
            @Parameter(description = "ID of the user", required = true) @PathVariable String userId) {
        return cartService.getCartByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new cart", description = "Creates a new cart for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created a new cart",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartDTO.class)))
    })
    @PostMapping("/user/{userId}")
    public ResponseEntity<CartDTO> createCart(
            @Parameter(description = "ID of the user", required = true) @PathVariable String userId) {
        CartDTO createdCart = cartService.createCart(userId);
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }

    @Operation(summary = "Add product to cart", description = "Adds a product to a cart with the specified quantity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully added product to cart",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cart or product not found", content = @Content)
    })
    @PostMapping("/{cartId}/products/{productId}")
    public ResponseEntity<CartDTO> addProductToCart(
            @Parameter(description = "ID of the cart", required = true) @PathVariable Long cartId,
            @Parameter(description = "ID of the product to add", required = true) @PathVariable Long productId,
            @Parameter(description = "Quantity to add", required = true) @RequestParam Integer quantity) {
        try {
            CartDTO updatedCart = cartService.addProductToCart(cartId, productId, quantity);
            return ResponseEntity.ok(updatedCart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update product quantity", description = "Updates the quantity of a product in a cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated product quantity",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cart or product not found", content = @Content)
    })
    @PutMapping("/{cartId}/products/{productId}")
    public ResponseEntity<CartDTO> updateProductQuantity(
            @Parameter(description = "ID of the cart", required = true) @PathVariable Long cartId,
            @Parameter(description = "ID of the product", required = true) @PathVariable Long productId,
            @Parameter(description = "New quantity", required = true) @RequestParam Integer quantity) {
        try {
            CartDTO updatedCart = cartService.updateProductQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(updatedCart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Remove product from cart", description = "Removes a product from a cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully removed product from cart",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cart or product not found", content = @Content)
    })
    @DeleteMapping("/{cartId}/products/{productId}")
    public ResponseEntity<CartDTO> removeProductFromCart(
            @Parameter(description = "ID of the cart", required = true) @PathVariable Long cartId,
            @Parameter(description = "ID of the product to remove", required = true) @PathVariable Long productId) {
        try {
            CartDTO updatedCart = cartService.removeProductFromCart(cartId, productId);
            return ResponseEntity.ok(updatedCart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Clear cart", description = "Removes all products from a cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully cleared the cart",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content)
    })
    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<CartDTO> clearCart(
            @Parameter(description = "ID of the cart", required = true) @PathVariable Long cartId) {
        try {
            CartDTO updatedCart = cartService.clearCart(cartId);
            return ResponseEntity.ok(updatedCart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete cart", description = "Deletes a cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the cart", content = @Content),
        @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content)
    })
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(
            @Parameter(description = "ID of the cart to delete", required = true) @PathVariable Long cartId) {
        try {
            cartService.deleteCart(cartId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}