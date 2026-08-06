package com.vorynt.vorynt_api.persistence.repositories;

import com.vorynt.vorynt_api.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameIgnoreCase(String name);

    List<Product> findAllByEnabledTrue();

    Optional<Product> findByIdAndEnabledTrue(Long id);
}