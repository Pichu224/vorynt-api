package com.vorynt.vorynt_api.domain.category;

import com.vorynt.vorynt_api.domain.exceptions.RequiredFieldException;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void shouldCreateCategory() {

        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks"
        );

        assertEquals("Notebooks", category.getName());
        assertEquals("Gaming notebooks", category.getDescription());

        assertTrue(category.isEnabled());

        assertNotNull(category.getCreatedAt());
        assertNotNull(category.getUpdatedAt());
    }

    @Test
    void shouldTrimNameAndDescription() {

        Category category = Category.create(
                "   Notebooks   ",
                "   Gaming notebooks   "
        );

        assertEquals("Notebooks", category.getName());
        assertEquals("Gaming notebooks", category.getDescription());
    }

    @Test
    void shouldAllowNullDescription() {

        Category category = Category.create(
                "Notebooks",
                null
        );

        assertNull(category.getDescription());
    }

    @Test
    void shouldThrowWhenNameIsNull() {

        assertThrows(
                RequiredFieldException.class,
                () -> Category.create(
                        null,
                        "Description"
                )
        );
    }

    @Test
    void shouldThrowWhenNameIsBlank() {

        assertThrows(
                RequiredFieldException.class,
                () -> Category.create(
                        "   ",
                        "Description"
                )
        );
    }

    @Test
    void shouldChangeName() {

        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks"
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
                "Gaming notebooks"
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
                "Gaming notebooks"
        );

        category.deactivate();

        assertFalse(category.isEnabled());
    }

    @Test
    void shouldNotChangeUpdatedAtWhenAlreadyDisabled() {

        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks"
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
                "Gaming notebooks"
        );

        category.deactivate();

        category.activate();

        assertTrue(category.isEnabled());
    }

    @Test
    void shouldNotChangeUpdatedAtWhenAlreadyEnabled() {

        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks"
        );

        OffsetDateTime updatedAt = category.getUpdatedAt();

        category.activate();

        assertEquals(updatedAt, category.getUpdatedAt());
    }
}