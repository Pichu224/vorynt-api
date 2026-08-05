package com.vorynt.vorynt_api.services.category;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.*;
import com.vorynt.vorynt_api.persistence.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        String normalizedName = name.trim().toLowerCase();

        if(categoryRepository.existsByNameIgnoreCase(normalizedName))
            throw new CategoryAlreadyExistsException(name);

        Category category = Category.create(
                name,
                description
        );

        return categoryRepository.save(category);
    }
}
