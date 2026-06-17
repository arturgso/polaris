package io.vexis.polaris.domain.specs;

import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import org.springframework.data.jpa.domain.Specification;

public class ShoppingItemsSpec {

  private static Specification<ShoppingItem> byStatus(String status) {
    return ((root, query, cb) ->
        status == null ? null : cb.equal(root.get("status").get("tag"), status));
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

  public static Specification<ShoppingItem> byFilters(ShoppingItemFiltersDTO filters) {
    return Specification.where(byTitle(filters.title()).and(byStatus(filters.status())))
        .and(byTag(filters.tag()))
        .and(byInVault(filters.inVault()));
  }
}
