package com.vorynt.vorynt_api.domain.category;

import com.vorynt.vorynt_api.domain.exceptions.RequiredFieldException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

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

    private Category(
            String name,
            String description
    ) {
        validateRequired(name, "name");

        this.name = normalizeText(name);
        this.description = description == null ? null : normalizeText(description);

        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
        enabled = true;
    }

    public static Category create(
            String name,
            String description
    ) {
        return new Category(name, description);
    }

    private String normalizeText(String text) {
        return text.trim();
    }

    private void validateRequired(String value, String field) {
        if (value == null || value.isBlank())
            throw new RequiredFieldException(field);
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
