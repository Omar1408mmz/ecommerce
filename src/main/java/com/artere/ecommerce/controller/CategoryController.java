package com.artere.ecommerce.controller;

import com.artere.ecommerce.dto.CategoryDTO;
import com.artere.ecommerce.service.CategoryService;
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

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "APIs for managing categories in the e-commerce system")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all categories in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all categories",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Get root categories", description = "Retrieves a list of all root categories (categories without a parent)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved root categories",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class)))
    })
    @GetMapping("/root")
    public ResponseEntity<List<CategoryDTO>> getRootCategories() {
        return ResponseEntity.ok(categoryService.getRootCategories());
    }

    @Operation(summary = "Get category by ID", description = "Retrieves a specific category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the category",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(
            @Parameter(description = "ID of the category to retrieve", required = true) @PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get subcategories", description = "Retrieves all subcategories of a specific category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved subcategories",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class)))
    })
    @GetMapping("/{id}/subcategories")
    public ResponseEntity<List<CategoryDTO>> getSubcategories(
            @Parameter(description = "ID of the parent category", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getSubcategories(id));
    }

    @Operation(summary = "Search categories by name", description = "Searches for categories containing the given name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved matching categories",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<CategoryDTO>> searchCategoriesByName(
            @Parameter(description = "Name to search for", required = true) @RequestParam String name) {
        return ResponseEntity.ok(categoryService.searchCategoriesByName(name));
    }

    @Operation(summary = "Create a new category", description = "Creates a new category in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category successfully created",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class)))
    })
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            @Parameter(description = "Category object to be created", required = true) @RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(categoryService.saveCategory(categoryDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a category", description = "Updates an existing category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category successfully updated",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @Parameter(description = "ID of the category to update", required = true) @PathVariable Long id,
            @Parameter(description = "Updated category object", required = true) @RequestBody CategoryDTO categoryDTO) {
        return categoryService.getCategoryById(id)
                .map(existingCategory -> {
                    categoryDTO.setId(id);
                    return ResponseEntity.ok(categoryService.saveCategory(categoryDTO));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a category", description = "Deletes a category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Category successfully deleted", content = @Content),
        @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID of the category to delete", required = true) @PathVariable Long id) {
        if (categoryService.getCategoryById(id).isPresent()) {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Add product to category", description = "Associates a product with a category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product successfully added to category", content = @Content),
        @ApiResponse(responseCode = "404", description = "Category or product not found", content = @Content)
    })
    @PostMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<Void> addProductToCategory(
            @Parameter(description = "ID of the category", required = true) @PathVariable Long categoryId,
            @Parameter(description = "ID of the product to add", required = true) @PathVariable Long productId) {
        try {
            categoryService.addProductToCategory(categoryId, productId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Remove product from category", description = "Removes the association between a product and a category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product successfully removed from category", content = @Content),
        @ApiResponse(responseCode = "404", description = "Category or product not found", content = @Content)
    })
    @DeleteMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<Void> removeProductFromCategory(
            @Parameter(description = "ID of the category", required = true) @PathVariable Long categoryId,
            @Parameter(description = "ID of the product to remove", required = true) @PathVariable Long productId) {
        try {
            categoryService.removeProductFromCategory(categoryId, productId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Add subcategory", description = "Adds a subcategory to a parent category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subcategory successfully added", content = @Content),
        @ApiResponse(responseCode = "404", description = "Parent category or subcategory not found", content = @Content)
    })
    @PostMapping("/{parentId}/subcategories/{subcategoryId}")
    public ResponseEntity<Void> addSubcategory(
            @Parameter(description = "ID of the parent category", required = true) @PathVariable Long parentId,
            @Parameter(description = "ID of the subcategory to add", required = true) @PathVariable Long subcategoryId) {
        try {
            categoryService.addSubcategory(parentId, subcategoryId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Remove subcategory", description = "Removes a subcategory from a parent category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subcategory successfully removed", content = @Content),
        @ApiResponse(responseCode = "404", description = "Parent category or subcategory not found", content = @Content)
    })
    @DeleteMapping("/{parentId}/subcategories/{subcategoryId}")
    public ResponseEntity<Void> removeSubcategory(
            @Parameter(description = "ID of the parent category", required = true) @PathVariable Long parentId,
            @Parameter(description = "ID of the subcategory to remove", required = true) @PathVariable Long subcategoryId) {
        try {
            categoryService.removeSubcategory(parentId, subcategoryId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
