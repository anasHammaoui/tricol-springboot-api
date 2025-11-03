package com.example.tricol.tricolspringbootrestapi.service.impl;

import com.example.tricol.tricolspringbootrestapi.dto.request.CreateOrderItemRequest;
import com.example.tricol.tricolspringbootrestapi.dto.request.CreateOrderRequest;
import com.example.tricol.tricolspringbootrestapi.dto.request.SupplierDTO;
import com.example.tricol.tricolspringbootrestapi.dto.request.UpdateOrderStatus;
import com.example.tricol.tricolspringbootrestapi.dto.response.OrderResponse;
import com.example.tricol.tricolspringbootrestapi.mapper.OrderMapper;
import com.example.tricol.tricolspringbootrestapi.model.Order;
import com.example.tricol.tricolspringbootrestapi.model.OrderItem;
import com.example.tricol.tricolspringbootrestapi.model.Product;
import com.example.tricol.tricolspringbootrestapi.model.Supplier;
import com.example.tricol.tricolspringbootrestapi.repository.OrderRepository;
import com.example.tricol.tricolspringbootrestapi.repository.ProductRepository;
import com.example.tricol.tricolspringbootrestapi.repository.SupplierRepository;
import com.example.tricol.tricolspringbootrestapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId()).
                orElseThrow(() -> new RuntimeException("Supplier not found with id: " + request.getSupplierId()));

        Order order = new Order();
        order.setSupplier(supplier);
        order.setStatus(Order.OrderStatus.pending);

        List<OrderItem> items = new ArrayList<>();
        double totalAmount = 0;

        for (CreateOrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(product.getUnitPrice());
            item.setTotal(product.getUnitPrice() * itemReq.getQuantity());

            totalAmount += item.getTotal();

            items.add(item);
        }
        order.setItems(items);
        order.setTotalAmount(totalAmount);

        Order saved = orderRepository.save(order);

        return orderMapper.toDto(saved);
    }

    public OrderResponse getOrderById(Long id){
        return orderRepository.findById(id)
                .map(order -> orderMapper.toDto(order))
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public List<OrderResponse> getAllOrders(){
        return orderMapper.toDTOList(orderRepository.findAll());
    }

    //update order
    public OrderResponse updateOrder(Long id, UpdateOrderStatus request) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
        orderMapper.updateOrderFromDTO(request, existingOrder);
        return orderMapper.toDto(orderRepository.save(existingOrder));
    }
}
