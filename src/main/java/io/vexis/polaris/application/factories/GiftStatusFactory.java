package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.models.entities.GiftStatus;
import io.vexis.polaris.shared.TextUtils;
import org.springframework.stereotype.Component;

@Component
public class GiftStatusFactory {

  private static final String DEFAULT_COLOR = "#6B7280";

  public GiftStatus create(String tag, String name, String color) {
    return GiftStatus.builder().tag(normalizeTag(tag)).name(name).color(normalizeColor(color)).build();
  }

  // TODO: Consolidate duplicated catalog color normalization with EventsFactory.
  public String normalizeColor(String color) {
    return color == null ? DEFAULT_COLOR : color;
  }

  // TODO: Consolidate duplicated catalog tag normalization with other factories.
  public String normalizeTag(String tag) {
    return TextUtils.normalizeText(tag).toUpperCase();
  }
}
