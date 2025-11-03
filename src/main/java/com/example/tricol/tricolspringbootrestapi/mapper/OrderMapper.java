package com.example.tricol.tricolspringbootrestapi.mapper;

import com.example.tricol.tricolspringbootrestapi.dto.request.ProductDTO;
import com.example.tricol.tricolspringbootrestapi.dto.response.OrderResponse;
import com.example.tricol.tricolspringbootrestapi.model.Order;
import com.example.tricol.tricolspringbootrestapi.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "supplier.id", target = "supplierId")
    OrderResponse toDto(Order order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    List<OrderResponse> toDTOList(List<Order> orders);

}
