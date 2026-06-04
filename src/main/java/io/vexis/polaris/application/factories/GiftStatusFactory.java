package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.models.entities.GiftStatus;
import io.vexis.polaris.shared.TextUtils;
import org.springframework.stereotype.Component;

@Component
public class GiftStatusFactory {



  public GiftStatus create(String name, String color) {
    return GiftStatus.builder().tag(TextUtils.normalizeTag(name)).name(name).color(TextUtils.normalizeColor(color)).build();
  }
}
