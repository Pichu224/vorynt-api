package com.vorynt.vorynt_api.dtos.user;

public record UserResponse(

        Long id,
        String firstName,
        String lastName,
        String email
) {}