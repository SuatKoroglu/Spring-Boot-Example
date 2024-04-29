package com.example.demo.product;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final IProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, IProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDto> getProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::fromProduct)
                .toList();
    }

    public ProductDto getProduct(Long id) {
        return productRepository.findById(id)
                .map(productMapper::fromProduct)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
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
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    @Transactional // *
    public ProductDto updateProduct(Long id, ProductDto givenProductDto) {
        Product givenProduct = productMapper.toProduct(givenProductDto);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("product with id " + id + " does not exist"));

        Optional.ofNullable(givenProduct.getName())
                .filter(name -> !name.isEmpty())
                .filter(name -> !Objects.equals(product.getName(), name))
                .ifPresent(product::setName);

        Optional.ofNullable(givenProduct.getBrand())
                .filter(brand -> !brand.isEmpty())
                .filter(brand -> !Objects.equals(product.getBrand(), brand))
                .ifPresent(product::setBrand);

        Optional.ofNullable(givenProduct.getPrice())
                .filter(price -> !Objects.equals(product.getPrice(), price))
                .ifPresent(product::setPrice);

        return productMapper.fromProduct(product);

        // bunları mapststruckt ile update etmeye de bi bak
        // mapstruckt içerisinde strateji belirleme (sadece null olanları değiş/ brand sıfır olmasın vesaire)
        // bi field'ı set'lerken ona özel bi strateji belirle
    }
}
