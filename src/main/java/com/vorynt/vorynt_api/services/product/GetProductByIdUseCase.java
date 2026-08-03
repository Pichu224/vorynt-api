package com.vorynt.vorynt_api.services.product;

import com.vorynt.vorynt_api.domain.exceptions.ProductNotFoundException;
import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.persistence.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetProductByIdUseCase {

    private final ProductRepository productRepository;

    public Product execute(Long id) throws ProductNotFoundException {
        return productRepository.findByIdAndEnabledTrue(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
