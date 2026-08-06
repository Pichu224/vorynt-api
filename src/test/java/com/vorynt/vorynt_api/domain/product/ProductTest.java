package com.vorynt.vorynt_api.domain.product;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.InvalidPriceException;
import com.vorynt.vorynt_api.domain.exceptions.RequiredFieldException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateProduct() {

        Category category = new Category();

        Product product = Product.create(
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500),
                category
        );

        assertEquals("Notebook", product.getName());
        assertEquals("Gaming notebook", product.getDescription());
        assertEquals(BigDecimal.valueOf(1500), product.getPrice());

        assertTrue(product.isEnabled());
        assertEquals(category, product.getCategory());

        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
    }

    @Test
    void shouldTrimNameAndDescription() {

        Product product = Product.create(
                "   Notebook   ",
                "   Gaming notebook   ",
                BigDecimal.valueOf(1500),
                new Category()
        );

        assertEquals("Notebook", product.getName());
        assertEquals("Gaming notebook", product.getDescription());
    }

    @Test
    void shouldAllowNullDescription() {

        Product product = Product.create(
                "Notebook",
                null,
                BigDecimal.valueOf(1500),
                new Category()
        );

        assertNull(product.getDescription());
    }

    @Test
    void shouldThrowWhenNameIsNull() {

        assertThrows(
                RequiredFieldException.class,
                () -> Product.create(
                        null,
                        "Description",
                        BigDecimal.ONE,
                        new Category()
                )
        );
    }

    @Test
    void shouldThrowWhenNameIsBlank() {

        assertThrows(
                RequiredFieldException.class,
                () -> Product.create(
                        "   ",
                        "Description",
                        BigDecimal.ONE,
                        new Category()
                )
        );
    }

    @Test
    void shouldThrowWhenPriceIsNull() {

        assertThrows(
                RequiredFieldException.class,
                () -> Product.create(
                        "Notebook",
                        "Description",
                        null,
                        new Category()
                )
        );
    }

    @Test
    void shouldThrowWhenPriceIsZero() {

        assertThrows(
                InvalidPriceException.class,
                () -> Product.create(
                        "Notebook",
                        "Description",
                        BigDecimal.ZERO,
                        new Category()
                )
        );
    }

    @Test
    void shouldThrowWhenPriceIsNegative() {

        assertThrows(
                InvalidPriceException.class,
                () -> Product.create(
                        "Notebook",
                        "Description",
                        BigDecimal.valueOf(-10),
                        new Category()
                )
        );
    }

    @Test
    void shouldChangeName() {

        Product product = Product.create(
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500),
                new Category()
        );

        OffsetDateTime before = product.getUpdatedAt();

        product.changeName("Mouse");

        assertEquals("Mouse", product.getName());
        assertNotNull(product.getUpdatedAt());
    }

    @Test
    void shouldChangeDescription() {

        Product product = Product.create(
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500),
                new Category()
        );

        OffsetDateTime before = product.getUpdatedAt();

        product.changeDescription("Wireless mouse");

        assertEquals("Wireless mouse", product.getDescription());
        assertNotNull(product.getUpdatedAt());
    }

    @Test
    void shouldChangePrice() {

        Product product = Product.create(
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500),
                new Category()
        );

        OffsetDateTime before = product.getUpdatedAt();

        product.changePrice(BigDecimal.valueOf(2500));

        assertEquals(BigDecimal.valueOf(2500), product.getPrice());
        assertNotNull(product.getUpdatedAt());
    }

    @Test
    void shouldDeactivateProduct() {

        Product product = Product.create(
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500),
                new Category()
        );

        product.deactivate();

        assertFalse(product.isEnabled());
    }

    @Test
    void shouldNotChangeUpdatedAtWhenAlreadyDisabled() {

        Product product = Product.create(
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500),
                new Category()
        );

        product.deactivate();

        OffsetDateTime updatedAt = product.getUpdatedAt();

        product.deactivate();

        assertEquals(updatedAt, product.getUpdatedAt());
    }

    @Test
    void shouldActivateProduct() {

        Product product = Product.create(
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500),
                new Category()
        );

        product.deactivate();

        product.activate();

        assertTrue(product.isEnabled());
    }

    @Test
    void shouldNotChangeUpdatedAtWhenAlreadyEnabled() {

        Product product = Product.create(
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500),
                new Category()
        );

        OffsetDateTime updatedAt = product.getUpdatedAt();

        product.activate();

        assertEquals(updatedAt, product.getUpdatedAt());
    }
}