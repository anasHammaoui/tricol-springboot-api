package com.example.tricol.tricolspringbootrestapi.service;

import com.example.tricol.tricolspringbootrestapi.model.Supplier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SupplierServiceInterface {
List<Supplier> getSuppliers();
Supplier createSupplier(Supplier supplier);
Optional<Supplier> getSupplierById(Long id);
Supplier updateSupplier(Supplier supplier);
boolean deleteSupplierById(Long id);
}
