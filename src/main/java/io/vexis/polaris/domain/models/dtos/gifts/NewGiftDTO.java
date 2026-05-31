package io.vexis.polaris.domain.models.dtos.gifts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewGiftDTO(
    @NotBlank String title, String link, @NotNull Long personId, String event, String status) {}
