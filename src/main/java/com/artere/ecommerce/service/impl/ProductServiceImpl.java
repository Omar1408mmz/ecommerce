package com.artere.ecommerce.service.impl;

import com.artere.ecommerce.dto.ProductDTO;
import com.artere.ecommerce.mapper.ProductMapper;
import com.artere.ecommerce.model.Category;
import com.artere.ecommerce.model.Product;
import com.artere.ecommerce.repository.CategoryRepository;
import com.artere.ecommerce.repository.ProductRepository;
import com.artere.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toDTOList(products);
    }

    @Override
    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO);
    }

    @Override
    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);

        // Handle category relationships if categoryIds are provided
        if (productDTO.getCategoryIds() != null && !productDTO.getCategoryIds().isEmpty()) {
            for (Long categoryId : productDTO.getCategoryIds()) {
                categoryRepository.findById(categoryId).ifPresent(product::addCategory);
            }
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> searchProductsByName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return productMapper.toDTOList(products);
    }

    @Override
    public List<ProductDTO> getProductsByPriceLessThanEqual(BigDecimal price) {
        return List.of();
    }

    @Override
    public List<ProductDTO> getProductsByStockQuantityGreaterThanEqual(Integer stockQuantity) {
        return List.of();
    }

    @Override
    public List<ProductDTO> getProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return productMapper.toDTOList(products);
    }

    @Override
    @Transactional
    public void addCategoryToProduct(Long productId, Long categoryId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        product.addCategory(category);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void removeCategoryFromProduct(Long productId, Long categoryId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        product.removeCategory(category);
        productRepository.save(product);
    }
}
