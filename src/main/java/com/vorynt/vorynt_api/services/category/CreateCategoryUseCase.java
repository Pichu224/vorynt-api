package com.vorynt.vorynt_api.services.category;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.*;
import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.persistence.repositories.CategoryRepository;
import com.vorynt.vorynt_api.persistence.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category execute(
            String name,
            String description
    ) throws CategoryNotFoundException, CategoryAlreadyExistsException, RequiredFieldException {
        if(name == null)
            throw new RequiredFieldException("name");

        if(categoryRepository.existsByNameIgnoreCase(name.trim()))
            throw new CategoryAlreadyExistsException(name);

        Category category = Category.create(
                name,
                description,
                List.of()
        );

        return categoryRepository.save(category);
    }
}
