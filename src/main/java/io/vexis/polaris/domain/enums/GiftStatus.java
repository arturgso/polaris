package io.vexis.polaris.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GiftStatus {
  IDEIA("#A855F7"),
  COMPRADO("#3B82F6"),
  ENTREGUE("#22C55E");

  private final String color;
}
