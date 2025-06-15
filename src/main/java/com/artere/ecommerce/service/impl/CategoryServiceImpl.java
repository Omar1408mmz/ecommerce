package com.artere.ecommerce.service.impl;

import com.artere.ecommerce.dto.CategoryDTO;
import com.artere.ecommerce.mapper.CategoryMapper;
import com.artere.ecommerce.model.Category;
import com.artere.ecommerce.model.Product;
import com.artere.ecommerce.repository.CategoryRepository;
import com.artere.ecommerce.repository.ProductRepository;
import com.artere.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDTOList(categories);
    }

    @Override
    public Optional<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDTO);
    }

    @Override
    @Transactional
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        Category category;
        if (categoryDTO.getId() != null) {

            category = categoryRepository.findById(categoryDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryDTO.getId()));

            category = categoryMapper.updateEntityFromDTO(category, categoryDTO);
        } else {
            category = categoryMapper.toEntity(categoryDTO);
        }

        if (categoryDTO.getParentId() != null) {
            categoryRepository.findById(categoryDTO.getParentId())
                    .ifPresent(category::setParent);
        } else {

            category.setParent(null);
        }

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDTO> getRootCategories() {
        List<Category> rootCategories = categoryRepository.findByParentIsNull();
        return categoryMapper.toDTOList(rootCategories);
    }

    @Override
    public List<CategoryDTO> getSubcategories(Long parentId) {
        List<Category> subcategories = categoryRepository.findByParentId(parentId);
        return categoryMapper.toDTOList(subcategories);
    }

    @Override
    public List<CategoryDTO> searchCategoriesByName(String name) {
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(name);
        return categoryMapper.toDTOList(categories);
    }

    @Override
    @Transactional
    public void addProductToCategory(Long categoryId, Long productId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        category.addProduct(product);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void removeProductFromCategory(Long categoryId, Long productId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        category.removeProduct(product);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void addSubcategory(Long parentId, Long subcategoryId) {
        Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent category not found with id: " + parentId));

        Category subcategory = categoryRepository.findById(subcategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Subcategory not found with id: " + subcategoryId));

        parent.addSubcategory(subcategory);
        categoryRepository.save(parent);
    }

    @Override
    @Transactional
    public void removeSubcategory(Long parentId, Long subcategoryId) {
        Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent category not found with id: " + parentId));

        Category subcategory = categoryRepository.findById(subcategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Subcategory not found with id: " + subcategoryId));

        parent.removeSubcategory(subcategory);
        categoryRepository.save(parent);
    }
}
