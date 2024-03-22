package com.example.demo.product;

import ch.qos.logback.core.model.ComponentModel;
import org.mapstruct.Mapper;

@Mapper(componentModel= "spring")
public interface IProductMapper {
    Product toProduct(ProductDto productDto);

    ProductDto fromProduct(Product product);



}
