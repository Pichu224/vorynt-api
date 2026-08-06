package com.vorynt.vorynt_api.services.product;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.ProductAlreadyExistsException;
import com.vorynt.vorynt_api.domain.exceptions.ProductNotFoundException;
import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.persistence.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductUseCaseTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private UpdateProductUseCase useCase;

    @Test
    void shouldUpdateProduct() {

        Category category1 = new Category();
        Category category2 = new Category();

        // Arrange
        Product product = Product.create(
                "Notebook",
                "Gaming notebook",
                new BigDecimal("1500.00"),
                category1
        );

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.of(product));

        // Act
        Product updated = useCase.execute(
                1L,
                "Notebook Pro",
                "Gaming notebook RTX",
                new BigDecimal("1800.00"),
                category2
        );

        // Assert
        assertEquals("Notebook Pro", updated.getName());
        assertEquals("Gaming notebook RTX", updated.getDescription());
        assertEquals(new BigDecimal("1800.00"), updated.getPrice());
        assertEquals(category2, updated.getCategory());

        verify(repository).findByIdAndEnabledTrue(1L);
        verify(repository).existsByNameIgnoreCase(anyString());
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {

        // Arrange
        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ProductNotFoundException.class,
                () -> useCase.execute(
                        1L,
                        "Notebook",
                        "Desc",
                        new BigDecimal("100"),
                        new Category()
                )
        );

        verify(repository).findByIdAndEnabledTrue(1L);
        verify(repository, never()).existsByNameIgnoreCase(anyString());
    }

    @Test
    void shouldThrowWhenNewNameAlreadyExists() {

        // Arrange
        Product product = Product.create(
                "Notebook",
                "Gaming",
                new BigDecimal("1000"),
                new Category()
        );

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.of(product));

        when(repository.existsByNameIgnoreCase("macbook"))
                .thenReturn(true);

        // Act & Assert
        assertThrows(
                ProductAlreadyExistsException.class,
                () -> useCase.execute(
                        1L,
                        "MacBook",
                        "Gaming",
                        new BigDecimal("1200"),
                        new Category()
                )
        );

        verify(repository).findByIdAndEnabledTrue(1L);
        verify(repository).existsByNameIgnoreCase("macbook");
    }

    @Test
    void shouldNotCheckDuplicateNameWhenNameDoesNotChange() {

        Category category1 = new Category();
        Category category2 = new Category();

        // Arrange
        Product product = Product.create(
                "Notebook",
                "Gaming",
                new BigDecimal("1000"),
                category1
        );

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.of(product));

        // Act
        Product updated = useCase.execute(
                1L,
                "Notebook",
                "Updated description",
                new BigDecimal("1100"),
                category2
        );

        // Assert
        assertEquals("Notebook", updated.getName());
        assertEquals("Updated description", updated.getDescription());
        assertEquals(new BigDecimal("1100"), updated.getPrice());
        assertEquals(category2, updated.getCategory());

        verify(repository).findByIdAndEnabledTrue(1L);
        verify(repository, never()).existsByNameIgnoreCase(anyString());
    }
}