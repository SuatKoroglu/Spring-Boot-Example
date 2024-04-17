package com.example.demo.cart;


import com.example.demo.product.Product;
import com.example.demo.user.Address;
import com.example.demo.user.LocalUser;
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



    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cartItem_id", referencedColumnName = "id")
    private List<CartItem> cartItems = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private LocalUser user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    public Cart(Long id, LocalUser user, Address address) {
        this.id = id;
        this.user = user;
        this.address = address;
    }

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
