package com.artere.ecommerce.mapper;

import com.artere.ecommerce.dto.CategoryDTO;
import com.artere.ecommerce.model.Category;
import com.artere.ecommerce.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper class to convert between Category entity and CategoryDTO
 */
@Component
public class CategoryMapper {

    /**
     * Convert a Category entity to a CategoryDTO
     * @param category the entity to convert
     * @return the corresponding DTO
     */
    public CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }
        
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        
        // Set parent ID if parent exists
        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
        }
        
        // Set subcategory IDs
        if (category.getSubcategories() != null) {
            Set<Long> subcategoryIds = category.getSubcategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet());
            dto.setSubcategoryIds(subcategoryIds);
        }
        
        // Set product IDs
        if (category.getProducts() != null) {
            Set<Long> productIds = category.getProducts().stream()
                    .map(Product::getId)
                    .collect(Collectors.toSet());
            dto.setProductIds(productIds);
        }
        
        return dto;
    }
    
    /**
     * Convert a list of Category entities to a list of CategoryDTOs
     * @param categories the list of entities to convert
     * @return the list of corresponding DTOs
     */
    public List<CategoryDTO> toDTOList(List<Category> categories) {
        if (categories == null) {
            return null;
        }
        
        return categories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert a CategoryDTO to a Category entity
     * Note: This method does not set relationships (parent, subcategories, products)
     * Those should be handled separately in the service layer
     * @param dto the DTO to convert
     * @return the corresponding entity
     */
    public Category toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        
        return category;
    }
    
    /**
     * Update an existing Category entity with data from a CategoryDTO
     * Note: This method does not update relationships (parent, subcategories, products)
     * Those should be handled separately in the service layer
     * @param category the entity to update
     * @param dto the DTO containing the new data
     * @return the updated entity
     */
    public Category updateEntityFromDTO(Category category, CategoryDTO dto) {
        if (category == null || dto == null) {
            return category;
        }
        
        if (dto.getName() != null) {
            category.setName(dto.getName());
        }
        
        if (dto.getDescription() != null) {
            category.setDescription(dto.getDescription());
        }
        
        return category;
    }
}