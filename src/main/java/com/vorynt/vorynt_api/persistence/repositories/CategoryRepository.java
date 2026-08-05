package com.vorynt.vorynt_api.persistence.repositories;

import com.vorynt.vorynt_api.domain.category.Category;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    boolean existsByNameIgnoreCase(String name);

    List<Category> findAllByEnabledTrue();

    Optional<Category> findByIdAndEnabledTrue(Long id);
}
