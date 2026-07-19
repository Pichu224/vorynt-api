package com.vorynt.vorynt_api.mappers;

import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.dtos.user.UserResponse;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    private void assertUserResponse(
            UserResponse response,
            String firstName,
            String lastName,
            String email
    ) {
        assertNull(response.id());
        assertEquals(firstName, response.firstName());
        assertEquals(lastName, response.lastName());
        assertEquals(email, response.email());
    }

    @Test
    void shouldMapUserToUserResponse() {

        // Arrange

        User user = User.create(
                "Alan",
                "acuna",
                Email.of("alan@gmail.com"),
                "123456"
        );

        // Act

        UserResponse userResponse = mapper.toResponse(user);

        // Assert

        assertUserResponse(
                userResponse,
                "Alan",
                "acuna",
                "alan@gmail.com"
        );
    }

    @Test
    void shouldMapUsersToResponseList() {

        // Arrange

        List<User> users = List.of(
                User.create("Alan", "acuna", Email.of("alan@gmail.com"), "hash"),
                User.create("Juan", "Perez", Email.of("juan@gmail.com"), "hash")
        );

        // Act

        List<UserResponse> usersResponse = mapper.toResponseList(users);

        // Assert

        assertEquals(2, usersResponse.size());

        assertUserResponse(
                usersResponse.getFirst(),
                "Alan",
                "acuna",
                "alan@gmail.com"
        );

        assertUserResponse(
                usersResponse.get(1),
                "Juan",
                "Perez",
                "juan@gmail.com"
        );
    }
}
