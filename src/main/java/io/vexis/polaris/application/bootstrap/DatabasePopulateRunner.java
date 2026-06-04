package io.vexis.polaris.application.bootstrap;

import io.vexis.polaris.domain.interfaces.repositories.EventsRepository;
import io.vexis.polaris.domain.interfaces.repositories.GiftStatusRepository;
import io.vexis.polaris.domain.interfaces.repositories.GiftsRepository;
import io.vexis.polaris.domain.interfaces.repositories.PersonsRepository;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemCategoriesRepository;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemRepository;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemStatusesRepository;
import io.vexis.polaris.domain.models.entities.Event;
import io.vexis.polaris.domain.models.entities.Gift;
import io.vexis.polaris.domain.models.entities.GiftStatus;
import io.vexis.polaris.domain.models.entities.Person;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import io.vexis.polaris.domain.models.entities.ShoppingItemCategory;
import io.vexis.polaris.domain.models.entities.ShoppingItemStatus;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.db.populate.enabled", havingValue = "true")
public class DatabasePopulateRunner implements ApplicationRunner {

  private final PersonsRepository personsRepository;
  private final GiftsRepository giftsRepository;
  private final ShoppingItemRepository shoppingItemRepository;
  private final EventsRepository eventsRepository;
  private final GiftStatusRepository giftStatusRepository;
  private final ShoppingItemCategoriesRepository shoppingItemCategoriesRepository;
  private final ShoppingItemStatusesRepository shoppingItemStatusesRepository;

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    log.info("Database populate is enabled");

    if (personsRepository.count() > 0
        || giftsRepository.count() > 0
        || shoppingItemRepository.count() > 0) {
      log.info("Database populate skipped because user data tables already contain records");
      return;
    }

    var birthday = getEvent("BIRTHDAY");
    var christmas = getEvent("CHRISTMAS");
    var marriage = getEvent("MARRIAGE");
    var none = getEvent("NONE");

    var giftIdea = getGiftStatus("IDEA");
    var purchased = getGiftStatus("PURCHASED");
    var delivered = getGiftStatus("DELIVERED");

    var tech = getShoppingItemCategory("TECH");
    var health = getShoppingItemCategory("HEALTH");
    var makeup = getShoppingItemCategory("MAKEUP");
    var other = getShoppingItemCategory("OTHER");

    var shoppingIdea = getShoppingItemStatus("IDEA");
    var planned = getShoppingItemStatus("PLANNED");
    var toBuy = getShoppingItemStatus("TO_BUY");
    var bought = getShoppingItemStatus("BOUGHT");
    var canceled = getShoppingItemStatus("CANCELED");

    var persons =
        personsRepository.saveAll(
            List.of(
                Person.builder()
                    .name("Ana")
                    .birthdayMonth((short) 5)
                    .birthdayDay((short) 12)
                    .build(),
                Person.builder()
                    .name("Bruno")
                    .birthdayMonth((short) 9)
                    .birthdayDay((short) 23)
                    .build(),
                Person.builder()
                    .name("Carla")
                    .birthdayMonth((short) 12)
                    .birthdayDay((short) 3)
                    .build()));

    var ana = persons.get(0);
    var bruno = persons.get(1);
    var carla = persons.get(2);

    giftsRepository.saveAll(
        List.of(
            gift("Fone bluetooth", "https://example.com/fone", ana, birthday, purchased),
            gift("Livro de receitas", null, bruno, christmas, giftIdea),
            gift("Jogo de toalhas", "https://example.com/toalhas", carla, marriage, delivered),
            gift("Cartao presente", null, ana, none, giftIdea)));

    shoppingItemRepository.saveAll(
        List.of(
            shoppingItem(
                "Teclado mecanico", "https://example.com/teclado", tech, "349.90", planned),
            shoppingItem("Vitaminas", null, health, "79.90", toBuy),
            shoppingItem("Base liquida", "https://example.com/base", makeup, "119.90", bought),
            shoppingItem("Organizador", null, other, "45.50", shoppingIdea),
            shoppingItem("Cabo USB-C", null, tech, "39.90", canceled)));

    log.info(
        "Database populate created {} persons, {} gifts and {} shopping items",
        personsRepository.count(),
        giftsRepository.count(),
        shoppingItemRepository.count());
  }

  private Gift gift(String title, String link, Person person, Event event, GiftStatus status) {
    return Gift.builder()
        .title(title)
        .link(link)
        .giftFor(person)
        .event(event)
        .status(status)
        .build();
  }

  private ShoppingItem shoppingItem(
      String title,
      String link,
      ShoppingItemCategory category,
      String price,
      ShoppingItemStatus status) {
    return ShoppingItem.builder()
        .title(title)
        .link(link)
        .category(category)
        .price(new BigDecimal(price))
        .status(status)
        .build();
  }

  private Event getEvent(String tag) {
    return eventsRepository.findByTag(tag).orElseThrow(() -> missingCatalog("event", tag));
  }

  private GiftStatus getGiftStatus(String tag) {
    return giftStatusRepository
        .findByTag(tag)
        .orElseThrow(() -> missingCatalog("gift status", tag));
  }

  private ShoppingItemCategory getShoppingItemCategory(String tag) {
    return shoppingItemCategoriesRepository
        .findByTag(tag)
        .orElseThrow(() -> missingCatalog("shopping item category", tag));
  }

  private ShoppingItemStatus getShoppingItemStatus(String tag) {
    return shoppingItemStatusesRepository
        .findByTag(tag)
        .orElseThrow(() -> missingCatalog("shopping item status", tag));
  }

  private IllegalStateException missingCatalog(String catalog, String tag) {
    return new IllegalStateException(
        "Required " + catalog + " catalog '" + tag + "' was not found. Check Flyway migrations.");
  }
}
