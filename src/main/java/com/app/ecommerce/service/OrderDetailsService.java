package com.app.ecommerce.service;

import com.app.ecommerce.entity.OrderDetails;
import com.app.ecommerce.entity.OrderInput;
import com.app.ecommerce.entity.TransctionDetails;


import java.util.List;

public interface OrderDetailsService {
    public void placeOrder(OrderInput orderInput, boolean isSingleProductCheckout);

    public List<OrderDetails> getOrderDetails();

    public List<OrderDetails> getAllOrderDetails(String status);

    public void markOrderAsDelivered(Long id);

    public TransctionDetails createTransaction(Double amount);
}
