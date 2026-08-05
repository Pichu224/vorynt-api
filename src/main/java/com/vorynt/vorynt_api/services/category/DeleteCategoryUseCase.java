package com.vorynt.vorynt_api.services.category;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.domain.exceptions.CategoryNotFoundException;
import com.vorynt.vorynt_api.persistence.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DeleteCategoryUseCase {

    private CategoryRepository categoryRepository;

    @Transactional
    public void execute(Long id) throws CategoryNotFoundException {
         Category category = categoryRepository.findByIdAndEnabledTrue(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

         category.deactivate();
    }
}
