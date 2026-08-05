package com.vorynt.vorynt_api.mappers;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.dtos.Category.CategoryResponse;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    public List<CategoryResponse> toResponseList(List<Category> categories) {
        return categories.stream().map(this::toResponse).toList();
    }
}
