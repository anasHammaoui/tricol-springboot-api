package com.example.tricol.tricolspringbootrestapi.service.impl;

import com.example.tricol.tricolspringbootrestapi.dto.request.CreateOrderItemRequest;
import com.example.tricol.tricolspringbootrestapi.dto.request.CreateOrderRequest;
import com.example.tricol.tricolspringbootrestapi.dto.response.OrderItemResponse;
import com.example.tricol.tricolspringbootrestapi.dto.response.OrderResponse;
import com.example.tricol.tricolspringbootrestapi.dto.response.ReceiveOrderResponse;
import com.example.tricol.tricolspringbootrestapi.mapper.OrderItemMapper;
import com.example.tricol.tricolspringbootrestapi.mapper.OrderMapper;
import com.example.tricol.tricolspringbootrestapi.model.*;
import com.example.tricol.tricolspringbootrestapi.repository.*;
import com.example.tricol.tricolspringbootrestapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockSlotRepository stockSlotRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

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

    @Transactional
    public ReceiveOrderResponse receiveOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (order.getStatus() == Order.OrderStatus.delivered) {
            throw new RuntimeException("Order has already been received");
        }
        order.setStatus(Order.OrderStatus.delivered);

        // create one stockslot per orderitem (per product)
        List<StockSlot> stockSlots = new ArrayList<>();

        for (OrderItem orderItem : order.getItems()) {
            StockSlot stockSlot = new StockSlot();
            stockSlot.setOrder(order);
            stockSlot.setQuantity(orderItem.getQuantity());
            stockSlot.setUnitPrice(orderItem.getUnitPrice());

            stockSlots.add(stockSlot);

            // update product current stock
            Product product = orderItem.getProduct();
            Double currentStock = product.getCurrentStock();
            Double newStock = currentStock + orderItem.getQuantity();
            product.setCurrentStock(newStock);

            productRepository.save(product);
        }

        // save all stock slots
        stockSlotRepository.saveAll(stockSlots);
        order.setStockSlot(stockSlots);

        // save updated order
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toReceiveOrderResponse(savedOrder);
    }

}
