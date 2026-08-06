package com.vorynt.vorynt_api.services.product;

import com.vorynt.vorynt_api.domain.category.Category;
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
class GetProductByIdUseCaseTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private GetProductByIdUseCase useCase;

    @Test
    void shouldReturnProduct() {

        Product product = Product.create(
                "Notebook",
                "Gaming",
                BigDecimal.valueOf(1000),
                new Category()
        );

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.of(product));

        Product result = useCase.execute(1L);

        assertEquals(product, result);
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {

        when(repository.findByIdAndEnabledTrue(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> useCase.execute(1L)
        );
    }
}