package io.vexis.polaris.domain.specs;

import io.vexis.polaris.domain.models.dtos.filters.GiftFiltersDTO;
import io.vexis.polaris.domain.models.entities.Gift;
import org.springframework.data.jpa.domain.Specification;

public class GiftsSpec {

  private static Specification<Gift> byTitle(String title) {
    return (root, query, cb) ->
        title == null || title.isBlank()
            ? null
            : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
  }

  private static Specification<Gift> byLink(String link) {
    return (root, query, cb) ->
        link == null || link.isBlank()
            ? null
            : cb.like(cb.lower(root.get("link")), "%" + link.toLowerCase() + "%");
  }

  private static Specification<Gift> byStatus(String status) {
    return ((root, query, cb) ->
        status == null ? null : cb.equal(root.get("status").get("tag"), status));
  }

  private static Specification<Gift> byEvent(String event) {
    return ((root, query, cb) ->
        event == null ? null : cb.equal(root.get("event").get("tag"), event));
  }

  private static Specification<Gift> byGiftForId(Long personId) {
    return ((root, query, cb) ->
        personId == null ? null : cb.equal(root.get("giftFor").get("id"), personId));
  }

  private static Specification<Gift> byInVault(Boolean inVault) {
    return (root, query, cb) -> 
    cb.equal(root.get("inVault"), inVault != null ? inVault : false);
  }

  public static Specification<Gift> byFilters(GiftFiltersDTO filters) {
    return Specification.where(byTitle(filters.title()))
        .and(byLink(filters.link()))
        .and(byStatus(filters.status()))
        .and(byEvent(filters.event()))
        .and(byGiftForId(filters.personId()))
        .and(byInVault(filters.inVault()));
  }
}
