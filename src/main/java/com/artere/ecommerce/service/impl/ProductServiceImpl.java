package com.artere.ecommerce.service.impl;

import com.artere.ecommerce.dto.PageDTO;
import com.artere.ecommerce.dto.ProductDTO;
import com.artere.ecommerce.exception.ResourceNotFoundException;
import com.artere.ecommerce.mapper.ProductMapper;
import com.artere.ecommerce.model.Category;
import com.artere.ecommerce.model.Product;
import com.artere.ecommerce.repository.CategoryRepository;
import com.artere.ecommerce.repository.ProductRepository;
import com.artere.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        List<Product> products = productRepository.findAllWithCategories();
        return productMapper.toDTOList(products);
    }

    @Override
    public PageDTO<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        Page<ProductDTO> dtoPage = productPage.map(productMapper::toDTO);
        return PageDTO.fromPage(dtoPage);
    }

    @Override
    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findByIdWithCategories(id)
                .map(productMapper::toDTO);
    }

    @Override
    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);

        if (productDTO.categoryIds() != null && !productDTO.categoryIds().isEmpty()) {
            for (Long categoryId : productDTO.categoryIds()) {
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
        List<Product> products = productRepository.findByNameContainingIgnoreCaseWithCategories(name);
        return productMapper.toDTOList(products);
    }

    @Override
    public PageDTO<ProductDTO> searchProductsByName(String name, Pageable pageable) {
        Page<Product> productPage = productRepository.findByNameContainingIgnoreCase(name, pageable);
        Page<ProductDTO> dtoPage = productPage.map(productMapper::toDTO);
        return PageDTO.fromPage(dtoPage);
    }

    @Override
    public List<ProductDTO> getProductsByPriceLessThanEqual(BigDecimal price) {
        List<Product> products = productRepository.findByPriceLessThanEqual(price);
        return productMapper.toDTOList(products);
    }

    @Override
    public PageDTO<ProductDTO> getProductsByPriceLessThanEqual(BigDecimal price, Pageable pageable) {
        Page<Product> productPage = productRepository.findByPriceLessThanEqual(price, pageable);
        Page<ProductDTO> dtoPage = productPage.map(productMapper::toDTO);
        return PageDTO.fromPage(dtoPage);
    }

    @Override
    public List<ProductDTO> getProductsByStockQuantityGreaterThanEqual(Integer stockQuantity) {
        List<Product> products = productRepository.findByStockQuantityGreaterThanEqual(stockQuantity);
        return productMapper.toDTOList(products);
    }

    @Override
    public PageDTO<ProductDTO> getProductsByStockQuantityGreaterThanEqual(Integer stockQuantity, Pageable pageable) {
        Page<Product> productPage = productRepository.findByStockQuantityGreaterThanEqual(stockQuantity, pageable);
        Page<ProductDTO> dtoPage = productPage.map(productMapper::toDTO);
        return PageDTO.fromPage(dtoPage);
    }

    @Override
    public List<ProductDTO> getProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return productMapper.toDTOList(products);
    }

    @Override
    public PageDTO<ProductDTO> getProductsByCategoryId(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);
        Page<ProductDTO> dtoPage = productPage.map(productMapper::toDTO);
        return PageDTO.fromPage(dtoPage);
    }

    @Override
    @Transactional
    public void addCategoryToProduct(Long productId, Long categoryId) {
        Product product = productRepository.findByIdWithCategories(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        Category category = categoryRepository.findByIdWithProducts(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        product.addCategory(category);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void removeCategoryFromProduct(Long productId, Long categoryId) {
        Product product = productRepository.findByIdWithCategories(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        Category category = categoryRepository.findByIdWithProducts(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        product.removeCategory(category);
        productRepository.save(product);
    }
}
