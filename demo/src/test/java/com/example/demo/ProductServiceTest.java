package com.example.demo;


import com.example.demo.product.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService undertest;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private IProductMapper productMapper;

    @Test
    void testGetProducts() {
        // Set up mocks
        List<Product> expectedProducts = List.of(
                new Product("Product 1","Brand 1", 10.0),
                new Product(2L, "Product 2", "Brand 2", 20.0)
        );

        when(productRepository.findAll()).thenReturn(expectedProducts);

        // Execute the getProducts method
        List<ProductDto> actualProductDtos = undertest.getProducts();

        // Verify results
        assertEquals(2, actualProductDtos.size());

        assertEquals(productMapper.fromProduct(expectedProducts.get(0)), actualProductDtos.get(0));

        assertEquals(productMapper.fromProduct(expectedProducts.get(1)), actualProductDtos.get(1));
    }

    @Test
    void getProduct_whenProductExists_shouldReturnProductDto() {
        // Arrange
        Long productId = 1L;
        Product product = new Product(productId,"Product 1","Brand 1", 10.0);
        ProductDto productDto = new ProductDto(product.getName(),product.getBrand(),product.getPrice());

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.fromProduct(product)).thenReturn(productDto);


        // Act
        ProductDto result = undertest.getProduct(productId);

        // Assert
        assertEquals(productDto, result);
    }

    @Test
    void testGetProduct_nonexistentProduct() {
        // Set up mocks
        Long nonExistentId = 10L;

        when(productRepository.findById(nonExistentId)).thenReturn(java.util.Optional.empty());

        // Execute the getProduct method (expected to throw exception)
        assertThrows(NoSuchElementException.class, () -> undertest.getProduct(nonExistentId));
    }

    @Test
    void testAddNewProduct() {
        // Mocking the conversion from DTO to entity
        Product product = new Product(1L,"Product 1","Brand 1", 10.0);


        // Mocking the input DTO
        ProductDto inputDto = new ProductDto(product.getName(),product.getBrand(),product.getPrice());
        when(productMapper.toProduct(inputDto)).thenReturn(product);

        // Mocking the saved product
        Product savedProduct = new Product(1L,"Product 1","Brand 1", 10.0);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Mocking the conversion from entity to DTO
        ProductDto outputDto = new ProductDto(product.getName(),product.getBrand(),product.getPrice());
        when(productMapper.fromProduct(savedProduct)).thenReturn(outputDto);

        // Calling the method under test
        ProductDto result = undertest.addNewProduct(inputDto);


        // Asserting the result
        assertThat(result).isNotNull();
        assertEquals(result, outputDto);
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        Long productId = 1L;
        Product product = new Product(productId, "Product 1", "Brand 1", 10.0);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        undertest.deleteProduct(productId);

        // Assert
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testUpdateProduct() {
        // Arrange
        Long productId = 1L;
        Product product = new Product(productId, "Product 1", "Brand 1", 10.0);
        ProductDto givenProductDto = new ProductDto("Updated Product", "Updated Brand", 20.0);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Mocking the conversion from DTO to entity
        Product givenProduct = new Product(productId, "Updated Product", "Updated Brand", 20.0);
        when(productMapper.toProduct(givenProductDto)).thenReturn(givenProduct);

        // Act
        ProductDto result = undertest.updateProduct(productId, givenProductDto);

        // Assert
        verify(productRepository, times(1)).findById(productId);
        assertEquals("Updated Product", product.getName());
        assertEquals("Updated Brand", product.getBrand());
        assertEquals(20.0, product.getPrice());
    }




}
