package com.vorynt.vorynt_api.domain.product;

import com.vorynt.vorynt_api.domain.category.Category;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private Product(
            String name,
            String description,
            BigDecimal price,
            Category category
    ) {
        validateRequired(name, "name");
        validatePrice(price);

        this.name = normalizeText(name);
        this.description = normalizeText(description);
        this.price = price;
        this.category = category;

        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
        enabled = true;
    }

    public static Product create(
        String name,
        String description,
        BigDecimal price,
        Category category
    ) {
        return new Product(name, description, price, category);
    }

    private String normalizeText(String text) {
        if(text == null) return null;
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
        name = normalizeText(newName);
        touch();
    }

    public void changeDescription(String newDescription) {
        validateRequired(newDescription, "description");
        description = normalizeText(newDescription);
        touch();
    }

    public void changePrice(BigDecimal newPrice) {
        validatePrice(newPrice);
        price = newPrice;
        touch();
    }

    public void setCategory(Category newCategory) {
        category = newCategory;
        touch();
    }

    public boolean hasCategory() {
        return category != null;
    }

    public void deactivate() {
        if(!enabled) return;
        enabled = false;
        touch();
    }

    public void activate() {
        if(enabled) return;
        enabled = true;
        touch();
    }

    private void touch() {
        updatedAt = OffsetDateTime.now();
    }
}
