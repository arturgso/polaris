package io.vexis.polaris;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.vexis.polaris.application.bootstrap.DatabasePopulateRunner;
import io.vexis.polaris.domain.interfaces.repositories.GiftsRepository;
import io.vexis.polaris.domain.interfaces.repositories.PersonsRepository;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    properties = {
      "app.db.populate.enabled=true",
      "spring.datasource.url=jdbc:h2:mem:polaris-populate-enabled;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1"
    })
class DatabasePopulateRunnerEnabledTests {

  @Autowired private DatabasePopulateRunner databasePopulateRunner;

  @Autowired private PersonsRepository personsRepository;

  @Autowired private GiftsRepository giftsRepository;

  @Autowired private ShoppingItemRepository shoppingItemRepository;

  @Test
  void shouldPopulateDemoDataWhenEnabled() {
    assertEquals(3, personsRepository.count());
    assertEquals(4, giftsRepository.count());
    assertEquals(5, shoppingItemRepository.count());

    var personNames = personsRepository.findAll().stream().map(person -> person.getName()).toList();

    assertTrue(personNames.contains("Ana"));
    assertTrue(personNames.contains("Bruno"));
    assertTrue(personNames.contains("Carla"));
  }

  @Test
  void shouldNotDuplicateDemoDataWhenRunnerExecutesAgain() throws Exception {
    databasePopulateRunner.run(new DefaultApplicationArguments(new String[0]));

    assertEquals(3, personsRepository.count());
    assertEquals(4, giftsRepository.count());
    assertEquals(5, shoppingItemRepository.count());
  }
}
