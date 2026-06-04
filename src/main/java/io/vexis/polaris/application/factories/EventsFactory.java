package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.models.entities.Event;
import io.vexis.polaris.shared.TextUtils;
import org.springframework.stereotype.Component;

@Component
public class EventsFactory {

  public Event create(String name, String color) {
    return Event.builder().tag(TextUtils.normalizeTag(name)).name(name).color(TextUtils.normalizeColor(color)).build();
  }
}
