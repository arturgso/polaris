package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.models.entities.ShoppingItemCategory;
import io.vexis.polaris.shared.TextUtils;
import org.springframework.stereotype.Component;

@Component
public class ShoppingItemCategoryFactory {

  public ShoppingItemCategory create(String name, String color) {
    return ShoppingItemCategory.builder().name(normalizeName(name)).color(color).build();
  }

  // TODO: Consolidate duplicated catalog name normalization with other factories.
  public String normalizeName(String name) {
    return TextUtils.normalizeText(name).toUpperCase();
  }
}
