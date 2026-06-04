package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.models.entities.ShoppingItemCategory;
import io.vexis.polaris.shared.TextUtils;
import org.springframework.stereotype.Component;

@Component
public class ShoppingItemCategoryFactory {

  public ShoppingItemCategory create(String tag, String name, String color) {
    return ShoppingItemCategory.builder().tag(normalizeTag(tag)).name(name).color(color).build();
  }

  // TODO: Consolidate duplicated catalog tag normalization with other factories.
  public String normalizeTag(String tag) {
    return TextUtils.normalizeText(tag).toUpperCase();
  }
}
