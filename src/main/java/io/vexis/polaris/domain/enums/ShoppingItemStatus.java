package io.vexis.polaris.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShoppingItemStatus {
  IDEIA("#A855F7"),
  PLANEJADO("#3B82F6"),
  COMPRAR("#F59E0B"),
  COMPRADO("#22C55E"),
  CANCELADO("#EF4444");

  private final String color;
}
