package com.vorynt.vorynt_api.mappers;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.dtos.Category.CategoryResponse;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CategoryMapperTest {

    private final CategoryMapper mapper = new CategoryMapper();

    private void assertCategoryResponse(
            CategoryResponse response,
            String name,
            String description
    ) {
        assertNull(response.id());
        assertEquals(name, response.name());
        assertEquals(description, response.description());
    }

    @Test
    void shouldMapCategoryToCategoryResponse() {

        // Arrange

        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks"
        );

        // Act

        CategoryResponse categoryResponse = mapper.toResponse(category);

        // Assert

        assertCategoryResponse(
                categoryResponse,
                "Notebooks",
                "Gaming notebooks"
        );
    }

    @Test
    void shouldMapUsersToResponseList() {

        // Arrange

        List<Category> categories = List.of(
                Category.create("Notebooks", "Gaming notebooks"),
                Category.create("Components", "Gaming components")
        );

        // Act

        List<CategoryResponse> categoryResponse = mapper.toResponseList(categories);

        // Assert

        assertEquals(2, categoryResponse.size());

        assertCategoryResponse(
                categoryResponse.getFirst(),
                "Notebooks",
                "Gaming notebooks"
        );

        assertCategoryResponse(
                categoryResponse.get(1),
                "Components",
                "Gaming components"
        );
    }
}
