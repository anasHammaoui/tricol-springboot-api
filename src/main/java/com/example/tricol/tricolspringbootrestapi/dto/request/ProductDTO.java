package com.example.tricol.tricolspringbootrestapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Reference is required")
    private String reference;

    @NotBlank(message = "Name is required")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Min(value = 0, message = "Unit price must be greater than 0")
    private Double unitPrice;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Measurement unit is required")
    private String measureUnit;

    @NotBlank(message = "The re-order point is required")
    private Double reorderPoint;

    @Min(value = 0, message = "Stock cannot be below 0")
    private Double currentStock;
}
