package com.vorynt.vorynt_api.domain.user;

import com.vorynt.vorynt_api.domain.exceptions.InvalidEmailException;
import com.vorynt.vorynt_api.domain.exceptions.RequiredFieldException;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.persistence.converters.EmailConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor // Requerido por JPA
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
    private LocalDateTime createdAt;

    private User(String firstName,
                String lastName,
                Email email,
                String passwordHash) {

        this.changeFirstName(firstName);
        this.changeLastName(lastName);
        this.changeEmail(email);
        this.changePasswordHash(passwordHash);

        this.role = Role.CLIENT;
        this.enabled = true;
        this.createdAt = LocalDateTime.now();
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
        this.validateRequired(firstName, "firstName");
        this.firstName = this.normalizeName(firstName);
    }

    public void changeLastName(String lastName) {
        this.validateRequired(lastName, "lastName");
        this.lastName = this.normalizeName(lastName);
    }

    public void changeEmail(Email email) {
        if (email == null)
            throw new InvalidEmailException("Email cannot be null.");
        this.email = email;
    }

    public void changePasswordHash(String passwordHash) {
        this.validateRequired(passwordHash, "passwordHash");
        this.passwordHash = passwordHash;
    }

    private String normalizeName(String name) {
        return name.trim();
    }

    public void promoteToAdmin() {
        this.role = Role.ADMIN;
    }

    public void demoteToClient() {
        this.role = Role.CLIENT;
    }

    public void activate() {
        this.enabled = true;
    }

    public void deactivate() {
        this.enabled = false;
    }

    private void validateRequired(String value, String field) {
        if(value == null)
            throw new RequiredFieldException(field);
        if (value.isBlank())
            throw new RequiredFieldException(field);
    }
}