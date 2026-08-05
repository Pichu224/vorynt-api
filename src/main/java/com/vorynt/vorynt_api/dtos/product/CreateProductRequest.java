package com.vorynt.vorynt_api.dtos.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(

        @NotBlank(message = "name is required.")
        String name,

        String description,

        @NotNull(message = "Price is required.")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero.")
        BigDecimal price
) {}
