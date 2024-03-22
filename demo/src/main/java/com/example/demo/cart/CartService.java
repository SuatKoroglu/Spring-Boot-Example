package com.example.demo.cart;

import com.example.demo.product.Product;
import com.example.demo.product.ProductRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service

public class CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;
    private final ICartMapper cartMapper;


    private final ICartItemMapper cartItemMapper;

    private final ProductRepository productRepository;
    @Lazy
    public CartService(CartItemRepository cartItemRepository, CartRepository cartRepository, ICartMapper cartMapper, ICartItemMapper cartItemMapper, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
        this.productRepository = productRepository;
    }


    public CartDto getCart(Long id) {
        var cart = cartRepository.findById(id);
        if (cart.isPresent()){
            return cartMapper.fromCart(cart.get());
        }
        return new CartDto(id, null);

    }


    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Cart not found with id: " + id));
    }


    public CartDto addProductToCart(Long id ,CartItemDto cartItemDto) {
        CartItem cartItem = null;
        try {
            cartItem= cartItemMapper.toCartItem(cartItemDto);
        }catch (Exception e) {
            System.err.println("Cart not found with id: " + id);
        }

        Cart cart;
        if (cartRepository.findById(id).isPresent()){
            cart = cartRepository.findById(id).get();
            if(cart.getCartItemByProduct(cartItem.getProduct()) == null) {
                cart.addCartItem(cartItem);
            }else {
                cart.getCartItemByProduct(cartItem.getProduct()).setQuantity(cartItem.getQuantity()+ cart.getCartItemByProduct(cartItem.getProduct()).getQuantity());
            }
        }else {
            cart = new Cart(id, Collections.singletonList(cartItem));
        }
        cartRepository.save(cart);
        return cartMapper.fromCart(cart);
    }

    public Cart removeProductFromCart(Long cartId, Long productId) {
        CartDto cartDto = getCart(cartId);
        Cart cart= cartMapper.toCart(cartDto);
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException("Product not found with id: " + productId));
        CartItem cartItem = cart.getCartItemByProduct(product);
        if (cartItem != null) {

            cartItemRepository.delete(cartItem);
            cart.removeCartItem(cartItem);
            cartRepository.save(cart);
        }
        return cart;
    }

    public List<CartDto> getCarts() {
            List<Cart> carts= cartRepository.findAll();
            return carts.stream()
                    .map(cartMapper::fromCart)
                    .toList();

    }

    public void deleteCart(Long id) {
        boolean exists = cartRepository.existsById(id);
        if (!exists){
            throw new IllegalStateException("Cart id " + id + " does not exists");
        }
        cartRepository.deleteById(id);
    }
}