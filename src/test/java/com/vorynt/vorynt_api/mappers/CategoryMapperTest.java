package com.vorynt.vorynt_api.mappers;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.dtos.category.CategoryResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CategoryMapperTest {

    private final CategoryMapper mapper = new CategoryMapper();

    private void assertCategoryResponse(
            CategoryResponse response,
            String name,
            String description,
            List<Product> products
    ) {
        assertNull(response.id());
        assertEquals(name, response.name());
        assertEquals(description, response.description());
        assertEquals(products, response.products());
    }

    @Test
    void shouldMapCategoryToCategoryResponse() {

        List<Product> products = new ArrayList<>();

        // Arrange

        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks",
                products
        );

        // Act

        CategoryResponse categoryResponse = mapper.toResponse(category);

        // Assert

        assertCategoryResponse(
                categoryResponse,
                "Notebooks",
                "Gaming notebooks",
                products
        );
    }

    @Test
    void shouldMapUsersToResponseList() {

        List<Product> products = new ArrayList<>();

        // Arrange

        List<Category> categories = List.of(
                Category.create("Notebooks", "Gaming notebooks", products),
                Category.create("Components", "Gaming components", products)
        );

        // Act

        List<CategoryResponse> categoryResponse = mapper.toResponseList(categories);

        // Assert

        assertEquals(2, categoryResponse.size());

        assertCategoryResponse(
                categoryResponse.getFirst(),
                "Notebooks",
                "Gaming notebooks",
                products
        );

        assertCategoryResponse(
                categoryResponse.get(1),
                "Components",
                "Gaming components",
                products
        );
    }
}
