package com.example.demo.product;


import org.mapstruct.Mapper;

@Mapper(componentModel= "spring")
public interface IProductMapper {
    Product toProduct(ProductDto productDto);

    ProductDto fromProduct(Product product);



}
