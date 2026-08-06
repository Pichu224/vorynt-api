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
public class UpdateProductUseCase {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Product execute(
            Long id,
            String newName,
            String newDescription,
            BigDecimal newPrice
    ) throws ProductNotFoundException, ProductAlreadyExistsException, RequiredFieldException {

        if(newName == null)
            throw new RequiredFieldException("name");

        Product product = productRepository.findByIdAndEnabledTrue(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        boolean sameNames = product.getName().equalsIgnoreCase(newName.trim());

        if(!sameNames && productRepository.existsByNameIgnoreCase(newName.trim()))
            throw new ProductAlreadyExistsException(newName.trim());

        product.changeName(newName);
        product.changeDescription(newDescription);
        product.changePrice(newPrice);

        return product;
    }
}
