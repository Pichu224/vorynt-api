package com.vorynt.vorynt_api.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @NotBlank(message = "first name is required.")
        String firstName,

        @NotBlank(message = "last name is required.")
        String lastName,

        @NotBlank(message = "email is required.")
        @Email(message = "email must be valid.")
        String email,

        @NotBlank(message = "password is required.")
        @Size(min = 4, message = "password must contain at least 4 characters.")
        String password
) {}
