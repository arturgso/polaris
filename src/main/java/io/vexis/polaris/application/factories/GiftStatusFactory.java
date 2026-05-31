package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.models.entities.GiftStatus;
import io.vexis.polaris.shared.TextUitls;
import org.springframework.stereotype.Component;

@Component
public class GiftStatusFactory {

  private static final String DEFAULT_COLOR = "#6B7280";

  public GiftStatus create(String name, String color) {
    return GiftStatus.builder().name(normalizeName(name)).color(normalizeColor(color)).build();
  }

  public String normalizeColor(String color) {
    return color == null ? DEFAULT_COLOR : color;
  }

  public String normalizeName(String name) {
    return TextUitls.normalizeText(name).toUpperCase();
  }
}
