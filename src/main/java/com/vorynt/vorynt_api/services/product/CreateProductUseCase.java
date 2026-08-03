package com.vorynt.vorynt_api.services.product;

import com.vorynt.vorynt_api.domain.exceptions.ProductAlreadyExistsException;
import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.persistence.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;

    @Transactional
    public Product execute(
            String name,
            String description,
            BigDecimal price
    ) {
        String normalizedName = name.trim().toLowerCase();

        if(productRepository.existsByNameIgnoreCase(normalizedName))
            throw new ProductAlreadyExistsException(name);

        Product product = Product.create(
                name,
                description,
                price
        );

        return productRepository.save(product);
    }
}
