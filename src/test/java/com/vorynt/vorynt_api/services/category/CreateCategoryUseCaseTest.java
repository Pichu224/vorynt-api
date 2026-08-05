package com.vorynt.vorynt_api.services.category;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.CategoryAlreadyExistsException;
import com.vorynt.vorynt_api.persistence.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CreateCategoryUseCase useCase;

    @Test
    void shouldCreateCategory() {

        Category category = Category.create(
                "Components",
                "Gaming components"
        );

        when(repository.existsByNameIgnoreCase("components")).thenReturn(false);
        when(repository.save(any(Category.class))).thenReturn(category);

        Category created = useCase.execute(
                "Components",
                "Gaming components"
        );

        assertEquals("Components", created.getName());
        assertEquals("Gaming components", created.getDescription());

        verify(repository).save(any(Category.class));
    }

    @Test
    void shouldThrowWhenCategoryAlreadyExists() {

        when(repository.existsByNameIgnoreCase("components")).thenReturn(true);

        assertThrows(
                CategoryAlreadyExistsException.class,
                () -> useCase.execute(
                        "Components",
                        "Gaming components"
                )
        );

        verify(repository, never()).save(any());
    }
}