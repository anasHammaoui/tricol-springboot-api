package com.example.tricol.tricolspringbootrestapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    private String society;
    private String address;
    private String socialReason;
    private String contactAgent;
    private String email;
    private String phone;
    private String city;
    private String ice;
}