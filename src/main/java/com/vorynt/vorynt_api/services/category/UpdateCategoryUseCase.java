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

        Category category = categoryRepository.findByIdAndEnabledTrue(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        boolean sameNames = category.getName().equalsIgnoreCase(newName.trim());

        if(!sameNames && categoryRepository.existsByNameIgnoreCase(newName.trim()))
            throw new CategoryAlreadyExistsException(newName.trim());

        category.changeName(newName);
        category.changeDescription(newDescription);

        return category;
    }
}
