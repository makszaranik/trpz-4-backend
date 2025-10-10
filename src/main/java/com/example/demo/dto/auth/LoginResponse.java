package com.example.demo.dto.auth;

import jakarta.validation.constraints.NotNull;

public record LoginResponse(
        @NotNull String token
) {}
