package com.vorynt.vorynt_api.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorynt.vorynt_api.domain.exceptions.EmailAlreadyExistsException;
import com.vorynt.vorynt_api.domain.exceptions.UserNotFoundException;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.dtos.user.CreateUserRequest;
import com.vorynt.vorynt_api.dtos.user.UpdateUserRequest;
import com.vorynt.vorynt_api.dtos.user.UserResponse;
import com.vorynt.vorynt_api.mappers.UserMapper;
import com.vorynt.vorynt_api.services.user.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private UpdateUserUseCase updateUserUseCase;

    @MockBean
    private DeleteUserUseCase deleteUserUseCase;

    @MockBean
    private GetAllUsersUseCase getAllUsersUseCase;

    @MockBean
    private GetUserByIdUseCase getUserByIdUseCase;

    @Test
    void shouldCreateUserSuccessfully() throws Exception {

        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "Alan",
                "acuna",
                "alan@gmail.com",
                "123456"
        );

        User user = User.create(
                "Alan",
                "acuna",
                Email.of("alan@gmail.com"),
                "123456"
        );

        when(userMapper.toResponse(any(User.class)))
                .thenReturn(new UserResponse(
                        1L,
                        "Alan",
                        "acuna",
                        "alan@gmail.com"
                ));

        when(createUserUseCase.execute(
                anyString(),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(user);

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Alan"))
                .andExpect(jsonPath("$.lastName").value("acuna"))
                .andExpect(jsonPath("$.email").value("alan@gmail.com"));

        verify(createUserUseCase, times(1))
                .execute(
                        "Alan",
                        "acuna",
                        "alan@gmail.com",
                        "123456"
                );

        verify(userMapper, times(1))
                .toResponse(user);
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {

        // Arrange

        CreateUserRequest request = new CreateUserRequest(
                "Alan",
                "acuna",
                "alan@gmail.com",
                "123456"
        );

        when(createUserUseCase.execute(
                anyString(),
                anyString(),
                anyString(),
                anyString()
        )).thenThrow(new EmailAlreadyExistsException(
                Email.of(request.email()))
        );

        // Act & Assert

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message")
                        .value("Email 'alan@gmail.com' is already registered."));

        verify(createUserUseCase).execute(
                "Alan",
                "acuna",
                "alan@gmail.com",
                "123456"
        );

        verifyNoInteractions(userMapper);
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {

        // Arrange
        UpdateUserRequest request = new UpdateUserRequest(
                "Alan",
                "acuna"
        );

        User user = User.create(
                "Juan",
                "Sultana",
                Email.of("alan@gmail.com"),
                "123456"
        );

        when(userMapper.toResponse(any(User.class)))
                .thenReturn(new UserResponse(
                        1L,
                        "Alan",
                        "acuna",
                        "alan@gmail.com"
                ));

        when(updateUserUseCase.execute(
                anyLong(),
                anyString(),
                anyString()
        )).thenReturn(user);

        // Act & Assert
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alan"))
                .andExpect(jsonPath("$.lastName").value("acuna"))
                .andExpect(jsonPath("$.email").value("alan@gmail.com"));

        verify(updateUserUseCase, times(1))
                .execute(
                        1L,
                        "Alan",
                        "acuna"
                );

        verify(userMapper, times(1))
                .toResponse(user);
    }

    @Test
    void shouldReturnNotFoundWhenDoNotExistUserWhenUpdatingIt() throws Exception {

        // Arrange

        UpdateUserRequest request = new UpdateUserRequest(
                "Alan",
                "acuna"
        );

        when(updateUserUseCase.execute(
                anyLong(),
                anyString(),
                anyString()
        )).thenThrow(new UserNotFoundException(
                1L
        ));

        // Act & Assert

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message")
                        .value("Don't exist an User with id: 1"));

        verify(updateUserUseCase).execute(
                1L,
                "Alan",
                "acuna"
        );

        verifyNoInteractions(userMapper);
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {

        // Act & Assert
        mockMvc.perform(delete("/users/1"))

                .andExpect(status().isNoContent());

        verify(deleteUserUseCase, times(1))
                .execute(
                        1L
                );
    }

    @Test
    void shouldReturnNotFoundWhenDoNotExistUserWhenDeletingIt() throws Exception {

        // Arrange

        doThrow(new UserNotFoundException(1L))
                .when(deleteUserUseCase)
                .execute(anyLong());

        // Act & Assert

        mockMvc.perform(delete("/users/1"))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message")
                        .value("Don't exist an User with id: 1"));

        verify(deleteUserUseCase).execute(
                1L
        );

        verifyNoInteractions(userMapper);
    }

    @Test
    void shouldGetUserByIdSuccessfully() throws Exception {

        // Arrange

        User user = User.create(
                "Alan",
                "acuna",
                Email.of("alan@gmail.com"),
                "123456"
        );

        when(userMapper.toResponse(any(User.class)))
                .thenReturn(new UserResponse(
                        1L,
                        "Alan",
                        "acuna",
                        "alan@gmail.com"
                ));

        when(getUserByIdUseCase.execute(
                anyLong()
        )).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alan"))
                .andExpect(jsonPath("$.lastName").value("acuna"))
                .andExpect(jsonPath("$.email").value("alan@gmail.com"));

        verify(getUserByIdUseCase, times(1))
                .execute(
                        1L
                );

        verify(userMapper, times(1))
                .toResponse(user);
    }

    @Test
    void shouldReturnNotFoundWhenDoNotExistUserWhenGettingIt() throws Exception {

        // Arrange

        when(getUserByIdUseCase.execute(
                anyLong()
        )).thenThrow(new UserNotFoundException(
                1L
        ));

        // Act & Assert

        mockMvc.perform(get("/users/1"))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message")
                        .value("Don't exist an User with id: 1"));

        verify(getUserByIdUseCase).execute(
                1L
        );

        verifyNoInteractions(userMapper);
    }

    @Test
    void shouldGetUsersSuccessfully() throws Exception {

        // Arrange

        List<User> users = List.of(
                User.create("Alan", "acuna", Email.of("alan@gmail.com"), "hash"),
                User.create("Juan", "Perez", Email.of("juan@gmail.com"), "hash")
        );

        List<UserResponse> userResponses = List.of(
                new UserResponse(
                        1L,
                        "Alan",
                        "acuna",
                        "alan@gmail.com"
                ),
                new UserResponse(
                        2L,
                        "Juan",
                        "Perez",
                        "juan@gmail.com"
                )
        );

        when(userMapper.toResponseList(anyList()))
                .thenReturn(userResponses);

        when(getAllUsersUseCase.execute())
                .thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName").value("Alan"))
                .andExpect(jsonPath("$.[0].lastName").value("acuna"))
                .andExpect(jsonPath("$.[0].email").value("alan@gmail.com"))

                .andExpect(jsonPath("$.[1].firstName").value("Juan"))
                .andExpect(jsonPath("$.[1].lastName").value("Perez"))
                .andExpect(jsonPath("$.[1].email").value("juan@gmail.com"));


        verify(getAllUsersUseCase, times(1))
                .execute();

        verify(userMapper, times(1))
                .toResponseList(users);
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoUsers() throws Exception {

        // Arrange

        when(getAllUsersUseCase.execute())
                .thenReturn(List.of());

        when(userMapper.toResponseList(anyList()))
                .thenReturn(List.of());

        // Act & Assert

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(getAllUsersUseCase).execute();

        verify(userMapper).toResponseList(anyList());
    }

    @Test
    void shouldReturnInternalServerErrorWhenUnexpectedExceptionOccurs() throws Exception {

        // Arrange

        when(getUserByIdUseCase.execute(anyLong()))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred."));

        verify(getUserByIdUseCase).execute(1L);

        verifyNoInteractions(userMapper);
    }

}