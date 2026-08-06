package com.vorynt.vorynt_api.mappers;

import com.vorynt.vorynt_api.domain.product.Product;
import com.vorynt.vorynt_api.dtos.product.ProductResponse;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public final class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory()
        );
    }

    public List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream().map(this::toResponse).toList();
    }
}
