package com.example.demo.product;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;

import static java.lang.Double.NaN;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public void addNewProduct(Product product) {
        System.out.println(product);
    }

    public void deleteProduct(Long id) {
        boolean exists = productRepository.existsById(id);
        if (!exists){
            throw new IllegalStateException("Product id " + id + " does not exists");
        }
        productRepository.deleteById(id);
    }

    @Transactional // *
    public void updateProduct(Long id, String name, String brand, Double price) {
        Product product = productRepository.findById(id).orElseThrow( () -> new IllegalStateException("product with id "+ " does not exists"));

        if (name != null && !name.isEmpty() && !Objects.equals(product.getName(), name)){
            product.setName(name);
        }

        if (brand != null && !brand.isEmpty() && !Objects.equals(product.getBrand(), brand)){
            product.setBrand(brand);
        }

        if (price != null && price != product.getPrice()) {
            product.setPrice(price);
        }

    }

}
