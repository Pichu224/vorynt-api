package com.vorynt.vorynt_api.domain.product;

import com.vorynt.vorynt_api.domain.exceptions.InvalidPriceException;
import com.vorynt.vorynt_api.domain.exceptions.RequiredFieldException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateProduct() {

        Product product = Product.create(
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500)
        );

        assertEquals("Notebook", product.getName());
        assertEquals("Gaming notebook", product.getDescription());
        assertEquals(BigDecimal.valueOf(1500), product.getPrice());

        assertTrue(product.isEnabled());

        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
    }

    @Test
    void shouldTrimNameAndDescription() {

        Product product = Product.create(
                "   Notebook   ",
                "   Gaming notebook   ",
                BigDecimal.valueOf(1500)
        );

        assertEquals("Notebook", product.getName());
        assertEquals("Gaming notebook", product.getDescription());
    }

    @Test
    void shouldAllowNullDescription() {

        Product product = Product.create(
                "Notebook",
                null,
                BigDecimal.valueOf(1500)
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
                        BigDecimal.ONE
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
                        BigDecimal.ONE
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
                        null
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
                        BigDecimal.ZERO
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
                        BigDecimal.valueOf(-10)
                )
        );
    }

    @Test
    void shouldChangeName() {

        Product product = Product.create(
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500)
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
                BigDecimal.valueOf(1500)
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
                BigDecimal.valueOf(1500)
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
                BigDecimal.valueOf(1500)
        );

        product.deactivate();

        assertFalse(product.isEnabled());
    }

    @Test
    void shouldNotChangeUpdatedAtWhenAlreadyDisabled() {

        Product product = Product.create(
                "Notebook",
                "Gaming notebook",
                BigDecimal.valueOf(1500)
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
                BigDecimal.valueOf(1500)
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
                BigDecimal.valueOf(1500)
        );

        OffsetDateTime updatedAt = product.getUpdatedAt();

        product.activate();

        assertEquals(updatedAt, product.getUpdatedAt());
    }
}