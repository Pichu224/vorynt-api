package com.vorynt.vorynt_api.services.product;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.ProductAlreadyExistsException;
import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.persistence.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private CreateProductUseCase useCase;

    @Test
    void shouldCreateProduct() {

        Category category = new Category();

        Product product = Product.create(
                "Notebook",
                "Gaming",
                BigDecimal.valueOf(1500),
                category
        );

        when(repository.existsByNameIgnoreCase("notebook")).thenReturn(false);
        when(repository.save(any(Product.class))).thenReturn(product);

        Product created = useCase.execute(
                "Notebook",
                "Gaming",
                BigDecimal.valueOf(1500),
                category
        );

        assertEquals("Notebook", created.getName());
        assertEquals(BigDecimal.valueOf(1500), created.getPrice());
        assertEquals(category, created.getCategory());

        verify(repository).save(any(Product.class));
    }

    @Test
    void shouldThrowWhenProductAlreadyExists() {

        when(repository.existsByNameIgnoreCase("notebook")).thenReturn(true);

        assertThrows(
                ProductAlreadyExistsException.class,
                () -> useCase.execute(
                        "Notebook",
                        "Gaming",
                        BigDecimal.valueOf(1500),
                        new Category()
                )
        );

        verify(repository, never()).save(any());
    }
}