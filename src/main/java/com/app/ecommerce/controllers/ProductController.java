package com.app.ecommerce.controllers;

import com.app.ecommerce.entity.ImageModel;
import com.app.ecommerce.entity.Product;
import com.app.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')"))
    @PostMapping(value = "/addNewProduct", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Product> addNewProduct(@RequestPart("product") Product product, @RequestPart("imageFile") MultipartFile[] file) {
        try {
            Set<ImageModel> imageModelSet = uploadImage(file);
            product.setProductImage(imageModelSet);
            return new ResponseEntity<>(productService.addNewProduct(product), HttpStatus.CREATED);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public Set<ImageModel> uploadImage(MultipartFile[] files) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();
        for (MultipartFile file : files) {
            ImageModel imageModel = new ImageModel(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            imageModels.add(imageModel);
        }
        return imageModels;
    }

    @PreAuthorize(("hasRole('ROLE_USER')"))
    @GetMapping("/getAllProduct")
    public ResponseEntity<List<Product>> getAllProduct(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "") String searchKey) {
        List<Product> allProduct = productService.getAllProduct(pageNumber, searchKey);
        return new ResponseEntity<>(allProduct, HttpStatus.OK);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')"))
    @DeleteMapping("/deleteProductDetails/{productId}")
    public void deleteProductDetails(@PathVariable("productId") Long productId) {
        productService.deleteProductDetails(productId);
    }

    @PreAuthorize(("hasRole('ROLE_USER')"))
    @GetMapping("/getProductById/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") Long productId) {
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    @PreAuthorize(("hasRole('ROLE_USER')"))
    @GetMapping("/getProductDetails/{isSingleProductCheckout}/{productId}")
    public ResponseEntity<List<Product>> getProductDetails(@PathVariable("isSingleProductCheckout") boolean isSingleProductCheckout, @PathVariable("productId") Long productId) {

        return new ResponseEntity<>(productService.getProductDetails(isSingleProductCheckout, productId), HttpStatus.OK);
    }

}
