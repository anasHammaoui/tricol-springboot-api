package com.example.tricol.tricolspringbootrestapi.repository;

import com.example.tricol.tricolspringbootrestapi.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
