package com.example.demo.cart;

import java.util.List;

public record CartDto (Long id, List<CartItem> cartItems){
}
