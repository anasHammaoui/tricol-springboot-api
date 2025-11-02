package com.example.tricol.tricolspringbootrestapi.service;

import com.example.tricol.tricolspringbootrestapi.dto.request.CreateOrderRequest;
import com.example.tricol.tricolspringbootrestapi.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
}
