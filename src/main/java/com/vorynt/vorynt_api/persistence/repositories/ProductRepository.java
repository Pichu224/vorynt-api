package com.vorynt.vorynt_api.persistence.repositories;

import com.vorynt.vorynt_api.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    boolean existsByNameIgnoreCase(String name);

    List<Product> findAllByEnabledTrue();

    Optional<Product> findByIdAndEnabledTrue(Long id);
}