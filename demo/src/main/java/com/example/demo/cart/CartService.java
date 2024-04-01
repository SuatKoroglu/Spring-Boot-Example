package com.example.demo.cart;

import com.example.demo.product.Product;
import com.example.demo.product.ProductRepository;
import com.example.demo.user.LocalUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final ICartMapper cartMapper;


    private final ICartItemMapper cartItemMapper;

    private final ProductRepository productRepository;


    public CartDto getCart(Long id) {

        return cartRepository.findById(id)
                .map(cartMapper::fromCart)
                .orElseGet(() -> new CartDto(id, null,null,null));
    }

    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Cart not found with id: " + id));
    }



    public CartDto addProductToCart(Long id ,CartItemDto cartItemDto) {
        CartItem cartItem = cartItemMapper.toCartItem(cartItemDto);


        Cart newCart = cartRepository.findById(id)
                .orElse(new Cart(id));

        CartItem newCartItem = newCart.getCartItemByProductOrCreate(cartItem.getProduct());

        newCartItem
                .setQuantity(cartItem.getQuantity() + newCartItem.getQuantity());

        cartRepository.save(newCart);
        return cartMapper.fromCart(newCart);
    }

    public Cart removeProductFromCart(Long cartId, Long productId) {
        CartDto cartDto = getCart(cartId);
        Cart cart= cartMapper.toCart(cartDto);
        Product product = productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException("Product not found with id: " + productId));
        CartItem cartItem = cart.getCartItemByProduct(product);

        Optional.ofNullable(cartItem)
                .ifPresent(item -> {
                    cart.removeCartItem(item);
                    cartRepository.save(cart);
                });

        return cart;
    }

    public List<CartDto> getCarts(LocalUser user) {
            List<Cart> carts=  cartRepository.findByUser(user);
            return carts.stream()
                    .map(cartMapper::fromCart)
                    .toList();
    }

    public void deleteCart(Long id) {

        Cart cart = getCartById(id);
        cartRepository.delete(cart);
    }
}
