package com.example.tricol.tricolspringbootrestapi.controller;

import com.example.tricol.tricolspringbootrestapi.dto.request.CreateOrderRequest;
import com.example.tricol.tricolspringbootrestapi.dto.response.OrderResponse;
import com.example.tricol.tricolspringbootrestapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> create(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }
}
