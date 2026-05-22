package io.vexis.polaris.domain.models.dtos.gifts;

import jakarta.validation.constraints.NotBlank;

public record NewGiftDTO(
        @NotBlank String title,
        String link,
        @NotBlank String personId,
        String event,
        String status
) {
}
