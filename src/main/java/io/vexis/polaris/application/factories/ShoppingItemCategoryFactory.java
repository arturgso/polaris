package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.models.entities.ShoppingItemCategory;
import io.vexis.polaris.shared.TextUtils;
import org.springframework.stereotype.Component;

@Component
public class ShoppingItemCategoryFactory {

  public ShoppingItemCategory create(String name, String color) {
    return ShoppingItemCategory.builder()
        .tag(TextUtils.normalizeTag(name))
        .name(name)
        .color(color)
        .build();
  }
}
