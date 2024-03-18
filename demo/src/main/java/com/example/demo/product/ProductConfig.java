package com.example.demo.product;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProductConfig {

    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository){
        return args -> {
            Product asus = new Product("Laptop","Asus",384.50);
            Product rotring = new Product("Pencil","Rotring",3.50);
            Product lenovo= new Product("Laptop","Lanovo",410.50);
            productRepository.saveAll(List.of(asus,rotring,lenovo));
        };
    }



}
