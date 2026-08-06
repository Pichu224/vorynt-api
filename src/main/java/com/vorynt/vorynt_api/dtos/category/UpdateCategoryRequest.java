package com.vorynt.vorynt_api.dtos.category;

import com.vorynt.vorynt_api.domain.product.Product;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdateCategoryRequest(

        @NotBlank(message = "name is required.")
        String name,

        String description,

        List<Product> products
) {}
