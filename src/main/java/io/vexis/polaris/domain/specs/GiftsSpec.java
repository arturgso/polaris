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

  private static Specification<Gift> byStatusId(Long statusId) {
    return ((root, query, cb) ->
        statusId == null ? null : cb.equal(root.get("status").get("id"), statusId));
  }

  private static Specification<Gift> byEventId(Long eventId) {
    return ((root, query, cb) ->
        eventId == null ? null : cb.equal(root.get("event").get("id"), eventId));
  }

  private static Specification<Gift> byGiftForId(Long personId) {
    return ((root, query, cb) ->
        personId == null ? null : cb.equal(root.get("giftFor").get("id"), personId));
  }

  public static Specification<Gift> byFilters(GiftFiltersDTO filters) {
    return Specification.where(byTitle(filters.title()))
        .and(byLink(filters.link()))
        .and(byStatusId(filters.statusId()))
        .and(byEventId(filters.eventId()))
        .and(byGiftForId(filters.personId()));
  }
}
