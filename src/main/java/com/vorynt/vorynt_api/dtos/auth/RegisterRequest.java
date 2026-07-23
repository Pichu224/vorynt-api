package com.vorynt.vorynt_api.dtos.auth;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {}
