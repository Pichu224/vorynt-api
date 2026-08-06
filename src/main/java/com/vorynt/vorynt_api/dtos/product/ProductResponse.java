package com.vorynt.vorynt_api.dtos.product;

import com.vorynt.vorynt_api.dtos.category.CategoryResponse;
import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        CategoryResponse category
) {}
