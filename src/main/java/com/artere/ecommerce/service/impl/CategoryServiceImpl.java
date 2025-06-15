package com.artere.ecommerce.service.impl;

import com.artere.ecommerce.dto.CategoryDTO;
import com.artere.ecommerce.dto.PageDTO;
import com.artere.ecommerce.exception.ResourceNotFoundException;
import com.artere.ecommerce.mapper.CategoryMapper;
import com.artere.ecommerce.model.Category;
import com.artere.ecommerce.model.Product;
import com.artere.ecommerce.repository.CategoryRepository;
import com.artere.ecommerce.repository.ProductRepository;
import com.artere.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public PageDTO<CategoryDTO> getAllCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        Page<CategoryDTO> dtoPage = categoryPage.map(categoryMapper::toDTO);
        return PageDTO.fromPage(dtoPage);
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
        if (categoryDTO.id() != null) {

            category = categoryRepository.findById(categoryDTO.id())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryDTO.id()));

            category = categoryMapper.updateEntityFromDTO(category, categoryDTO);
        } else {
            category = categoryMapper.toEntity(categoryDTO);
        }

        if (categoryDTO.parentId() != null) {
            categoryRepository.findById(categoryDTO.parentId())
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
    public PageDTO<CategoryDTO> getRootCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findByParentIsNull(pageable);
        Page<CategoryDTO> dtoPage = categoryPage.map(categoryMapper::toDTO);
        return PageDTO.fromPage(dtoPage);
    }

    @Override
    public List<CategoryDTO> getSubcategories(Long parentId) {
        List<Category> subcategories = categoryRepository.findByParentId(parentId);
        return categoryMapper.toDTOList(subcategories);
    }

    @Override
    public PageDTO<CategoryDTO> getSubcategories(Long parentId, Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findByParentId(parentId, pageable);
        Page<CategoryDTO> dtoPage = categoryPage.map(categoryMapper::toDTO);
        return PageDTO.fromPage(dtoPage);
    }

    @Override
    public List<CategoryDTO> searchCategoriesByName(String name) {
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(name);
        return categoryMapper.toDTOList(categories);
    }

    @Override
    public PageDTO<CategoryDTO> searchCategoriesByName(String name, Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findByNameContainingIgnoreCase(name, pageable);
        Page<CategoryDTO> dtoPage = categoryPage.map(categoryMapper::toDTO);
        return PageDTO.fromPage(dtoPage);
    }

    @Override
    @Transactional
    public void addProductToCategory(Long categoryId, Long productId) {
        Category category = categoryRepository.findByIdWithProducts(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        Product product = productRepository.findByIdWithCategories(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        category.addProduct(product);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void removeProductFromCategory(Long categoryId, Long productId) {
        Category category = categoryRepository.findByIdWithProducts(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        Product product = productRepository.findByIdWithCategories(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        category.removeProduct(product);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void addSubcategory(Long parentId, Long subcategoryId) {
        Category parent = categoryRepository.findByIdWithSubcategories(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", parentId));

        Category subcategory = categoryRepository.findById(subcategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", subcategoryId));

        parent.addSubcategory(subcategory);
        categoryRepository.save(parent);
    }

    @Override
    @Transactional
    public void removeSubcategory(Long parentId, Long subcategoryId) {
        Category parent = categoryRepository.findByIdWithSubcategories(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", parentId));

        Category subcategory = categoryRepository.findById(subcategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", subcategoryId));

        parent.removeSubcategory(subcategory);
        categoryRepository.save(parent);
    }
}
