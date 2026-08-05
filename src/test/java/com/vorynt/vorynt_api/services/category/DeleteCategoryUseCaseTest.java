package com.vorynt.vorynt_api.services.category;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.CategoryNotFoundException;
import com.vorynt.vorynt_api.persistence.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryUseCaseTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private DeleteCategoryUseCase useCase;

    @Test
    void shouldDeactivateCategory() {

        Category category = Category.create(
                "Components",
                "Gaming components"
        );

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.of(category));

        useCase.execute(1L);

        assertFalse(category.isEnabled());
    }

    @Test
    void shouldThrowWhenCategoryDoesNotExist() {

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> useCase.execute(1L)
        );
    }
}