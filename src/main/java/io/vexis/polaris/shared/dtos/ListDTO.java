package io.vexis.polaris.shared.dtos;

import java.time.Instant;
import lombok.Data;

@Data
public abstract class ListDTO {
  Long id;
  String title;
  Instant createdAt;
  Instant updatedAt;
}
