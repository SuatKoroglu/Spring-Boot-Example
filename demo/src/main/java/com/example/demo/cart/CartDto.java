package com.example.demo.cart;

import com.example.demo.user.Address;
import com.example.demo.user.LocalUser;

import java.util.List;

public record CartDto (Long id, List<CartItem> cartItems, LocalUser user, Address address){
}
