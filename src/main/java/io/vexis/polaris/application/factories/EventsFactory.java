package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.models.entities.Event;
import io.vexis.polaris.shared.TextUitls;
import org.springframework.stereotype.Component;

@Component
public class EventsFactory {

  public Event create(String name) {
    return Event.builder().name(normalizeName(name)).build();
  }

  public String normalizeName(String name) {
    return TextUitls.normalizeText(name).toUpperCase();
  }
}
