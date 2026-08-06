package com.vorynt.vorynt_api.domain.category;

import com.vorynt.vorynt_api.domain.exceptions.RequiredFieldException;
import com.vorynt.vorynt_api.domain.product.Product;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void shouldCreateCategory() {

        List<Product> products = List.of(
                Product.create(
                    "Laptop",
                    "Gaming Laptop",
                    BigDecimal.valueOf(1000),
                    null
                )
        );

        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks",
                products
        );

        assertEquals("Notebooks", category.getName());
        assertEquals("Gaming notebooks", category.getDescription());
        assertEquals(products, category.getProducts());

        assertTrue(category.isEnabled());

        assertNotNull(category.getCreatedAt());
        assertNotNull(category.getUpdatedAt());
    }

    @Test
    void shouldTrimNameAndDescription() {

        List<Product> products = List.of(
                Product.create(
                        "Laptop",
                        "Gaming Laptop",
                        BigDecimal.valueOf(1000),
                        null
                )
        );

        Category category = Category.create(
                "   Notebooks   ",
                "   Gaming notebooks   ",
                products
        );

        assertEquals("Notebooks", category.getName());
        assertEquals("Gaming notebooks", category.getDescription());
        assertEquals(products, category.getProducts());
    }

    @Test
    void shouldAllowNullDescription() {

        Category category = Category.create(
                "Notebooks",
                null,
                List.of()
        );

        assertNull(category.getDescription());
    }

    @Test
    void shouldAllowNullProducts() {

        Category category = Category.create(
                "Notebooks",
                "Gaming Laptop",
                List.of()
        );

        assertEquals(ArrayList.class, category.getProducts().getClass());
    }

    @Test
    void shouldThrowWhenNameIsNull() {

        assertThrows(
                RequiredFieldException.class,
                () -> Category.create(
                        null,
                        "Description",
                        List.of()
                )
        );
    }

    @Test
    void shouldThrowWhenNameIsBlank() {

        assertThrows(
                RequiredFieldException.class,
                () -> Category.create(
                        "   ",
                        "Description",
                        List.of()
                )
        );
    }

    @Test
    void shouldChangeName() {

        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks",
                List.of()
        );

        OffsetDateTime before = category.getUpdatedAt();

        category.changeName("Mouses");

        assertEquals("Mouses", category.getName());
        assertNotNull(category.getUpdatedAt());
    }

    @Test
    void shouldChangeDescription() {

        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks",
                List.of()
        );

        OffsetDateTime before = category.getUpdatedAt();

        category.changeDescription("Wireless mouses");

        assertEquals("Wireless mouses", category.getDescription());
        assertNotNull(category.getUpdatedAt());
    }

    @Test
    void shouldDeactivateCategory() {

        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks",
                List.of()
        );

        category.deactivate();

        assertFalse(category.isEnabled());
    }

    @Test
    void shouldNotChangeUpdatedAtWhenAlreadyDisabled() {

        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks",
                List.of()
        );

        category.deactivate();

        OffsetDateTime updatedAt = category.getUpdatedAt();

        category.deactivate();

        assertEquals(updatedAt, category.getUpdatedAt());
    }

    @Test
    void shouldActivateProduct() {

        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks",
                List.of()
        );

        category.deactivate();

        category.activate();

        assertTrue(category.isEnabled());
    }

    @Test
    void shouldNotChangeUpdatedAtWhenAlreadyEnabled() {

        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks",
                List.of()
        );

        OffsetDateTime updatedAt = category.getUpdatedAt();

        category.activate();

        assertEquals(updatedAt, category.getUpdatedAt());
    }
}