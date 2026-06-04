package io.vexis.polaris.domain.models.dtos.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewUserDTO(
    @NotBlank @Size(max = 100) String username,
    @NotBlank @Size(min = 6, max = 255) String password) {}
