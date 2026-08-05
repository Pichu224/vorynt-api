package com.vorynt.vorynt_api.services.category;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.CategoryNotFoundException;
import com.vorynt.vorynt_api.persistence.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetCategoryByIdUseCase {

    private final CategoryRepository categoryRepository;

    public Category execute(Long id) throws CategoryNotFoundException {
        return categoryRepository.findByIdAndEnabledTrue(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
