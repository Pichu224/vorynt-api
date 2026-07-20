package com.vorynt.vorynt_api.domain.user;

import com.vorynt.vorynt_api.domain.exceptions.InvalidEmailException;
import com.vorynt.vorynt_api.domain.exceptions.RequiredFieldException;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
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
                () -> assertNotNull(user.getCreatedAt()),
                () -> assertNotNull(user.getUpdatedAt())
        );
    }

    @Test
    void shouldNormalizeNamesWhenCreatingUser() {

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
        User user = createUser();

        // Act
        user.changeFirstName("Juan");

        // Assert
        assertEquals("Juan", user.getFirstName());
    }

    @Test
    void shouldNormalizeFirstNameWhenChangingIt() {

        // Arrange
        User user = createUser();

        // Act
        user.changeFirstName("   Juan   ");

        // Assert
        assertEquals("Juan", user.getFirstName());
    }

    @Test
    void shouldChangeLastName() {

        // Arrange
        User user = createUser();

        // Act
        user.changeLastName("Perez");

        // Assert
        assertEquals("Perez", user.getLastName());
    }

    @Test
    void shouldChangeEmail() {

        // Arrange
        User user = createUser();

        Email newEmail = Email.of("juan@gmail.com");

        // Act
        user.changeEmail(newEmail);

        // Assert
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    void shouldChangePasswordHash() {

        // Arrange
        User user = createUser();

        // Act
        user.changePasswordHash("newHash");

        // Assert
        assertEquals("newHash", user.getPasswordHash());
    }

    @Test
    void shouldPromoteUserToAdmin() {

        // Arrange
        User user = createUser();

        // Act
        user.promoteToAdmin();

        // Assert
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void shouldDemoteAdminToClient() {

        // Arrange
        User user = createUser();

        user.promoteToAdmin();

        // Act
        user.demoteToClient();

        // Assert
        assertEquals(Role.CLIENT, user.getRole());
    }

    @Test
    void shouldActivateUser() {

        // Arrange
        User user = createUser();

        user.deactivate();

        // Act
        user.activate();

        // Assert
        assertTrue(user.isEnabled());
    }

    @Test
    void shouldDeactivateUser() {

        // Arrange
        User user = createUser();

        // Act
        user.deactivate();

        // Assert
        assertFalse(user.isEnabled());
    }

    @Test
    void shouldUpdateUpdatedAtWhenChangingFirstName() throws InterruptedException {

        // Arrange
        User user = createUser();

        OffsetDateTime previousUpdatedAt = user.getUpdatedAt();

        Thread.sleep(5);

        // Act
        user.changeFirstName("Juan");

        // Assert
        assertTrue(user.getUpdatedAt().isAfter(previousUpdatedAt));
    }

    @Test
    void shouldUpdateUpdatedAtWhenDeactivatingUser() throws InterruptedException {

        // Arrange
        User user = createUser();

        OffsetDateTime previousUpdatedAt = user.getUpdatedAt();

        Thread.sleep(5);

        // Act
        user.deactivate();

        // Assert
        assertTrue(user.getUpdatedAt().isAfter(previousUpdatedAt));
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

    @Test
    void shouldRejectNullEmail() {

        assertThrows(
                InvalidEmailException.class,
                () -> User.create(
                        "Alan",
                        "Acuna",
                        null,
                        "hashedPassword"
                )
        );
    }

    private User createUser() {
        return User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "hashedPassword"
        );
    }
}