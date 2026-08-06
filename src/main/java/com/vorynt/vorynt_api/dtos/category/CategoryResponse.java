package com.vorynt.vorynt_api.dtos.category;

import com.vorynt.vorynt_api.domain.product.Product;

import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        List<Product> products
) {}
