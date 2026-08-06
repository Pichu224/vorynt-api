package com.vorynt.vorynt_api.dtos.category;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record UpdateCategoryRequest(

        @NotBlank(message = "name is required.")
        String name,

        String description
) {}
