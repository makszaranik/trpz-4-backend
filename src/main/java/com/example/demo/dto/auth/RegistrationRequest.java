package com.example.demo.dto.auth;

import com.example.demo.model.user.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(
        @NotNull @NotBlank String username,
        @NotNull @NotBlank String password,
        @Email String email,
        @NotNull UserEntity.UserRole role
) {}