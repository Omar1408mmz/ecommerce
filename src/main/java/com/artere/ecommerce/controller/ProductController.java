package com.artere.ecommerce.controller;

import com.artere.ecommerce.dto.PageDTO;
import com.artere.ecommerce.dto.ProductDTO;
import com.artere.ecommerce.exception.ResourceNotFoundException;
import com.artere.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "APIs for managing products in the e-commerce system")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all products", description = "Retrieves a paginated list of all products in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all products",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageDTO.class)))
    })
    @GetMapping
    public ResponseEntity<PageDTO<ProductDTO>> getAllProducts(
            @Parameter(description = "Page number (0-based)", schema = @Schema(type = "integer", defaultValue = "0"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", schema = @Schema(type = "integer", defaultValue = "10"))
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", schema = @Schema(type = "string", defaultValue = "id"))
            @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Sort direction (asc or desc)", schema = @Schema(type = "string", defaultValue = "asc"))
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @Operation(summary = "Get product by ID", description = "Retrieves a specific product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the product",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(description = "ID of the product to retrieve", required = true) @PathVariable Long id) {
        ProductDTO productDTO = productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return ResponseEntity.ok(productDTO);
    }

    @Operation(summary = "Get products by category", description = "Retrieves a paginated list of products belonging to a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products by category",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageDTO.class)))
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageDTO<ProductDTO>> getProductsByCategoryId(
            @Parameter(description = "ID of the category", required = true) @PathVariable Long categoryId,
            @Parameter(description = "Page number (0-based)", schema = @Schema(type = "integer", defaultValue = "0"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", schema = @Schema(type = "integer", defaultValue = "10"))
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", schema = @Schema(type = "string", defaultValue = "id"))
            @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Sort direction (asc or desc)", schema = @Schema(type = "string", defaultValue = "asc"))
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        return ResponseEntity.ok(productService.getProductsByCategoryId(categoryId, pageable));
    }

    @Operation(summary = "Create a new product", description = "Creates a new product in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class)))
    })
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @Parameter(description = "Product object to be created", required = true) @RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.saveProduct(productDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a product", description = "Updates an existing product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "ID of the product to update", required = true) @PathVariable Long id,
            @Parameter(description = "Updated product object", required = true) @RequestBody ProductDTO productDTO) {
        productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        ProductDTO updatedDTO = new ProductDTO(
                id,
                productDTO.name(),
                productDTO.price(),
                productDTO.stockQuantity(),
                productDTO.categoryIds()
        );
        return ResponseEntity.ok(productService.saveProduct(updatedDTO));
    }

    @Operation(summary = "Delete a product", description = "Deletes a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product successfully deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to delete", required = true) @PathVariable Long id) {
        productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add category to product", description = "Associates a category with a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category successfully added to product", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product or category not found", content = @Content)
    })
    @PostMapping("/{productId}/categories/{categoryId}")
    public ResponseEntity<Void> addCategoryToProduct(
            @Parameter(description = "ID of the product", required = true) @PathVariable Long productId,
            @Parameter(description = "ID of the category to add", required = true) @PathVariable Long categoryId) {
        productService.addCategoryToProduct(productId, categoryId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove category from product", description = "Removes the association between a category and a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category successfully removed from product", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product or category not found", content = @Content)
    })
    @DeleteMapping("/{productId}/categories/{categoryId}")
    public ResponseEntity<Void> removeCategoryFromProduct(
            @Parameter(description = "ID of the product", required = true) @PathVariable Long productId,
            @Parameter(description = "ID of the category to remove", required = true) @PathVariable Long categoryId) {
        productService.removeCategoryFromProduct(productId, categoryId);
        return ResponseEntity.ok().build();
    }
}
