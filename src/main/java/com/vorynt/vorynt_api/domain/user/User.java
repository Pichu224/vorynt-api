package com.vorynt.vorynt_api.domain.user;

import com.vorynt.vorynt_api.domain.exceptions.InvalidEmailException;
import com.vorynt.vorynt_api.domain.exceptions.RequiredFieldException;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.persistence.converters.EmailConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    @Convert(converter = EmailConverter.class)
    private Email email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    private User(
            String firstName,
            String lastName,
            Email email,
            String passwordHash
    ) {
        validateRequired(firstName, "firstName");
        validateRequired(lastName, "lastName");
        validateRequired(passwordHash, "passwordHash");

        if (email == null)
            throw new InvalidEmailException("Email cannot be null.");

        this.firstName = normalizeName(firstName);
        this.lastName = normalizeName(lastName);
        this.email = email;
        this.passwordHash = passwordHash;

        this.role = Role.CLIENT;
        this.enabled = true;

        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public static User create(
            String firstName,
            String lastName,
            Email email,
            String passwordHash
    ) {
        return new User(firstName, lastName, email, passwordHash);
    }

    public void changeFirstName(String firstName) {
        validateRequired(firstName, "firstName");
        this.firstName = normalizeName(firstName);
        touch();
    }

    public void changeLastName(String lastName) {
        validateRequired(lastName, "lastName");
        this.lastName = normalizeName(lastName);
        touch();
    }

    public void changeEmail(Email email) {
        if (email == null)
            throw new InvalidEmailException("Email cannot be null.");

        this.email = email;
        touch();
    }

    public void changePasswordHash(String passwordHash) {
        validateRequired(passwordHash, "passwordHash");
        this.passwordHash = passwordHash;
        touch();
    }

    public void promoteToAdmin() {
        this.role = Role.ADMIN;
        touch();
    }

    public void demoteToClient() {
        this.role = Role.CLIENT;
        touch();
    }

    public void activate() {
        this.enabled = true;
        touch();
    }

    public void deactivate() {
        this.enabled = false;
        touch();
    }

    private void touch() {
        this.updatedAt = OffsetDateTime.now();
    }

    private String normalizeName(String name) {
        return name.trim();
    }

    private void validateRequired(String value, String field) {
        if (value == null || value.isBlank())
            throw new RequiredFieldException(field);
    }
}