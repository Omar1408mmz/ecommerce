package com.artere.ecommerce.service;

import com.artere.ecommerce.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    // CRUD operations
    List<CategoryDTO> getAllCategories();

    Optional<CategoryDTO> getCategoryById(Long id);

    CategoryDTO saveCategory(CategoryDTO categoryDTO);

    void deleteCategory(Long id);

    // Additional operations
    List<CategoryDTO> getRootCategories();

    List<CategoryDTO> getSubcategories(Long parentId);

    List<CategoryDTO> searchCategoriesByName(String name);

    // Category-Product relationship operations
    void addProductToCategory(Long categoryId, Long productId);

    void removeProductFromCategory(Long categoryId, Long productId);

    // Category-Subcategory relationship operations
    void addSubcategory(Long parentId, Long subcategoryId);

    void removeSubcategory(Long parentId, Long subcategoryId);
}
