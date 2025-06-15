package com.artere.ecommerce.repository;

import com.artere.ecommerce.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findByParentIsNull(Pageable pageable);
    List<Category> findByParentIsNull();

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Category> findByNameContainingIgnoreCase(String name);

    Page<Category> findByParentId(Long parentId, Pageable pageable);
    List<Category> findByParentId(Long parentId);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.subcategories WHERE c.id = :id")
    Optional<Category> findByIdWithSubcategories(@Param("id") Long id);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.products WHERE c.id = :id")
    Optional<Category> findByIdWithProducts(@Param("id") Long id);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.subcategories LEFT JOIN FETCH c.products WHERE c.id = :id")
    Optional<Category> findByIdWithSubcategoriesAndProducts(@Param("id") Long id);
}
