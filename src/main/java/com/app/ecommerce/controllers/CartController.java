package com.app.ecommerce.controllers;

import com.app.ecommerce.entity.Cart;
import com.app.ecommerce.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize(("hasRole('ROLE_USER')"))
    @GetMapping({"/addToCart/{productId}"})
    public ResponseEntity<Cart> addToCart(@PathVariable(name = "productId") Long productId) {
        return new ResponseEntity<>(cartService.addToCart(productId), HttpStatus.CREATED);
    }

    @PreAuthorize(("hasRole('ROLE_USER')"))
    @GetMapping({"/getCartDetails"})
    public ResponseEntity<List<Cart>> getCartDetails() {
        return new ResponseEntity<>(cartService.getCartDetails(), HttpStatus.OK);
    }

    @PreAuthorize(("hasRole('ROLE_USER')"))
    @DeleteMapping("deleteCartItem/{cartItem}")
    public void deleteCartItem(@PathVariable("cartItem") Long cartId) {
        cartService.deleteCartItem(cartId);
    }
}
