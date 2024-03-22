package com.example.demo.cart;

import com.example.demo.product.Product;
import com.example.demo.product.ProductDto;
import org.mapstruct.Mapper;

@Mapper(componentModel= "spring")

public interface ICartMapper {

    Cart toCart(CartDto cartDto);

    CartDto fromCart(Cart cart);
}
