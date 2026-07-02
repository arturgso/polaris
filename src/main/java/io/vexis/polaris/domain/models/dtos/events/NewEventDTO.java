package io.vexis.polaris.domain.models.dtos.events;

import jakarta.validation.constraints.NotBlank;

public record NewEventDTO(@NotBlank String tag) {}
