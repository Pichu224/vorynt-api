package com.vorynt.vorynt_api.services.product;

import com.vorynt.vorynt_api.domain.exceptions.ProductNotFoundException;
import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.persistence.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DeleteProductUseCase {

    private final ProductRepository productRepository;

    @Transactional
    public void execute(Long id) throws ProductNotFoundException {
        Product product = productRepository.findByIdAndEnabledTrue(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.deactivate();
    }
}
