package com.artere.ecommerce.service;

import com.artere.ecommerce.dto.CategoryDTO;
import com.artere.ecommerce.dto.PageDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    // CRUD operations
    List<CategoryDTO> getAllCategories();

    PageDTO<CategoryDTO> getAllCategories(Pageable pageable);

    Optional<CategoryDTO> getCategoryById(Long id);

    CategoryDTO saveCategory(CategoryDTO categoryDTO);

    void deleteCategory(Long id);

    // Additional operations
    List<CategoryDTO> getRootCategories();

    PageDTO<CategoryDTO> getRootCategories(Pageable pageable);

    List<CategoryDTO> getSubcategories(Long parentId);

    PageDTO<CategoryDTO> getSubcategories(Long parentId, Pageable pageable);

    List<CategoryDTO> searchCategoriesByName(String name);

    PageDTO<CategoryDTO> searchCategoriesByName(String name, Pageable pageable);

    // Category-Product relationship operations
    void addProductToCategory(Long categoryId, Long productId);

    void removeProductFromCategory(Long categoryId, Long productId);

    // Category-Subcategory relationship operations
    void addSubcategory(Long parentId, Long subcategoryId);

    void removeSubcategory(Long parentId, Long subcategoryId);
}
