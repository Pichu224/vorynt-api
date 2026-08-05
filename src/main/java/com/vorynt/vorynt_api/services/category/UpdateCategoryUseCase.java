package com.vorynt.vorynt_api.services.category;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.*;
import com.vorynt.vorynt_api.persistence.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UpdateCategoryUseCase {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category execute(
            Long id,
            String newName,
            String newDescription
    ) throws CategoryNotFoundException, CategoryAlreadyExistsException, RequiredFieldException {

        if(newName == null)
            throw new RequiredFieldException("name");

        String trimmedName = newName.trim().toLowerCase();

        Category category = categoryRepository.findByIdAndEnabledTrue(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if(!category.getName().toLowerCase().equals(trimmedName) &&
                categoryRepository.existsByNameIgnoreCase(trimmedName))
            throw new CategoryAlreadyExistsException(trimmedName);

        category.changeName(newName);
        category.changeDescription(newDescription);

        return category;
    }
}
