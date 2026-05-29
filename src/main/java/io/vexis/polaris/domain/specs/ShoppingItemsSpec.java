package io.vexis.polaris.domain.specs;

import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class ShoppingItemsSpec {

    private static Specification<ShoppingItem> byStatusId(Long statusId) {
        return ((root, query, criteriaBuilder) -> {
          if (Objects.nonNull(statusId)) {
              return criteriaBuilder.equal(root.get("status").get("id"), statusId);
          }

          return null;
        });
    }

    private static Specification<ShoppingItem> byCategoryId(Long categoryId) {
        return ((root, query, cb) ->
                categoryId == null
                        ? null
                        : cb.equal(root.get("category").get("id"), categoryId));
    }

    public static Specification<ShoppingItem> byFilters(ShoppingItemFiltersDTO filters) {
        return Specification.where(byStatusId(filters.statusId())).and(byCategoryId(filters.categoryId()));
    }
}
