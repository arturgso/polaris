package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.interfaces.services.ShoppingItemCategoriesService;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemStatusesService;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import io.vexis.polaris.shared.TextUtils;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShoppingItemFactory {

  private final ShoppingItemStatusesService statusesService;
  private final ShoppingItemCategoriesService categoriesService;

  public ShoppingItem create(
      String title, String link, Long categoryId, BigDecimal price, Long statusId) {
    return ShoppingItem.builder()
        .title(TextUtils.normalizeText(title))
        .link(link)
        .category(categoriesService.getEntity(categoryId))
        .price(price)
        .status(statusesService.getEntity(statusId))
        .build();
  }
}
