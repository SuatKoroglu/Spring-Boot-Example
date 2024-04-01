package com.example.demo.cart;

import com.example.demo.product.ProductService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel= "spring", uses = {ProductService.class})

public interface ICartItemMapper {
    @Mapping(target = "product",source = "productId" )
    CartItem toCartItem(CartItemDto cartItemDto);

    CartItemDto fromCartItem(CartItem cartItem);

}
