package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.models.entities.GiftStatus;
import io.vexis.polaris.shared.TextUitls;
import org.springframework.stereotype.Component;

@Component
public class GiftStatusFactory {

  public GiftStatus create(String name) {
    return GiftStatus.builder().name(normalizeName(name)).build();
  }

  public String normalizeName(String name) {
    return TextUitls.normalizeText(name).toUpperCase();
  }
}
