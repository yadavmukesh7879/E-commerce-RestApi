package com.app.ecommerce.service.impl;

import com.app.ecommerce.entity.*;
import com.app.ecommerce.repository.CartRepository;
import com.app.ecommerce.repository.OrderDetailsRepository;
import com.app.ecommerce.repository.ProductRepository;
import com.app.ecommerce.repository.UserRepository;
import com.app.ecommerce.security.JwtAuthenticationFilter;
import com.app.ecommerce.service.OrderDetailsService;
import com.app.ecommerce.service.ProductService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
    private static final String ORDER_PLACED = "Placed";
    private static final String CURRENCY = "INR";
    private static final String KEY = "rzp_test_UdWrs353Xnda89";
    private static final String SECRET_KEY = "fBWQcDLdIqyDXvbKdGYgJaFi";
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final CartRepository cartRepository;

    public OrderDetailsServiceImpl(ProductService productService, ProductRepository productRepository, UserRepository userRepository, OrderDetailsRepository orderDetailsRepository, CartRepository cartRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public void placeOrder(OrderInput orderInput, boolean isSingleProductCheckout) {
        List<ProductQuantity> orderProductQuantityList = orderInput.getProductQuantityList();
        for (ProductQuantity o : orderProductQuantityList) {
            Product product = productRepository.findById(o.getProductId()).get();
            String currentUser = JwtAuthenticationFilter.CURRENT_USER;
            User user = userRepository.findByUserName(currentUser).get();
            OrderDetails orderDetails = new OrderDetails(orderInput.getFullName(), orderInput.getFullAddress(), orderInput.getContactNumber(), orderInput.getAlternateContact(), ORDER_PLACED, product.getActualPrice() * o.getQuantity(), product, user, orderInput.getTransactionId());
            orderDetailsRepository.save(orderDetails);
        }
        if (!isSingleProductCheckout) {
            String currentUser = JwtAuthenticationFilter.CURRENT_USER;
            User user = userRepository.findByUserName(currentUser).get();
            List<Cart> cartList = cartRepository.findByUser(user);
            for (Cart cart : cartList) {
                cartRepository.delete(cart);
            }
        }

    }

    @Override
    public List<OrderDetails> getOrderDetails() {
        String currentUser = JwtAuthenticationFilter.CURRENT_USER;
        User user = userRepository.findByUserName(currentUser).get();
        return orderDetailsRepository.findByUser(user);
    }

    @Override
    public List<OrderDetails> getAllOrderDetails(String status) {
        List<OrderDetails> orderDetails = new ArrayList<>();
        if (status.equals("All")) {
            orderDetailsRepository.findAll().forEach(x -> orderDetails.add(x));
            return orderDetails;
        } else {
            orderDetailsRepository.findByStatus(status).forEach(x -> orderDetails.add(x));
            return orderDetails;
        }
    }

    @Override
    public void markOrderAsDelivered(Long id) {
        OrderDetails orderDetails = orderDetailsRepository.findById(id).get();
        orderDetails.setStatus("Delivered");
        orderDetailsRepository.save(orderDetails);
    }

    @Override
    public TransctionDetails createTransaction(Double amount) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount", (amount * 100));
            jsonObject.put("currency", CURRENCY);
            RazorpayClient razorpayClient = new RazorpayClient(KEY, SECRET_KEY);
            Order order = razorpayClient.orders.create(jsonObject);
            return transactionalDetails(order);
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }
    }

    private TransctionDetails transactionalDetails(Order order) {
        String orderId = order.get("id");
        String currency = order.get("currency");
        Integer amount = order.get("amount");
        return new TransctionDetails(orderId, currency, amount, KEY);
    }
}
