package com.vorynt.vorynt_api.dtos.user;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(

        @NotBlank(message = "first name is required.")
        String firstName,

        @NotBlank(message = "last name is required.")
        String lastName
) {}
