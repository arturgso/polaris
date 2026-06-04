package io.vexis.polaris.domain.models.dtos.users;

import java.time.Instant;
import java.util.UUID;

public record UserDTO(
    UUID id,
    String username,
    Instant createdAt,
    Instant updatedAt
) {
    
}
