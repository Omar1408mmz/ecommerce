package com.artere.ecommerce.service.impl;

import com.artere.ecommerce.dto.CategoryDTO;
import com.artere.ecommerce.mapper.CategoryMapper;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private Category parentCategory;
    private Category subcategory;
    private CategoryDTO categoryDTO;
    private Product product;

    @BeforeEach
    void setUp() {
        // Initialize test data
        category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        parentCategory = new Category();
        parentCategory.setId(2L);
        parentCategory.setName("Parent Category");

        subcategory = new Category();
        subcategory.setId(3L);
        subcategory.setName("Subcategory");

        categoryDTO = new CategoryDTO();
        // Set properties on categoryDTO based on your actual DTO structure

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories() {
        // Arrange
        List<Category> categories = Arrays.asList(category, parentCategory);
        List<CategoryDTO> expectedDTOs = Arrays.asList(categoryDTO, categoryDTO);

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDTOList(categories)).thenReturn(expectedDTOs);

        // Act
        List<CategoryDTO> result = categoryService.getAllCategories();

        // Assert
        assertEquals(expectedDTOs, result);
        verify(categoryRepository).findAll();
        verify(categoryMapper).toDTOList(categories);
    }

    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnCategory() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        // Act
        Optional<CategoryDTO> result = categoryService.getCategoryById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(categoryDTO, result.get());
        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toDTO(category);
    }

    @Test
    void getCategoryById_WhenCategoryDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<CategoryDTO> result = categoryService.getCategoryById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(categoryRepository).findById(1L);
        verify(categoryMapper, never()).toDTO(any());
    }

    @Test
    void saveCategory_NewCategory_ShouldSaveCategory() {
        // Arrange
        CategoryDTO inputDTO = new CategoryDTO();
        // Set properties on inputDTO

        when(categoryMapper.toEntity(inputDTO)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        // Act
        CategoryDTO result = categoryService.saveCategory(inputDTO);

        // Assert
        assertEquals(categoryDTO, result);
        verify(categoryMapper).toEntity(inputDTO);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDTO(category);
    }

    @Test
    void saveCategory_ExistingCategory_ShouldUpdateCategory() {
        // Arrange
        CategoryDTO inputDTO = new CategoryDTO(1L, "Updated Category", "Updated Description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.updateEntityFromDTO(category, inputDTO)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        // Act
        CategoryDTO result = categoryService.saveCategory(inputDTO);

        // Assert
        assertEquals(categoryDTO, result);
        verify(categoryRepository).findById(anyLong());
        verify(categoryMapper).updateEntityFromDTO(any(), any());
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDTO(category);
    }

    @Test
    void deleteCategory_ShouldDeleteCategory() {
        // Arrange
        Long categoryId = 1L;
        doNothing().when(categoryRepository).deleteById(categoryId);

        // Act
        categoryService.deleteCategory(categoryId);

        // Assert
        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    void getRootCategories_ShouldReturnRootCategories() {
        // Arrange
        List<Category> rootCategories = Arrays.asList(category, parentCategory);
        List<CategoryDTO> expectedDTOs = Arrays.asList(categoryDTO, categoryDTO);

        when(categoryRepository.findByParentIsNull()).thenReturn(rootCategories);
        when(categoryMapper.toDTOList(rootCategories)).thenReturn(expectedDTOs);

        // Act
        List<CategoryDTO> result = categoryService.getRootCategories();

        // Assert
        assertEquals(expectedDTOs, result);
        verify(categoryRepository).findByParentIsNull();
        verify(categoryMapper).toDTOList(rootCategories);
    }

    @Test
    void getSubcategories_ShouldReturnSubcategories() {
        // Arrange
        Long parentId = 2L;
        List<Category> subcategories = Arrays.asList(subcategory);
        List<CategoryDTO> expectedDTOs = Arrays.asList(categoryDTO);

        when(categoryRepository.findByParentId(parentId)).thenReturn(subcategories);
        when(categoryMapper.toDTOList(subcategories)).thenReturn(expectedDTOs);

        // Act
        List<CategoryDTO> result = categoryService.getSubcategories(parentId);

        // Assert
        assertEquals(expectedDTOs, result);
        verify(categoryRepository).findByParentId(parentId);
        verify(categoryMapper).toDTOList(subcategories);
    }

    @Test
    void searchCategoriesByName_ShouldReturnMatchingCategories() {
        // Arrange
        String searchName = "Test";
        List<Category> categories = Arrays.asList(category);
        List<CategoryDTO> expectedDTOs = Arrays.asList(categoryDTO);

        when(categoryRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(categories);
        when(categoryMapper.toDTOList(categories)).thenReturn(expectedDTOs);

        // Act
        List<CategoryDTO> result = categoryService.searchCategoriesByName(searchName);

        // Assert
        assertEquals(expectedDTOs, result);
        verify(categoryRepository).findByNameContainingIgnoreCase(searchName);
        verify(categoryMapper).toDTOList(categories);
    }

    @Test
    void addProductToCategory_WhenCategoryAndProductExist_ShouldAddProduct() {
        // Arrange
        Long categoryId = 1L;
        Long productId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(categoryRepository.save(category)).thenReturn(category);

        // Act
        categoryService.addProductToCategory(categoryId, productId);

        // Assert
        verify(categoryRepository).findById(categoryId);
        verify(productRepository).findById(productId);
        verify(categoryRepository).save(category);
    }

    @Test
    void removeProductFromCategory_WhenCategoryAndProductExist_ShouldRemoveProduct() {
        // Arrange
        Long categoryId = 1L;
        Long productId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(categoryRepository.save(category)).thenReturn(category);

        // Act
        categoryService.removeProductFromCategory(categoryId, productId);

        // Assert
        verify(categoryRepository).findById(categoryId);
        verify(productRepository).findById(productId);
        verify(categoryRepository).save(category);
    }

    @Test
    void addSubcategory_WhenParentAndSubcategoryExist_ShouldAddSubcategory() {
        // Arrange
        Long parentId = 2L;
        Long subcategoryId = 3L;

        when(categoryRepository.findById(parentId)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findById(subcategoryId)).thenReturn(Optional.of(subcategory));
        when(categoryRepository.save(parentCategory)).thenReturn(parentCategory);

        // Act
        categoryService.addSubcategory(parentId, subcategoryId);

        // Assert
        verify(categoryRepository).findById(parentId);
        verify(categoryRepository).findById(subcategoryId);
        verify(categoryRepository).save(parentCategory);
    }

    @Test
    void removeSubcategory_WhenParentAndSubcategoryExist_ShouldRemoveSubcategory() {
        // Arrange
        Long parentId = 2L;
        Long subcategoryId = 3L;

        when(categoryRepository.findById(parentId)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findById(subcategoryId)).thenReturn(Optional.of(subcategory));
        when(categoryRepository.save(parentCategory)).thenReturn(parentCategory);

        // Act
        categoryService.removeSubcategory(parentId, subcategoryId);

        // Assert
        verify(categoryRepository).findById(parentId);
        verify(categoryRepository).findById(subcategoryId);
        verify(categoryRepository).save(parentCategory);
    }
}
