package com.artere.ecommerce.service;

import com.artere.ecommerce.dto.ProductDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    Optional<ProductDTO> getProductById(Long id);

    ProductDTO saveProduct(ProductDTO productDTO);

    void deleteProduct(Long id);

    List<ProductDTO> searchProductsByName(String name);

    List<ProductDTO> getProductsByPriceLessThanEqual(BigDecimal price);

    List<ProductDTO> getProductsByStockQuantityGreaterThanEqual(Integer stockQuantity);

    List<ProductDTO> getProductsByCategoryId(Long categoryId);

    void addCategoryToProduct(Long productId, Long categoryId);

    void removeCategoryFromProduct(Long productId, Long categoryId);
}
