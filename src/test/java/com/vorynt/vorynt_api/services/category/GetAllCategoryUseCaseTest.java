package com.vorynt.vorynt_api.services.category;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.persistence.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllCategoryUseCaseTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private GetAllCategoriesUseCase useCase;

    @Test
    void shouldReturnAllEnabledCategories() {

        List<Category> categories = List.of(
                Category.create("Components", "Gaming components"),
                Category.create("Notebooks", "Gaming notebooks")
        );

        when(repository.findAllByEnabledTrue())
                .thenReturn(categories);

        List<Category> result = useCase.execute();

        assertEquals(2, result.size());

        verify(repository).findAllByEnabledTrue();
    }
}