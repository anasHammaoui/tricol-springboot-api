package com.example.tricol.tricolspringbootrestapi.dto.request;

import com.example.tricol.tricolspringbootrestapi.model.Order;

import java.util.List;

public class UpdateOrderStatus {
    private List<CreateOrderItemRequest> items;
    private Order.OrderStatus status;
}
