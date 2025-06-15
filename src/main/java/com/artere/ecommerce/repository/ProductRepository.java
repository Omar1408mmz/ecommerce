package com.artere.ecommerce.repository;

import com.artere.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Find products by name containing the given string (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Find products with price less than or equal to the given value
    List<Product> findByPriceLessThanEqual(BigDecimal price);
    
    // Find products with stock quantity greater than or equal to the given value
    List<Product> findByStockQuantityGreaterThanEqual(Integer stockQuantity);
    
    // Find products by category id
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);
}