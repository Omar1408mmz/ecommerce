package com.artere.ecommerce.controller;

import com.artere.ecommerce.dto.CategoryDTO;
import com.artere.ecommerce.dto.PageDTO;
import com.artere.ecommerce.exception.ResourceNotFoundException;
import com.artere.ecommerce.service.CategoryService;
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

    @Operation(summary = "Get all categories", description = "Retrieves a paginated list of all categories in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all categories",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageDTO.class)))
    })
    @GetMapping
    public ResponseEntity<PageDTO<CategoryDTO>> getAllCategories(
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

        return ResponseEntity.ok(categoryService.getAllCategories(pageable));
    }

    @Operation(summary = "Get root categories", description = "Retrieves a paginated list of all root categories (categories without a parent)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved root categories",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageDTO.class)))
    })
    @GetMapping("/root")
    public ResponseEntity<PageDTO<CategoryDTO>> getRootCategories(
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

        return ResponseEntity.ok(categoryService.getRootCategories(pageable));
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
        CategoryDTO categoryDTO = categoryService.getCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return ResponseEntity.ok(categoryDTO);
    }

    @Operation(summary = "Get subcategories", description = "Retrieves a paginated list of subcategories of a specific category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved subcategories",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageDTO.class)))
    })
    @GetMapping("/{id}/subcategories")
    public ResponseEntity<PageDTO<CategoryDTO>> getSubcategories(
            @Parameter(description = "ID of the parent category", required = true) @PathVariable Long id,
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

        return ResponseEntity.ok(categoryService.getSubcategories(id, pageable));
    }

    @Operation(summary = "Search categories by name", description = "Searches for categories containing the given name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved matching categories",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageDTO.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<PageDTO<CategoryDTO>> searchCategoriesByName(
            @Parameter(description = "Name to search for", required = true) @RequestParam String name,
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

        return ResponseEntity.ok(categoryService.searchCategoriesByName(name, pageable));
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
        // Check if category exists, will throw ResourceNotFoundException if not found
        categoryService.getCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        // Create a new CategoryDTO with the id from the path variable
        CategoryDTO updatedDTO = new CategoryDTO(
            id,
            categoryDTO.name(),
            categoryDTO.description(),
            categoryDTO.parentId(),
            categoryDTO.subcategoryIds(),
            categoryDTO.productIds()
        );
        return ResponseEntity.ok(categoryService.saveCategory(updatedDTO));
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
