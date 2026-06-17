package io.vexis.polaris;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.vexis.polaris.application.bootstrap.DatabasePopulateRunner;
import io.vexis.polaris.domain.interfaces.repositories.EventsRepository;
import io.vexis.polaris.domain.interfaces.repositories.GiftsRepository;
import io.vexis.polaris.domain.interfaces.repositories.PersonsRepository;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemCategoriesRepository;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemRepository;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemStatusesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    properties = {
      "app.db.populate.enabled=false",
      "spring.datasource.url=jdbc:h2:mem:polaris-populate-disabled;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1"
    })
class DatabasePopulateRunnerDisabledTests {

  @Autowired private ObjectProvider<DatabasePopulateRunner> databasePopulateRunner;

  @Autowired private EventsRepository eventsRepository;

  @Autowired private ShoppingItemCategoriesRepository shoppingItemCategoriesRepository;

  @Autowired private ShoppingItemStatusesRepository shoppingItemStatusesRepository;

  @Autowired private PersonsRepository personsRepository;

  @Autowired private GiftsRepository giftsRepository;

  @Autowired private ShoppingItemRepository shoppingItemRepository;

  @Test
  void shouldNotPopulateDemoDataWhenDisabled() {
    assertNull(databasePopulateRunner.getIfAvailable());

    assertEquals(4, eventsRepository.count());
    assertEquals(4, shoppingItemCategoriesRepository.count());
    assertEquals(5, shoppingItemStatusesRepository.count());

    assertEquals(0, personsRepository.count());
    assertEquals(0, giftsRepository.count());
    assertEquals(0, shoppingItemRepository.count());
  }
}
