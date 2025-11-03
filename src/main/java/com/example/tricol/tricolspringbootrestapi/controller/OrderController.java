package com.example.tricol.tricolspringbootrestapi.controller;

import com.example.tricol.tricolspringbootrestapi.dto.request.CreateOrderRequest;
import com.example.tricol.tricolspringbootrestapi.dto.request.ProductDTO;
import com.example.tricol.tricolspringbootrestapi.dto.request.UpdateOrderStatus;
import com.example.tricol.tricolspringbootrestapi.dto.response.OrderResponse;
import com.example.tricol.tricolspringbootrestapi.model.Order;
import com.example.tricol.tricolspringbootrestapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> create(@RequestBody CreateOrderRequest request) {
        try {
        OrderResponse createdOrder = orderService.createOrder(request);
            return  ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllOrders(){
        try {
            List<OrderResponse> orders = orderService.getAllOrders();
            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No orders found");
            }
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving orders: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable Long id){
        try {
            OrderResponse order = orderService.getOrderById(id);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found with id: " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateOrder(@PathVariable Long id, @RequestBody UpdateOrderStatus orderDto) {
        try {
            OrderResponse updatedOrder = orderService.updateOrder(id, orderDto);
            return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found with id: " + id);
        }
    }

    @GetMapping("/filter/status")
    public ResponseEntity<Object> filterOrdersByStatus(@RequestParam String status) {
        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            List<OrderResponse> orders = orderService.filterOrdersByStatus(orderStatus);
            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No orders found with status: " + status);
            }
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid order status: " + status + ". Valid values are: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error filtering orders: " + e.getMessage());
        }
    }

    @GetMapping("/filter/supplier/{supplierId}")
    public ResponseEntity<Object> filterOrdersBySupplier(@PathVariable Long supplierId) {
        try {
            List<OrderResponse> orders = orderService.filterOrdersBySupplier(supplierId);
            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No orders found for supplier with id: " + supplierId);
            }
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supplier not found with id: " + supplierId);
        }
    }

    @GetMapping("/filter/date-range")
    public ResponseEntity<Object> filterOrdersByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            List<OrderResponse> orders = orderService.filterOrdersByDateRange(start, end);
            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No orders found between " + startDate + " and " + endDate);
            }
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format. Use ISO format: yyyy-MM-ddTHH:mm:ss");
        }
    }
}
