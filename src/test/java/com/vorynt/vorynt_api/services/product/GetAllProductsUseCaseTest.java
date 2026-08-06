package com.vorynt.vorynt_api.services.product;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.persistence.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllProductsUseCaseTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private GetAllProductsUseCase useCase;

    @Test
    void shouldReturnAllEnabledProducts() {

        List<Product> products = List.of(
                Product.create("Notebook", "Gaming", BigDecimal.valueOf(1000), new Category()),
                Product.create("Mouse", "Wireless", BigDecimal.valueOf(100), new Category())
        );

        when(repository.findAllByEnabledTrue())
                .thenReturn(products);

        List<Product> result = useCase.execute();

        assertEquals(2, result.size());

        verify(repository).findAllByEnabledTrue();
    }
}