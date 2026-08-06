package com.vorynt.vorynt_api.services.category;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.CategoryAlreadyExistsException;
import com.vorynt.vorynt_api.domain.exceptions.CategoryNotFoundException;
import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.persistence.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryUseCaseTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private UpdateCategoryUseCase useCase;

    @Test
    void shouldUpdateCategory() {

        List<Product> products = List.of(
                Product.create(
                        "Notebook",
                        "Gaming notebook",
                        BigDecimal.valueOf(150),
                        null
                )
        );

        // Arrange
        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks",
                products
        );

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.of(category));

        // Act
        Category updated = useCase.execute(
                1L,
                "Notebooks Pro",
                "Gaming notebooks RTX",
                new ArrayList<>()
        );

        // Assert
        assertEquals("Notebooks Pro", updated.getName());
        assertEquals("Gaming notebooks RTX", updated.getDescription());
        assertEquals(0, updated.getProducts().size());

        verify(repository).findByIdAndEnabledTrue(1L);
        verify(repository).existsByNameIgnoreCase(anyString());
    }

    @Test
    void shouldThrowWhenCategoryDoesNotExist() {

        // Arrange
        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                CategoryNotFoundException.class,
                () -> useCase.execute(
                        1L,
                        "Notebooks",
                        "Desc",
                        List.of()
                )
        );

        verify(repository).findByIdAndEnabledTrue(1L);
        verify(repository, never()).existsByNameIgnoreCase(anyString());
    }

    @Test
    void shouldThrowWhenNewNameAlreadyExists() {

        // Arrange
        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks",
                List.of()
        );

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.of(category));

        when(repository.existsByNameIgnoreCase("MacBooks"))
                .thenReturn(true);

        // Act & Assert
        assertThrows(
                CategoryAlreadyExistsException.class,
                () -> useCase.execute(
                        1L,
                        "MacBooks",
                        "Gaming macbooks",
                        List.of()
                )
        );

        verify(repository).findByIdAndEnabledTrue(1L);
        verify(repository).existsByNameIgnoreCase("MacBooks");
    }

    @Test
    void shouldNotCheckDuplicateNameWhenNameDoesNotChange() {

        List<Product> products = List.of(
                Product.create(
                        "Notebook",
                        "Gaming notebook",
                        BigDecimal.valueOf(150),
                        null
                )
        );

        // Arrange
        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks",
                products
        );

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.of(category));

        // Act
        Category updated = useCase.execute(
                1L,
                "Notebooks",
                "Updated description",
                new ArrayList<>()
        );

        // Assert
        assertEquals("Notebooks", updated.getName());
        assertEquals("Updated description", updated.getDescription());
        assertEquals(0, updated.getProducts().size());

        verify(repository).findByIdAndEnabledTrue(1L);
        verify(repository, never()).existsByNameIgnoreCase(anyString());
    }
}