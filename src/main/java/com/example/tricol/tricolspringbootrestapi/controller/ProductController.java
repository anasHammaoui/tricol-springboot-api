package com.example.tricol.tricolspringbootrestapi.controller;

import com.example.tricol.tricolspringbootrestapi.dto.request.ProductDTO;
import com.example.tricol.tricolspringbootrestapi.model.Product;
import com.example.tricol.tricolspringbootrestapi.service.ProductService;
import com.example.tricol.tricolspringbootrestapi.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<Object> createProduct(@RequestBody ProductDTO productDTO){
        try {
            Product product = productService.createProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product Created Successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Product could not be created");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable Long id){
        try {
            ProductDTO product = productService.getProductById(id);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Product not found");
        }
    }

    @GetMapping
    public ResponseEntity<Object> getProducts(){
        try {
            List<ProductDTO> products = productService.getProducts();
            return  ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Products not found");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        try {
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Product not found or could not be updated");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body("deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Product not found or could not be deleted");
        }
    }

    @GetMapping("/stock/{id}")
    public ResponseEntity<Object> getProductStock(@PathVariable Long id){
        try {
            Double stock = productService.getProductStock(id);
            return ResponseEntity.status(HttpStatus.OK).body(stock);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Product not found");
        }
    }

    @GetMapping("/lowstock")
    public  ResponseEntity<Object> getLowStockProducts(){
        try {
            List<ProductDTO> lowStockProducts = productService.getLowStockProducts();
            return ResponseEntity.status(HttpStatus.OK).body(lowStockProducts);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("No low stock products found");
        }
    }

}
