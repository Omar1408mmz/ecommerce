package com.artere.ecommerce.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * Data Transfer Object for Category entity
 */
public class CategoryDTO {
    
    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private Set<Long> subcategoryIds = new HashSet<>();
    private Set<Long> productIds = new HashSet<>();
    
    // Constructors
    public CategoryDTO() {
    }
    
    public CategoryDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public Set<Long> getSubcategoryIds() {
        return subcategoryIds;
    }
    
    public void setSubcategoryIds(Set<Long> subcategoryIds) {
        this.subcategoryIds = subcategoryIds;
    }
    
    public Set<Long> getProductIds() {
        return productIds;
    }
    
    public void setProductIds(Set<Long> productIds) {
        this.productIds = productIds;
    }
}