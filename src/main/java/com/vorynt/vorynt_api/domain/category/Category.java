package com.vorynt.vorynt_api.domain.category;

import com.vorynt.vorynt_api.domain.exceptions.ProductInvalidException;
import com.vorynt.vorynt_api.domain.exceptions.RequiredFieldException;
import com.vorynt.vorynt_api.domain.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(
            mappedBy = "category",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            fetch = FetchType.LAZY
    )
    private List<Product> products;

    private Category(
            String name,
            String description,
            List<Product> products
    ) {
        validateRequired(name, "name");

        this.name = normalizeText(name);
        this.description = normalizeText(description);
        initializeProducts(products);

        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
        enabled = true;
    }

    public static Category create(
            String name,
            String description,
            List<Product> products
    ) {
        return new Category(name, description, products);
    }

    private String normalizeText(String text) {
        if(text == null) return null;
        return text.trim();
    }

    private void validateRequired(String value, String field) {
        if (value == null || value.isBlank())
            throw new RequiredFieldException(field);
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

    private void initializeProducts(List<Product> newProducts) {
        products = new ArrayList<>();

        if(newProducts != null)
            newProducts.forEach(this::addProduct);
    }

    public void changeProducts(List<Product> newProducts) {
        removeAllProducts();

        if (newProducts != null)
            newProducts.forEach(this::addProduct);
    }

    public void addProduct(Product newProduct) {
        if(newProduct == null)
            throw new ProductInvalidException();

        if (products.contains(newProduct))
            return;

        if (newProduct.hasCategory())
            newProduct.getCategory().removeProduct(newProduct);

        products.add(newProduct);
        newProduct.setCategory(this);
        touch();
    }

    public void removeProduct(Product product) {
        if(product == null)
            throw new ProductInvalidException();

        if(products.remove(product)) {
            product.setCategory(null);
            touch();
        }
    }

    private void removeAllProducts() {
        List.copyOf(products).forEach(this::removeProduct);
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
