package com.vorynt.vorynt_api.controllers.category;

import com.vorynt.vorynt_api.domain.category.Category;
import com.vorynt.vorynt_api.dtos.Category.CategoryResponse;
import com.vorynt.vorynt_api.dtos.Category.CreateCategoryRequest;
import com.vorynt.vorynt_api.dtos.Category.UpdateCategoryRequest;
import com.vorynt.vorynt_api.mappers.CategoryMapper;
import com.vorynt.vorynt_api.services.category.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final GetAllCategoriesUseCase getAllCategoriesUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final CategoryMapper categoryMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> create(
            @Valid @RequestBody CreateCategoryRequest request
    ) {
        Category category = createCategoryUseCase.execute(
                request.name(),
                request.description()
        );

        CategoryResponse response = categoryMapper.toResponse(category);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateById(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        Category category = updateCategoryUseCase.execute(
                id,
                request.name(),
                request.description()
        );

        CategoryResponse response = categoryMapper.toResponse(category);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {
        deleteCategoryUseCase.execute(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        List<Category> categories = getAllCategoriesUseCase.execute();

        List<CategoryResponse> responses = categoryMapper.toResponseList(categories);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(
            @PathVariable Long id
    ) {
        Category category = getCategoryByIdUseCase.execute(id);

        CategoryResponse response = categoryMapper.toResponse(category);

        return ResponseEntity.ok(response);
    }
}
