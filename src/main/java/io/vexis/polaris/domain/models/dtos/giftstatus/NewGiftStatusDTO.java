package io.vexis.polaris.domain.models.dtos.giftstatus;

import jakarta.validation.constraints.NotBlank;

public record NewGiftStatusDTO(@NotBlank String tag, @NotBlank String name, String color) {}
