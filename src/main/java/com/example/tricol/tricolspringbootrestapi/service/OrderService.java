package com.example.tricol.tricolspringbootrestapi.service;

import com.example.tricol.tricolspringbootrestapi.dto.request.CreateOrderRequest;
import com.example.tricol.tricolspringbootrestapi.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getAllOrders();
}
