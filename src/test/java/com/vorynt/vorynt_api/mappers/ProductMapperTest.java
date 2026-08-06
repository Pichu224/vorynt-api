package com.vorynt.vorynt_api.mappers;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.dtos.product.ProductResponse;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapper();



    private void assertProductResponse(
            ProductResponse response,
            String name,
            String description,
            BigDecimal price,
            Category category
    ) {
        assertNull(response.id());
        assertEquals(name, response.name());
        assertEquals(description, response.description());
        assertEquals(price, response.price());
        assertEquals(category, response.category());
    }

    @Test
    void shouldMapProductToProductResponse() {

        // Arrange

        Category category = new Category();

        Product product = Product.create(
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500),
                category
        );

        // Act

        ProductResponse productResponse = mapper.toResponse(product);

        // Assert

        assertProductResponse(
                productResponse,
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500),
                category
        );
    }

    @Test
    void shouldMapUsersToResponseList() {

        Category category = new Category();

        // Arrange

        List<Product> products = List.of(
                Product.create("Notebook", "Gaming notebook", BigDecimal.valueOf(1500), category),
                Product.create("Macbook", "Gaming macbook", BigDecimal.valueOf(1200), category)
        );

        // Act

        List<ProductResponse> productResponse = mapper.toResponseList(products);

        // Assert

        assertEquals(2, productResponse.size());

        assertProductResponse(
                productResponse.getFirst(),
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500),
                category
        );

        assertProductResponse(
                productResponse.get(1),
                "Macbook",
                "Gaming macbook",
                BigDecimal.valueOf(1200),
                category
        );
    }
}
