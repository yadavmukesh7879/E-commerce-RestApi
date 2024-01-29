package com.app.ecommerce.service;

import com.app.ecommerce.entity.Cart;
import com.app.ecommerce.entity.User;

import java.util.List;

public interface CartService {
    public Cart addToCart(Long productId);

    public List<Cart> getCartDetails();

    public void deleteCartItem(Long cartId);
}
