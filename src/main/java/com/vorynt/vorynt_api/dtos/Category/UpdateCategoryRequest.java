package com.vorynt.vorynt_api.dtos.Category;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryRequest(

        @NotBlank(message = "name is required.")
        String name,

        String description
) {}
