package com.vorynt.vorynt_api.mappers;

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
            BigDecimal price
    ) {
        assertNull(response.id());
        assertEquals(name, response.name());
        assertEquals(description, response.description());
        assertEquals(price, response.price());
    }

    @Test
    void shouldMapProductToProductResponse() {

        // Arrange

        Product product = Product.create(
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500)
        );

        // Act

        ProductResponse productResponse = mapper.toResponse(product);

        // Assert

        assertProductResponse(
                productResponse,
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500)
        );
    }

    @Test
    void shouldMapUsersToResponseList() {

        // Arrange

        List<Product> products = List.of(
                Product.create("Notebook", "Gaming notebook", BigDecimal.valueOf(1500)),
                Product.create("Macbook", "Gaming macbook", BigDecimal.valueOf(1200))
        );

        // Act

        List<ProductResponse> productResponse = mapper.toResponseList(products);

        // Assert

        assertEquals(2, productResponse.size());

        assertProductResponse(
                productResponse.getFirst(),
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500)
        );

        assertProductResponse(
                productResponse.get(1),
                "Macbook",
                "Gaming macbook",
                BigDecimal.valueOf(1200)
        );
    }
}
