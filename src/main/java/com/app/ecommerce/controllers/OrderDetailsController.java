package com.app.ecommerce.controllers;

import com.app.ecommerce.entity.OrderDetails;
import com.app.ecommerce.entity.OrderInput;
import com.app.ecommerce.entity.TransctionDetails;
import com.app.ecommerce.service.OrderDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderDetailsController {
    private final OrderDetailsService orderDetailsService;

    public OrderDetailsController(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    @PreAuthorize(("hasRole('ROLE_USER')"))
    @PostMapping("/placeOrder/{isSingleProductCheckout}")
    public void placeOrder(@RequestBody OrderInput orderInput, @PathVariable("isSingleProductCheckout") boolean isSingleProductCheckout) {
        orderDetailsService.placeOrder(orderInput, isSingleProductCheckout);
    }

    @PreAuthorize(("hasRole('ROLE_USER')"))
    @GetMapping("/getOrderDetails")
    public ResponseEntity<List<OrderDetails>> getOrderDetails() {
        return new ResponseEntity<>(orderDetailsService.getOrderDetails(), HttpStatus.OK);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')"))
    @GetMapping("/getAllOrderDetails/{status}")
    public ResponseEntity<List<OrderDetails>> getAllOrderDetails(@PathVariable String status) {
        return new ResponseEntity<>(orderDetailsService.getAllOrderDetails(status), HttpStatus.OK);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')"))
    @GetMapping("/markOrderAsDelivered/{id}")
    public void markOrderAsDelivered(@PathVariable Long id) {
        orderDetailsService.markOrderAsDelivered(id);
    }

    @PreAuthorize(("hasRole('ROLE_USER')"))
    @GetMapping("/createTransaction/{amount}")
    public ResponseEntity<TransctionDetails> createTransaction(@PathVariable("amount") Double amount) {
        return new ResponseEntity<>(orderDetailsService.createTransaction(amount), HttpStatus.OK);
    }
}

