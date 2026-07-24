package io.vexis.polaris.domain.specs;

import io.vexis.polaris.domain.enums.ShoppingItemStatus;
import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import io.vexis.polaris.shared.ListConstants;
import org.springframework.data.jpa.domain.Specification;

public class ShoppingItemsSpec {

  private static Specification<ShoppingItem> byStatus(String status) {
    return ((root, query, cb) ->
        status == null || status.isBlank()
            ? null
            : cb.equal(root.get("status"), ShoppingItemStatus.valueOf(status)));
  }

  private static Specification<ShoppingItem> byTag(String tag) {
    return ((root, query, cb) ->
        tag == null ? null : cb.equal(root.get("category").get("tag"), tag));
  }

  private static Specification<ShoppingItem> byTitle(String title) {
    return (root, query, cb) ->
        title == null || title.isBlank()
            ? null
            : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
  }

  private static Specification<ShoppingItem> byInVault(Boolean inVault) {
    return (root, query, cb) -> cb.equal(root.get("inVault"), inVault != null ? inVault : false);
  }

  private static Specification<ShoppingItem> byListId(Long listId) {
    return (root, query, cb) -> {
      if (listId == null) {
        return null;
      }
      if (ListConstants.NO_LIST_ID.equals(listId)) {
        return cb.isNull(root.get("shoppingList"));
      }
      return cb.equal(root.get("shoppingList").get("id"), listId);
    };
  }

  public static Specification<ShoppingItem> byFilters(ShoppingItemFiltersDTO filters) {
    return Specification.where(byTitle(filters.title()).and(byStatus(filters.status())))
        .and(byTag(filters.tag()))
        .and(byListId(filters.listId()))
        .and(byInVault(filters.inVault()));
  }
}
