package com.artere.ecommerce.service;

import com.artere.ecommerce.dto.PageDTO;
import com.artere.ecommerce.dto.ProductDTO;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    PageDTO<ProductDTO> getAllProducts(Pageable pageable);

    Optional<ProductDTO> getProductById(Long id);

    ProductDTO saveProduct(ProductDTO productDTO);

    void deleteProduct(Long id);

    List<ProductDTO> searchProductsByName(String name);

    PageDTO<ProductDTO> searchProductsByName(String name, Pageable pageable);

    List<ProductDTO> getProductsByPriceLessThanEqual(BigDecimal price);

    PageDTO<ProductDTO> getProductsByPriceLessThanEqual(BigDecimal price, Pageable pageable);

    List<ProductDTO> getProductsByStockQuantityGreaterThanEqual(Integer stockQuantity);

    PageDTO<ProductDTO> getProductsByStockQuantityGreaterThanEqual(Integer stockQuantity, Pageable pageable);

    List<ProductDTO> getProductsByCategoryId(Long categoryId);

    PageDTO<ProductDTO> getProductsByCategoryId(Long categoryId, Pageable pageable);

    void addCategoryToProduct(Long productId, Long categoryId);

    void removeCategoryFromProduct(Long productId, Long categoryId);
}
