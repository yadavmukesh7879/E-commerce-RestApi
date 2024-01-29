package com.app.ecommerce.service;

import com.app.ecommerce.entity.Product;

import java.util.List;

public interface ProductService {
    public Product addNewProduct(Product product);

    List<Product> getAllProduct(int pageNumber, String searchKey);

    public void deleteProductDetails(Long productId);

    public Product getProductById(Long productId);

    public List<Product> getProductDetails(boolean isSingleProductCheckout, Long productId);
}
