package com.vorynt.vorynt_api.domain.user.valueObjects;

import com.vorynt.vorynt_api.domain.exceptions.InvalidEmailException;
import lombok.Getter;
import java.util.regex.Pattern;

public final class Email {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final int MAX_LENGTH = 254;

    @Getter
    private final String value;

    private Email(String email) {
        String normalizedEmail = normalize(email);

        validate(normalizedEmail);

        this.value = normalizedEmail;
    }

    public static Email of(String email) {
        return new Email(email);
    }

    private static String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private static void validate(String email) {
        validateRequired(email);
        validateLength(email);
        validateFormat(email);
    }

    private static void validateRequired(String email) {
        if (email == null)
            throw new InvalidEmailException("Email cannot be null.");
        if (email.isBlank())
            throw new InvalidEmailException("Email cannot be blank.");
    }

    private static void validateLength(String email) {
        if (email.length() > Email.MAX_LENGTH)
            throw new InvalidEmailException("Email is too long.");
    }

    private static void validateFormat(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches())
            throw new InvalidEmailException("Invalid email format.");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Email other)) return false;

        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}