package com.example.demo.cart;


import com.example.demo.user.LocalUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @GetMapping
    public List<CartDto> getCarts(@AuthenticationPrincipal LocalUser user) {
        return cartService.getCarts(user);
    }


    @PostMapping()
    public ResponseEntity<CartDto> addProductToCart(@AuthenticationPrincipal LocalUser user, @RequestBody() CartItemDto cartItemDto) {
        return ResponseEntity.ok(cartService.addProductToCart(user.getId(), cartItemDto));
    }


    @DeleteMapping()
    public ResponseEntity<CartDto> removeProductFromCart(@AuthenticationPrincipal LocalUser user, @RequestParam("productId") Long productId) {
        return ResponseEntity.ok(cartService.removeProductFromCart(user.getId(), productId));
    }


    @DeleteMapping(path = "/delete")
    public void deleteCart(@AuthenticationPrincipal LocalUser user) {
        cartService.deleteCart(user.getId());
    }

}