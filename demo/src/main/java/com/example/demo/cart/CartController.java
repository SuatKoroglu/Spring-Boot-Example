package com.example.demo.cart;


import com.example.demo.user.LocalUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ICartMapper cartMapper;


    @GetMapping
    public List<CartDto> getCarts(@AuthenticationPrincipal LocalUser user) {
        return cartService.getCarts(user);
    }

    @GetMapping(path = "{id}")
    public CartDto getCart(@PathVariable("id") Long cartId) {
        return cartService.getCart(cartId);
    }

    @PostMapping(path = "{id}")
    public ResponseEntity<CartDto> addProductToCart(@PathVariable("id") Long cartId, @RequestBody() CartItemDto cartItemDto) {
        return ResponseEntity.ok(cartService.addProductToCart(cartId, cartItemDto));
    }


    @DeleteMapping(path = "{id}")
    public ResponseEntity<CartDto> removeProductFromCart(@PathVariable("id") Long cartID, @RequestParam("productId") Long productId) {
        return ResponseEntity.ok(cartMapper.fromCart(cartService.removeProductFromCart(cartID ,productId)));
    }


    @DeleteMapping(path = "/delete/{id}")
    public void deleteCart(@PathVariable("id") Long cartID) {
        cartService.deleteCart(cartID);
    }

}