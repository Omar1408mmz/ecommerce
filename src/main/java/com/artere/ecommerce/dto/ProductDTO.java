package com.artere.ecommerce.dto;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Transfer Object for Product entity
 */
public class ProductDTO {
    
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stockQuantity;
    private Set<Long> categoryIds = new HashSet<>();
    
    // Constructors
    public ProductDTO() {
    }
    
    public ProductDTO(Long id, String name, BigDecimal price, Integer stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public Set<Long> getCategoryIds() {
        return categoryIds;
    }
    
    public void setCategoryIds(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}