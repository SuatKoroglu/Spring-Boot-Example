package com.example.demo.cart;


import com.example.demo.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Cart {
    public Cart(Long id) {
        this.id = id;
    }

    @Id
    private Long id;

    //@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cartItem_id", referencedColumnName = "id")
    private List<CartItem> cartItems = new ArrayList<>();


//    public void addCartItem(Product product, Integer quantity) {
//        CartItem cartItem = new CartItem( product, quantity);
//        cartItems.add(cartItem);
//    }
//
//    public void addCartItem(CartItem cartItem) {
//        cartItems.add(cartItem);
//    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    public CartItem getCartItemByProduct(Product product) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .findFirst()
                .orElse(null);
    }

    public CartItem getCartItemByProductOrCreate(Product product) {
        Optional<CartItem> opt = cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .findFirst();

        if (opt.isEmpty()) {
            CartItem cartItem = new CartItem(product, 0);
            cartItems.add(cartItem);
            return cartItem;
        }

        return opt.get();
    }
}
