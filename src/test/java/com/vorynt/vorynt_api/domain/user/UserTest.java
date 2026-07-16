package com.vorynt.vorynt_api.domain.user;

import com.vorynt.vorynt_api.domain.exceptions.RequiredFieldException;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateClientUser() {
        // Arrange
        Email email = Email.of("alan@gmail.com");

        // Act
        User user = User.create(
                "Alan",
                "Acuna",
                email,
                "hashedPassword"
        );

        // Assert
        assertAll(
                () -> assertEquals("Alan", user.getFirstName()),
                () -> assertEquals("Acuna", user.getLastName()),
                () -> assertEquals(email, user.getEmail()),
                () -> assertEquals("hashedPassword", user.getPasswordHash()),
                () -> assertEquals(Role.CLIENT, user.getRole()),
                () -> assertTrue(user.isEnabled()),
                () -> assertNotNull(user.getCreatedAt())
        );
    }

    @Test
    void shouldNormalizeNamesWhenCreatingUser() {
        // Arrange

        // Act
        User user = User.create(
                "  Alan ",
                " Acuna   ",
                Email.of("alan@gmail.com"),
                "hashedPassword"
        );

        // Assert
        assertAll(
                () -> assertEquals("Alan", user.getFirstName()),
                () -> assertEquals("Acuna", user.getLastName())
        );
    }

    @Test
    void shouldChangeFirstName() {
        // Arrange
        User user = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "hashedPassword"
        );

        // Act
        user.changeFirstName("Juan");

        // Assert
        assertEquals("Juan", user.getFirstName());
    }

    @Test
    void shouldChangeLastName() {
        // Arrange
        User user = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "hashedPassword"
        );

        // Act
        user.changeLastName("Perez");

        // Assert
        assertEquals("Perez", user.getLastName());
    }

    @Test
    void shouldChangeEmail() {
        // Arrange
        User user = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "hashedPassword"
        );

        Email newEmail = Email.of("juan@gmail.com");

        // Act
        user.changeEmail(newEmail);

        // Assert
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    void shouldChangePasswordHash() {
        // Arrange
        User user = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "hashedPassword"
        );

        // Act
        user.changePasswordHash("newHash");

        // Assert
        assertEquals("newHash", user.getPasswordHash());
    }

    @Test
    void shouldPromoteUserToAdmin() {
        // Arrange
        User user = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "hashedPassword"
        );

        // Act
        user.promoteToAdmin();

        // Assert
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void shouldDemoteAdminToClient() {
        // Arrange
        User user = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "hashedPassword"
        );

        user.promoteToAdmin();

        // Act
        user.demoteToClient();

        // Assert
        assertEquals(Role.CLIENT, user.getRole());
    }

    @Test
    void shouldActivateUser() {
        // Arrange
        User user = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "hashedPassword"
        );

        user.deactivate();

        // Act
        user.activate();

        // Assert
        assertTrue(user.isEnabled());
    }

    @Test
    void shouldDeactivateUser() {
        // Arrange
        User user = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "hashedPassword"
        );

        // Act
        user.deactivate();

        // Assert
        assertFalse(user.isEnabled());
    }

    @Test
    void shouldRejectBlankFirstName() {
        assertThrows(
                RequiredFieldException.class,
                () -> User.create(
                        "",
                        "Acuna",
                        Email.of("alan@gmail.com"),
                        "hashedPassword"
                )
        );
    }

    @Test
    void shouldRejectBlankLastName() {
        assertThrows(
                RequiredFieldException.class,
                () -> User.create(
                        "Alan",
                        "",
                        Email.of("alan@gmail.com"),
                        "hashedPassword"
                )
        );
    }

    @Test
    void shouldRejectNullPasswordHash() {
        assertThrows(
                RequiredFieldException.class,
                () -> User.create(
                        "Alan",
                        "Acuna",
                        Email.of("alan@gmail.com"),
                        null
                )
        );
    }
}
