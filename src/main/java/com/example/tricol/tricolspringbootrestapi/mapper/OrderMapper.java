package com.example.tricol.tricolspringbootrestapi.mapper;

import com.example.tricol.tricolspringbootrestapi.dto.response.OrderResponse;
import com.example.tricol.tricolspringbootrestapi.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "orderDate", target = "orderDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "totalAmount", target = "totalAmount")
    @Mapping(source = "items", target = "items")
    OrderResponse toDto(Order order);
}
