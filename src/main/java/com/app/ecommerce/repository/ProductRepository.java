package com.app.ecommerce.repository;

import com.app.ecommerce.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String key1, String key2, Pageable pageable);
}
