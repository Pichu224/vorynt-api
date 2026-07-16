package com.vorynt.vorynt_api.domain.user;

import com.vorynt.vorynt_api.domain.exceptions.InvalidEmailException;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void shouldNormalizeEmail() {
        // Arrange
        Email result = Email.of("  JUANCARLOS@gmail.com ");

        // Assert
        assertEquals("juancarlos@gmail.com", result.getValue());
    }

    @Test
    void shouldRejectBlankEmail() {
        // Act & Assert
        assertThrows(
                InvalidEmailException.class,
                () -> Email.of("")
        );
    }

    @Test
    void shouldRejectNullEmail() {
        // Act & Assert
        assertThrows(
                InvalidEmailException.class,
                () -> Email.of(null)
        );
    }

    @Test
    void shouldRejectInvalidFormat() {
        // Act & Assert
        assertAll(
                () -> assertThrows(InvalidEmailException.class, () -> Email.of("alan")),
                () -> assertThrows(InvalidEmailException.class, () -> Email.of("@gmail.com")),
                () -> assertThrows(InvalidEmailException.class, () -> Email.of("alan@")),
                () -> assertThrows(InvalidEmailException.class, () -> Email.of("alan@gmail"))
        );
    }

    @Test
    void shouldRejectTooLongEmail() {
        // Arrange
        String localPart = "a".repeat(245);
        String email = localPart + "@gmail.com";

        // Act & Assert
        assertThrows(
                InvalidEmailException.class,
                () -> Email.of(email)
        );
    }

    @Test
    void shouldCompareEmailsByValue() {
        // Arrange
        Email first = Email.of("Alan@gmail.com");
        Email second = Email.of(" alan@gmail.com ");

        // Assert
        assertEquals(first, second);
    }

    @Test
    void shouldNotBeEqualToDifferentEmails() {
        // Arrange
        Email first = Email.of("alan@gmail.com");
        Email second = Email.of("juan@gmail.com");

        // Assert
        assertNotEquals(first, second);
    }

    @Test
    void shouldReturnSameHashCodeForEqualEmails() {
        // Arrange
        Email first = Email.of("Alan@gmail.com");
        Email second = Email.of(" alan@gmail.com ");

        // Assert
        assertEquals(first.hashCode(), second.hashCode());
    }
}