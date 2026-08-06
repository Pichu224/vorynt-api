package com.vorynt.vorynt_api.services.product;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.CategoryNotFoundException;
import com.vorynt.vorynt_api.domain.exceptions.ProductAlreadyExistsException;
import com.vorynt.vorynt_api.domain.exceptions.ProductNotFoundException;
import com.vorynt.vorynt_api.domain.exceptions.RequiredFieldException;
import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.persistence.repositories.CategoryRepository;
import com.vorynt.vorynt_api.persistence.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Product execute(
            String name,
            String description,
            BigDecimal price,
            Long categoryId
    ) throws ProductNotFoundException, ProductAlreadyExistsException, RequiredFieldException, CategoryNotFoundException {
        if(name == null)
            throw new RequiredFieldException("name");

        String normalizedName = name.trim().toLowerCase();

        if(productRepository.existsByNameIgnoreCase(normalizedName))
            throw new ProductAlreadyExistsException(name);

        Category category = (categoryId == null) ? null :
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        Product product = Product.create(
                name,
                description,
                price,
                category
        );

        return productRepository.save(product);
    }
}
