package com.vorynt.vorynt_api.persistence.repositories;

import com.vorynt.vorynt_api.domain.category.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

    boolean existsByNameIgnoreCase(String name);

    List<Category> findAllByEnabledTrue();

    Optional<Category> findByIdAndEnabledTrue(Long id);

    boolean existsProductsById(Long id);
}
