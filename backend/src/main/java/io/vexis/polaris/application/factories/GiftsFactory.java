package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.models.entities.Event;
import io.vexis.polaris.domain.models.entities.Gift;
import io.vexis.polaris.domain.models.entities.GiftStatus;
import io.vexis.polaris.domain.models.entities.Person;
import io.vexis.polaris.shared.TextUitls;

import org.springframework.stereotype.Component;

@Component
public class GiftsFactory {

    public Gift create(
      String title,
      String link,
      Person person,
      Event event,
      GiftStatus status
    ) {
      return Gift.builder()
      .title(TextUitls.normalizeText(title)) 
      .link(TextUitls.normalizeText(link))
      .giftFor(person)
      .event(event)
      .status(status)
      .build();
    }

    
}
