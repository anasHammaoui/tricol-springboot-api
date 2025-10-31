package com.example.tricol.tricolspringbootrestapi.controller;

import com.example.tricol.tricolspringbootrestapi.dto.request.SupplierDTO;
import com.example.tricol.tricolspringbootrestapi.model.Supplier;
import com.example.tricol.tricolspringbootrestapi.service.impl.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @PostMapping("/create")
    public ResponseEntity<Supplier> createSupplier(@RequestBody SupplierDTO supplierDTO) {
        try {
            Supplier supplier = supplierService.createSupplier(supplierDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(supplier);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllSuppliers() {
        try {
            List<SupplierDTO> suppliers = supplierService.getSuppliers();
            return ResponseEntity.ok(suppliers);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("suppliers not found");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getSupplierById(@PathVariable Long id) {
        SupplierDTO supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(supplier);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSupplier(@PathVariable Long id, @RequestBody SupplierDTO supplierDTO) {
        try {
            SupplierDTO updatedSupplier = supplierService.updateSupplier(id, supplierDTO);
            return ResponseEntity.ok(updatedSupplier);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Supplier not found or could not be updated");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.ok("deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Supplier not found or could not be deleted");
        }
    }

    // search suppliers by query (society or contact agent)
    @GetMapping("/search")
    public ResponseEntity<Object> searchSuppliers(@RequestParam("q") String query) {
        try {
            List<SupplierDTO> suppliers = supplierService.searchSuppliers(query);
            return ResponseEntity.ok(suppliers);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Supplier not found or could not be deleted");
        }
    }
}