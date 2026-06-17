package io.vexis.polaris.domain.specs;

import io.vexis.polaris.domain.models.dtos.filters.ListEntityFiltersDTO;
import io.vexis.polaris.shared.abstracts.ListEntity;
import org.springframework.data.jpa.domain.Specification;

public class ListEntitySpec {

  private static <T extends ListEntity> Specification<T> byTitle(String title) {
    return (root, query, cb) ->
        title == null || title.isBlank()
            ? null
            : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
  }

  private static <T extends ListEntity> Specification<T> byInVault(Boolean inVault) {
    return (root, query, cb) -> cb.equal(root.get("inVault"), inVault != null ? inVault : false);
  }

  public static <T extends ListEntity> Specification<T> byFilters(ListEntityFiltersDTO filters) {
    return Specification.<T>where(byTitle(filters.title())).and(byInVault(filters.inVault()));
  }
}
