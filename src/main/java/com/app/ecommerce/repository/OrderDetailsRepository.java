package com.app.ecommerce.repository;

import com.app.ecommerce.entity.OrderDetails;
import com.app.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    public List<OrderDetails> findByUser(User user);

    public List<OrderDetails> findByStatus(String status);
}
