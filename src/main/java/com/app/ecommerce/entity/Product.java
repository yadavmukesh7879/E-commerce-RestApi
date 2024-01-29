package com.app.ecommerce.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Product {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String name;
    @Column(length = 2000)
    private String description;
    private Double discountedPrice;
    private Double actualPrice;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "product_images", joinColumns = {@JoinColumn(name = "product_id"),}, inverseJoinColumns = {@JoinColumn(name = "image_id")})
    private Set<ImageModel> productImage;

    public Set<ImageModel> getProductImage() {
        return productImage;
    }

    public void setProductImage(Set<ImageModel> productImage) {
        this.productImage = productImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }
}
