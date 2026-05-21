package io.vexis.polaris.domain.models.dtos.gifts;

import io.vexis.polaris.domain.enums.Events;
import io.vexis.polaris.domain.enums.GiftStatus;
import jakarta.validation.constraints.NotBlank;

public record UpdateGiftDTO(
        String title,
        String link,
        String giftFor,
        Events event,
        GiftStatus status
) {
}
