package io.vexis.polaris.domain.specs;

import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import org.springframework.data.jpa.domain.Specification;

public class ShoppingItemsSpec {

    private static Specification<ShoppingItem> byStatusId(Long statusId) {
        return ((root, query, cb) ->
                statusId == null
                        ? null
                        : cb.equal(root.get("status").get("id"), statusId));
    }

    private static Specification<ShoppingItem> byCategoryId(Long categoryId) {
        return ((root, query, cb) ->
                categoryId == null
                        ? null
                        : cb.equal(root.get("category").get("id"), categoryId));
    }

    private static Specification<ShoppingItem> byTitle(String title) {
        return (root, query, cb) ->
                title == null || title.isBlank()
                ? null
                : cb.like(cb.lower(root.get("title")),
                       "%" + title.toLowerCase() + "%"
        );
    }

    public static Specification<ShoppingItem> byFilters(ShoppingItemFiltersDTO filters) {
        return Specification.where(
                byTitle(filters.title())
                        .and(byStatusId(filters.statusId())))
                .and(byCategoryId(filters.categoryId()));
    }
}
