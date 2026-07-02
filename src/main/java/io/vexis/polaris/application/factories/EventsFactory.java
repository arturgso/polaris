package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.models.entities.Event;
import io.vexis.polaris.shared.TextUtils;
import org.springframework.stereotype.Component;

@Component
public class EventsFactory {

  public Event create(String tag) {
    return Event.builder().tag(TextUtils.normalizeTag(tag)).build();
  }
}
