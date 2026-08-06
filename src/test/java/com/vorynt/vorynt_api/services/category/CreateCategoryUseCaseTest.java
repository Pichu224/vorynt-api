package com.vorynt.vorynt_api.services.category;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.CategoryAlreadyExistsException;
import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.persistence.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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

        List<Product> products = new ArrayList<>();

        Category category = Category.create(
                "Components",
                "Gaming components",
                products
        );

        when(repository.existsByNameIgnoreCase("Components")).thenReturn(false);
        when(repository.save(any(Category.class))).thenReturn(category);

        Category created = useCase.execute(
                "Components",
                "Gaming components",
                products
        );

        assertEquals("Components", created.getName());
        assertEquals("Gaming components", created.getDescription());
        assertEquals(products, created.getProducts());

        verify(repository).save(any(Category.class));
    }

    @Test
    void shouldThrowWhenCategoryAlreadyExists() {

        when(repository.existsByNameIgnoreCase("Components")).thenReturn(true);

        assertThrows(
                CategoryAlreadyExistsException.class,
                () -> useCase.execute(
                        "Components",
                        "Gaming components",
                        new ArrayList<>()
                )
        );

        verify(repository, never()).save(any());
    }
}