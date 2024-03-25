package com.example.demo.cart;

import com.example.demo.product.Product;
import com.example.demo.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class CartService {

    private final CartRepository cartRepository;

    private final ICartMapper cartMapper;


    private final ICartItemMapper cartItemMapper;

    private final ProductRepository productRepository;
    @Lazy
    public CartService(CartRepository cartRepository, ICartMapper cartMapper, ICartItemMapper cartItemMapper, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
        this.productRepository = productRepository;
    }


    public CartDto getCart(Long id) {
//        var cart = cartRepository.findById(id);
//        if (cart.isPresent()){
//            return cartMapper.fromCart(cart.get());
//        }
//        return new CartDto(id, null);

        return cartRepository.findById(id)
                .map(cartMapper::fromCart)
                .orElseGet(() -> new CartDto(id, null));


    }



    public CartDto addProductToCart(Long id ,CartItemDto cartItemDto) {
        CartItem cartItem = cartItemMapper.toCartItem(cartItemDto);

//        Cart cart;
//        Optional<Cart> optionalCart = cartRepository.findById(id);
//        if (optionalCart.isPresent()){
//            cart = optionalCart.get();
//            if(cart.getCartItemByProduct(cartItem.getProduct()) == null) {
//                cart.addCartItem(cartItem);
//            }else {
//                cart.getCartItemByProduct(cartItem.getProduct()).setQuantity(cartItem.getQuantity()+ cart.getCartItemByProduct(cartItem.getProduct()).getQuantity());
//            }
//        }else {
//            cart = new Cart(id, Collections.singletonList(cartItem));
//        }
////        cartRepository.findById(id).ifPresentOrElse(c -> {
////            cart = c;
////        }, () -> {
////            cart = new Cart()
////        });

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
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException("Product not found with id: " + productId));
        CartItem cartItem = cart.getCartItemByProduct(product);
//        if (cartItem != null) {
//
//            cart.removeCartItem(cartItem);
//            cartRepository.save(cart);
//        }

        Optional.ofNullable(cartItem)
                .ifPresent(item -> {
                    cart.removeCartItem(item);
                    cartRepository.save(cart);
                });
        return cart;
    }

    public List<CartDto> getCarts() {
            List<Cart> carts= cartRepository.findAll();
            return carts.stream()
                    .map(cartMapper::fromCart)
                    .toList();

    }

    public void deleteCart(Long id) {
//        boolean exists = cartRepository.existsById(id);
//        if (!exists) {
//            throw new IllegalStateException("Cart id " + id + " does not exists");
//        }
//        cartRepository.deleteById(id);
        try {
            cartRepository.deleteById(id);
        } catch (Exception e) {
            throw new NoSuchElementException("Cart id " + id + " does not exist");
        }
    }
}
