package com.example.tricol.tricolspringbootrestapi.repository;

import com.example.tricol.tricolspringbootrestapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
}
