package com.example.tricol.tricolspringbootrestapi.service.impl;

import com.example.tricol.tricolspringbootrestapi.model.Supplier;
import com.example.tricol.tricolspringbootrestapi.repository.SupplierRepository;
import com.example.tricol.tricolspringbootrestapi.service.SupplierServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService implements SupplierServiceInterface {
    SupplierRepository supplierRepository;
    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public List<Supplier> getSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    @Override
    public Supplier updateSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public boolean deleteSupplierById(Long id) {
        if (supplierRepository.existsById(id)){
            supplierRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
