package com.vorynt.vorynt_api.dtos.product;

import com.vorynt.vorynt_api.domain.category.Category;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Category category
) {}
