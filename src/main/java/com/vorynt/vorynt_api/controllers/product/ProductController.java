package com.vorynt.vorynt_api.controllers.product;

import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.dtos.product.CreateProductRequest;
import com.vorynt.vorynt_api.dtos.product.ProductResponse;
import com.vorynt.vorynt_api.dtos.product.UpdateProductRequest;
import com.vorynt.vorynt_api.mappers.ProductMapper;
import com.vorynt.vorynt_api.services.product.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final ProductMapper productMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> create(
        @Valid @RequestBody CreateProductRequest request
    ) {
        Product product = createProductUseCase.execute(
                request.name(),
                request.description(),
                request.price(),
                request.category()
        );

        ProductResponse response = productMapper.toResponse(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateById(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        Product product = updateProductUseCase.execute(
                id,
                request.name(),
                request.description(),
                request.price(),
                request.category()
        );

        ProductResponse response = productMapper.toResponse(product);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {
        deleteProductUseCase.execute(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        List<Product> products = getAllProductsUseCase.execute();

        List<ProductResponse> responses = productMapper.toResponseList(products);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(
            @PathVariable Long id
    ) {
        Product product = getProductByIdUseCase.execute(id);

        ProductResponse response = productMapper.toResponse(product);

        return ResponseEntity.ok(response);
    }
}
