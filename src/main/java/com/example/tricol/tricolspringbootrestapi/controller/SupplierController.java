package com.example.tricol.tricolspringbootrestapi.controller;

import com.example.tricol.tricolspringbootrestapi.model.Supplier;
import com.example.tricol.tricolspringbootrestapi.service.impl.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    SupplierService supplierService;
    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }
    @GetMapping
    public List<Supplier> getAllSuppliers(){
       return supplierService.getSuppliers();
    }
    @GetMapping("/{id}")
    public Supplier getSupplierById(@PathVariable Long id){
        return supplierService.getSupplierById(id).orElse(null);
    }
    @PostMapping
    public Supplier createSupplier(@RequestBody Supplier supplier){
        return supplierService.createSupplier(supplier);
    }
    @PutMapping("/{id}")
    public Supplier updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier){
        supplier.setId(id);
        return supplierService.updateSupplier(supplier);
    }
    @DeleteMapping("/{id}")
    public boolean deleteSupplier(@PathVariable Long id){
        return supplierService.deleteSupplierById(id);
    }

}
