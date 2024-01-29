package com.app.ecommerce.service.impl;

import com.app.ecommerce.entity.Cart;
import com.app.ecommerce.entity.Product;
import com.app.ecommerce.entity.User;
import com.app.ecommerce.repository.CartRepository;
import com.app.ecommerce.repository.ProductRepository;
import com.app.ecommerce.repository.UserRepository;
import com.app.ecommerce.security.JwtAuthenticationFilter;
import com.app.ecommerce.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository, CartRepository cartRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Product addNewProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProduct(int pageNumber, String searchKey) {
        PageRequest pageable = PageRequest.of(pageNumber, 7);
        if (searchKey.equals("")) {
            return productRepository.findAll(pageable).toList();
        } else {
            List<Product> listOfSerchProduct = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchKey, searchKey, pageable);
            return listOfSerchProduct;
        }
    }

    @Override
    public void deleteProductDetails(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).get();
    }

    @Override
    public List<Product> getProductDetails(boolean isSingleProductCheckout, Long productId) {

        if (isSingleProductCheckout && productId != 0) {
            //  We are going to buy a single product
            List<Product> productList = new ArrayList<>();
            Product product = productRepository.findById(productId).get();
            productList.add(product);
            return productList;
        } else {
            // Here we are going to buy all cart product
            String currentUser = JwtAuthenticationFilter.CURRENT_USER;
            User user = userRepository.findByUserName(currentUser).get();
            List<Cart> cartDetails = cartRepository.findByUser(user);
            return cartDetails.stream().map(x -> x.getProduct()).collect(Collectors.toList());
        }
    }
}
