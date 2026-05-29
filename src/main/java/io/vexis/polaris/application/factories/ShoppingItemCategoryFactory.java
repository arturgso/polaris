package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.models.entities.ShoppingItemCategory;
import io.vexis.polaris.shared.TextUitls;
import org.springframework.stereotype.Component;

@Component
public class ShoppingItemCategoryFactory {

  public ShoppingItemCategory create(String name, String color) {
    return ShoppingItemCategory.builder().name(normalizeName(name)).color(color).build();
  }

  public String normalizeName(String name) {
    return TextUitls.normalizeText(name).toUpperCase();
  }
}
