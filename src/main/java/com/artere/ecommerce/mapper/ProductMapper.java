package com.artere.ecommerce.mapper;

import com.artere.ecommerce.dto.ProductDTO;
import com.artere.ecommerce.model.Category;
import com.artere.ecommerce.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper class to convert between Product entity and ProductDTO
 */
@Component
public class ProductMapper {

    /**
     * Convert a Product entity to a ProductDTO
     * @param product the entity to convert
     * @return the corresponding DTO
     */
    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }
        
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        
        // Set category IDs
        if (product.getCategories() != null) {
            Set<Long> categoryIds = product.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet());
            dto.setCategoryIds(categoryIds);
        }
        
        return dto;
    }
    
    /**
     * Convert a list of Product entities to a list of ProductDTOs
     * @param products the list of entities to convert
     * @return the list of corresponding DTOs
     */
    public List<ProductDTO> toDTOList(List<Product> products) {
        if (products == null) {
            return null;
        }
        
        return products.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert a ProductDTO to a Product entity
     * Note: This method does not set relationships (categories)
     * Those should be handled separately in the service layer
     * @param dto the DTO to convert
     * @return the corresponding entity
     */
    public Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        
        return product;
    }
    
    /**
     * Update an existing Product entity with data from a ProductDTO
     * Note: This method does not update relationships (categories)
     * Those should be handled separately in the service layer
     * @param product the entity to update
     * @param dto the DTO containing the new data
     * @return the updated entity
     */
    public Product updateEntityFromDTO(Product product, ProductDTO dto) {
        if (product == null || dto == null) {
            return product;
        }
        
        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }
        
        if (dto.getStockQuantity() != null) {
            product.setStockQuantity(dto.getStockQuantity());
        }
        
        return product;
    }
}