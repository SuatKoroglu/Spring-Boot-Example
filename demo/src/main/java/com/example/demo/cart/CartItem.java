package com.example.demo.cart;


import com.example.demo.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    public CartItem( Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
