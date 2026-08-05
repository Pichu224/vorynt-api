package com.vorynt.vorynt_api.services.category;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.CategoryAlreadyExistsException;
import com.vorynt.vorynt_api.domain.exceptions.CategoryNotFoundException;
import com.vorynt.vorynt_api.persistence.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

        // Arrange
        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks"
        );

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.of(category));

        // Act
        Category updated = useCase.execute(
                1L,
                "Notebooks Pro",
                "Gaming notebooks RTX"
        );

        // Assert
        assertEquals("Notebooks Pro", updated.getName());
        assertEquals("Gaming notebooks RTX", updated.getDescription());

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
                        "Desc"
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
                "Gaming notebooks"
        );

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.of(category));

        when(repository.existsByNameIgnoreCase("macbooks"))
                .thenReturn(true);

        // Act & Assert
        assertThrows(
                CategoryAlreadyExistsException.class,
                () -> useCase.execute(
                        1L,
                        "MacBooks",
                        "Gaming macbooks"
                )
        );

        verify(repository).findByIdAndEnabledTrue(1L);
        verify(repository).existsByNameIgnoreCase("macbooks");
    }

    @Test
    void shouldNotCheckDuplicateNameWhenNameDoesNotChange() {

        // Arrange
        Category category = Category.create(
                "Notebooks",
                "Gaming notebooks"
        );

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.of(category));

        // Act
        Category updated = useCase.execute(
                1L,
                "Notebooks",
                "Updated description"
        );

        // Assert
        assertEquals("Notebooks", updated.getName());
        assertEquals("Updated description", updated.getDescription());

        verify(repository).findByIdAndEnabledTrue(1L);
        verify(repository, never()).existsByNameIgnoreCase(anyString());
    }
}