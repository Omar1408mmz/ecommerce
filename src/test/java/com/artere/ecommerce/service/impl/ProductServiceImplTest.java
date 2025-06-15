package com.artere.ecommerce.service.impl;

import com.artere.ecommerce.dto.ProductDTO;
import com.artere.ecommerce.exception.ResourceNotFoundException;
import com.artere.ecommerce.mapper.ProductMapper;
import com.artere.ecommerce.model.Category;
import com.artere.ecommerce.model.Product;
import com.artere.ecommerce.repository.CategoryRepository;
import com.artere.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        // Initialize test data
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(99.99));
        product.setStockQuantity(10);

        category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        Set<Long> categoryIds = new HashSet<>();
        categoryIds.add(1L);

        productDTO = new ProductDTO(
                1L,
                "Test Product",
                BigDecimal.valueOf(99.99),
                10,
                categoryIds
        );
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Arrange
        List<Product> products = Arrays.asList(product);
        List<ProductDTO> expectedDTOs = Arrays.asList(productDTO);

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDTOList(products)).thenReturn(expectedDTOs);

        // Act
        List<ProductDTO> result = productService.getAllProducts();

        // Assert
        assertEquals(expectedDTOs, result);
        verify(productRepository).findAll();
        verify(productMapper).toDTOList(products);
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(productDTO);

        // Act
        Optional<ProductDTO> result = productService.getProductById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(productDTO, result.get());
        verify(productRepository).findById(1L);
        verify(productMapper).toDTO(product);
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<ProductDTO> result = productService.getProductById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(productRepository).findById(1L);
        verify(productMapper, never()).toDTO(any());
    }

    @Test
    void saveProduct_WithoutCategories_ShouldSaveProduct() {

        ProductDTO inputDTO = new ProductDTO(
                null,
                "New Product",
                BigDecimal.valueOf(49.99),
                5
        );

        Product newProduct = new Product();
        newProduct.setName("New Product");

        Product savedProduct = new Product();
        savedProduct.setId(2L);
        savedProduct.setName("New Product");

        ProductDTO expectedDTO = new ProductDTO(
                2L,
                "New Product",
                BigDecimal.valueOf(49.99),
                5
        );

        when(productMapper.toEntity(inputDTO)).thenReturn(newProduct);
        when(productRepository.save(newProduct)).thenReturn(savedProduct);
        when(productMapper.toDTO(savedProduct)).thenReturn(expectedDTO);

        // Act
        ProductDTO result = productService.saveProduct(inputDTO);

        // Assert
        assertEquals(expectedDTO, result);
        verify(productMapper).toEntity(inputDTO);
        verify(productRepository).save(newProduct);
        verify(productMapper).toDTO(savedProduct);
        verify(categoryRepository, never()).findById(anyLong());
    }

    @Test
    void saveProduct_WithCategories_ShouldSaveProductWithCategories() {
        // Arrange
        when(productMapper.toEntity(productDTO)).thenReturn(product);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(productDTO);

        // Act
        ProductDTO result = productService.saveProduct(productDTO);

        // Assert
        assertEquals(productDTO, result);
        verify(productMapper).toEntity(productDTO);
        verify(categoryRepository).findById(1L);
        verify(productRepository).save(product);
        verify(productMapper).toDTO(product);
    }

    @Test
    void deleteProduct_ShouldDeleteProduct() {
        // Arrange
        Long productId = 1L;
        doNothing().when(productRepository).deleteById(productId);

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository).deleteById(productId);
    }

    @Test
    void searchProductsByName_ShouldReturnMatchingProducts() {
        // Arrange
        String searchName = "Test";
        List<Product> products = Arrays.asList(product);
        List<ProductDTO> expectedDTOs = Arrays.asList(productDTO);

        when(productRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(products);
        when(productMapper.toDTOList(products)).thenReturn(expectedDTOs);

        // Act
        List<ProductDTO> result = productService.searchProductsByName(searchName);

        // Assert
        assertEquals(expectedDTOs, result);
        verify(productRepository).findByNameContainingIgnoreCase(searchName);
        verify(productMapper).toDTOList(products);
    }

    @Test
    void getProductsByCategoryId_ShouldReturnProductsInCategory() {
        // Arrange
        Long categoryId = 1L;
        List<Product> products = Arrays.asList(product);
        List<ProductDTO> expectedDTOs = Arrays.asList(productDTO);

        when(productRepository.findByCategoryId(categoryId)).thenReturn(products);
        when(productMapper.toDTOList(products)).thenReturn(expectedDTOs);

        // Act
        List<ProductDTO> result = productService.getProductsByCategoryId(categoryId);

        // Assert
        assertEquals(expectedDTOs, result);
        verify(productRepository).findByCategoryId(categoryId);
        verify(productMapper).toDTOList(products);
    }

    @Test
    void addCategoryToProduct_WhenProductAndCategoryExist_ShouldAddCategory() {
        // Arrange
        Long productId = 1L;
        Long categoryId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.save(product)).thenReturn(product);

        // Act
        productService.addCategoryToProduct(productId, categoryId);

        // Assert
        verify(productRepository).findById(productId);
        verify(categoryRepository).findById(categoryId);
        verify(productRepository).save(product);
    }

    @Test
    void addCategoryToProduct_WhenProductDoesNotExist_ShouldThrowException() {
        // Arrange
        Long productId = 1L;
        Long categoryId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            productService.addCategoryToProduct(productId, categoryId)
        );

        verify(productRepository).findById(productId);
        verify(categoryRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any());
    }

    @Test
    void removeCategoryFromProduct_WhenProductAndCategoryExist_ShouldRemoveCategory() {
        // Arrange
        Long productId = 1L;
        Long categoryId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.save(product)).thenReturn(product);

        // Act
        productService.removeCategoryFromProduct(productId, categoryId);

        // Assert
        verify(productRepository).findById(productId);
        verify(categoryRepository).findById(categoryId);
        verify(productRepository).save(product);
    }

    @Test
    void removeCategoryFromProduct_WhenProductDoesNotExist_ShouldThrowException() {
        // Arrange
        Long productId = 1L;
        Long categoryId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            productService.removeCategoryFromProduct(productId, categoryId)
        );

        verify(productRepository).findById(productId);
        verify(categoryRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any());
    }
}
