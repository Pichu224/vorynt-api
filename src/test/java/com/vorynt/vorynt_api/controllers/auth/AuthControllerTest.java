package com.vorynt.vorynt_api.controllers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorynt.vorynt_api.domain.user.User;
import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import com.vorynt.vorynt_api.dtos.auth.LoginRequest;
import com.vorynt.vorynt_api.dtos.auth.RegisterRequest;
import com.vorynt.vorynt_api.handlers.GlobalExceptionHandler;
import com.vorynt.vorynt_api.security.JwtAuthenticationEntryPoint;
import com.vorynt.vorynt_api.security.JwtAuthenticationFilter;
import com.vorynt.vorynt_api.services.auth.LoginUseCase;
import com.vorynt.vorynt_api.services.auth.RegisterUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginUseCase loginUseCase;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    void shouldLoginSuccessfully() throws Exception {

        LoginRequest request = new LoginRequest(
                "alan@gmail.com",
                "123456"
        );

        when(loginUseCase.execute(
                request.email(),
                request.password()
        )).thenReturn("jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));

        verify(loginUseCase).execute(
                request.email(),
                request.password()
        );
    }

    @Test
    void shouldReturnBadRequestWhenLoginRequestIsInvalid() throws Exception {

        LoginRequest request = new LoginRequest(
                "",
                ""
        );

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));

        verifyNoInteractions(loginUseCase);
    }

    @Test
    void shouldRegisterSuccessfully() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "Alan",
                "Acuna",
                "alan@gmail.com",
                "123456"
        );

        User user = User.create(
                "Alan",
                "Acuna",
                Email.of("alan@gmail.com"),
                "hash"
        );

        when(registerUserUseCase.execute(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password()
        )).thenReturn(user);

        when(loginUseCase.execute(
                request.email(),
                request.password()
        )).thenReturn("jwt-token");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));

        verify(registerUserUseCase).execute(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password()
        );

        verify(loginUseCase).execute(
                request.email(),
                request.password()
        );
    }

    @Test
    void shouldReturnBadRequestWhenRegisterRequestIsInvalid() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "",
                "",
                "",
                ""
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));

        verifyNoInteractions(registerUserUseCase);
        verifyNoInteractions(loginUseCase);
    }
}