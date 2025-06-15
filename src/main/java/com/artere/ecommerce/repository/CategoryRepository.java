package com.artere.ecommerce.repository;

import com.artere.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Find all root categories (categories without a parent)
    List<Category> findByParentIsNull();
    
    // Find categories by name containing the given string (case-insensitive)
    List<Category> findByNameContainingIgnoreCase(String name);
    
    // Find subcategories of a given category
    List<Category> findByParentId(Long parentId);
}