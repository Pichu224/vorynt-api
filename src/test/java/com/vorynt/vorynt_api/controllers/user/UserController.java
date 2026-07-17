package com.vorynt.vorynt_api.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.dtos.user.CreateUserRequest;
import com.vorynt.vorynt_api.services.user.CreateUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateUserUseCase createUserUseCase;

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
    }

}