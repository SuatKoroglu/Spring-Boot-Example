package com.example.demo.product;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;




@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final IProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, IProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDto> getProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::fromProduct)
                .toList();
    }

    public ProductDto getProduct(Long id) {

        boolean exists = productRepository.existsById(id);
        if (!exists){
            throw new IllegalStateException("Product id " + id + " does not exists");
        }
        return productMapper.fromProduct(productRepository.findById(id).get());

    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
    }

    public ProductDto addNewProduct(ProductDto productDto) {
        Product product = productMapper.toProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.fromProduct(savedProduct);
    }

    public void deleteProduct(Long id) {
        boolean exists = productRepository.existsById(id);
        if (!exists){
            throw new IllegalStateException("Product id " + id + " does not exists");
        }
        productRepository.deleteById(id);
    }

    @Transactional // *
    public ProductDto updateProduct(Long id, ProductDto givenProductDto) {
        Product givenProduct = productMapper.toProduct(givenProductDto);
        Product product = productRepository.findById(id).orElseThrow( () -> new IllegalStateException("product with id "+ " does not exists"));

        if (givenProduct.getName() != null && !givenProduct.getName().isEmpty() && !Objects.equals(product.getName(), givenProduct.getName())){
            product.setName(givenProduct.getName());
        }

        if (givenProduct.getBrand() != null && !givenProduct.getBrand().isEmpty() && !Objects.equals(product.getBrand(), givenProduct.getBrand())){
            product.setBrand(givenProduct.getBrand());
        }

        if ( Objects.nonNull(givenProduct.getPrice()) && !Objects.equals(product.getPrice(), givenProduct.getPrice())) {

            product.setPrice(givenProduct.getPrice());
        }
    return productMapper.fromProduct(product);
    }

}
