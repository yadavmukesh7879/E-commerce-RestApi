package com.app.ecommerce.service.impl;

import com.app.ecommerce.entity.Cart;
import com.app.ecommerce.entity.Product;
import com.app.ecommerce.entity.User;
import com.app.ecommerce.repository.CartRepository;
import com.app.ecommerce.repository.ProductRepository;
import com.app.ecommerce.repository.UserRepository;
import com.app.ecommerce.security.JwtAuthenticationFilter;
import com.app.ecommerce.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public CartServiceImpl(ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart addToCart(Long productId) {
        Product productDetails = productRepository.findById(productId).get();
        String userName = JwtAuthenticationFilter.CURRENT_USER;
        User user = null;
        if (userName != null) {
            user = userRepository.findByUserName(userName).get();
        }

        List<Cart> cartList = cartRepository.findByUser(user);
        List<Cart> filteredList = cartList.stream().filter(cart -> cart.getProduct().getId() == productId).collect(Collectors.toList());
        if (filteredList.size() > 0) {
            return null;
        }
        if (user != null && productDetails != null) {
            Cart cart = new Cart(productDetails, user);
            return cartRepository.save(cart);
        }

        return null;
    }

    @Override
    public List<Cart> getCartDetails() {
        String userName = JwtAuthenticationFilter.CURRENT_USER;
        if (userName != null) {
            User user = userRepository.findById(userName).get();
            return cartRepository.findByUser(user);
        }
        return null;
    }

    @Override
    public void deleteCartItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
