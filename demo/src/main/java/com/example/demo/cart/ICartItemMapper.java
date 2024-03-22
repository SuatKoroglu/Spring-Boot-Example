package com.example.demo.cart;

import com.example.demo.product.ProductService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel= "spring", uses = {ProductService.class})

public interface ICartItemMapper {
    // uses yöntemi veya kendin metot yaz ve id yi cart'a çevir
    @Mapping(target = "product",source = "productId" )
    CartItem toCartItem(CartItemDto cartItemDto);

    CartItemDto fromCartItem(CartItem cartItem);

}
