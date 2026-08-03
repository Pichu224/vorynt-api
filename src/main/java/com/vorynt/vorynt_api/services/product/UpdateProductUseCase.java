package com.vorynt.vorynt_api.services.product;

import com.vorynt.vorynt_api.domain.exceptions.ProductAlreadyExistsException;
import com.vorynt.vorynt_api.domain.exceptions.ProductNotFoundException;
import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.persistence.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class UpdateProductUseCase {

    private final ProductRepository productRepository;

    @Transactional
    public Product execute(
            Long id,
            String newName,
            String newDescription,
            BigDecimal newPrice
    ) throws ProductNotFoundException, ProductAlreadyExistsException {

        String trimmedName = newName.trim().toLowerCase();

        Product product = productRepository.findByIdAndEnabledTrue(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if(!product.getName().toLowerCase().equals(trimmedName) &&
                productRepository.existsByNameIgnoreCase(trimmedName))
            throw new ProductAlreadyExistsException(trimmedName);


        product.changeName(newName);
        product.changeDescription(newDescription);
        product.changePrice(newPrice);

        return product;
    }
}
