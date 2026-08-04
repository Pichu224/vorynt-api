package com.vorynt.vorynt_api.domain.product;

import com.vorynt.vorynt_api.domain.exceptions.InvalidPriceException;
import com.vorynt.vorynt_api.domain.exceptions.RequiredFieldException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    private Product(
            String name,
            String description,
            BigDecimal price
    ) {
        validateRequired(name, "name");
        validatePrice(price);

        this.name = normalizeText(name);
        this.description = description == null ? null : normalizeText(description);
        this.price = price;

        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
        enabled = true;
    }

    public static Product create(
        String name,
        String description,
        BigDecimal price
    ) {
        return new Product(name, description, price);
    }

    private String normalizeText(String text) {
        return text.trim();
    }

    private void validateRequired(String value, String field) {
        if (value == null || value.isBlank())
            throw new RequiredFieldException(field);
    }

    private void validatePrice(BigDecimal price) {
        if (price == null)
            throw new RequiredFieldException("price");
        if (price.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidPriceException();
    }

    public void changeName(String newName) {
        validateRequired(newName, "name");
        this.name = normalizeText(newName);
        touch();
    }

    public void changeDescription(String newDescription) {
        validateRequired(newDescription, "description");
        this.description = normalizeText(newDescription);
        touch();
    }

    public void changePrice(BigDecimal newPrice) {
        validatePrice(newPrice);
        this.price = newPrice;
        touch();
    }

    public void deactivate() {
        if(!enabled) return;
        this.enabled = false;
        touch();
    }

    public void activate() {
        if(enabled) return;
        this.enabled = true;
        touch();
    }

    private void touch() {
        this.updatedAt = OffsetDateTime.now();
    }
}
