package io.vexis.polaris;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.vexis.polaris.domain.enums.GiftStatus;
import io.vexis.polaris.domain.interfaces.repositories.GiftsRepository;
import io.vexis.polaris.domain.interfaces.services.GiftsService;
import io.vexis.polaris.domain.interfaces.services.PersonsService;
import io.vexis.polaris.domain.models.dtos.gifts.NewGiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.UpdateGiftDTO;
import io.vexis.polaris.domain.models.dtos.persons.NewPersonDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class GiftsServiceIntegrationTests {

  @Autowired private PersonsService personsService;

  @Autowired private GiftsService giftsService;

  @Autowired private GiftsRepository giftsRepository;

  @Test
  void shouldCreateGiftWithExplicitEventAndStatusNames() {
    var person = personsService.create(new NewPersonDTO("alice", (short) 10, (short) 1));

    var gift =
        giftsService.create(
            new NewGiftDTO(
                "book", "http://example.com", person.id(), "birthday", GiftStatus.COMPRADO));

    assertEquals("BIRTHDAY", gift.event());
    assertEquals(GiftStatus.COMPRADO.name(), gift.status().value());
    assertEquals(GiftStatus.COMPRADO.getColor(), gift.status().color());
    assertEquals("alice", gift.giftFor());
  }

  @Test
  void shouldCreateGiftUsingPersistedDefaultsWhenEventAndStatusAreNull() {
    var person = personsService.create(new NewPersonDTO("alice", (short) 10, (short) 1));

    var gift = giftsService.create(new NewGiftDTO("book", null, person.id(), null, null));

    assertEquals("NONE", gift.event());
    assertEquals(GiftStatus.IDEIA.name(), gift.status().value());
    assertEquals(GiftStatus.IDEIA.getColor(), gift.status().color());
  }

  @Test
  void shouldUpdateGiftEventAndStatusByName() {
    var person = personsService.create(new NewPersonDTO("alice", (short) 10, (short) 1));
    var created = giftsService.create(new NewGiftDTO("book", null, person.id(), null, null));

    giftsService.update(
        new UpdateGiftDTO(null, null, null, null, "christmas", GiftStatus.ENTREGUE), created.id());

    var updated = giftsRepository.findById(created.id()).orElseThrow();

    assertEquals("CHRISTMAS", updated.getEvent().getTag());
    assertEquals(GiftStatus.ENTREGUE, updated.getStatus());
    assertNotNull(updated.getEvent().getId());
  }

  @Test
  void shouldFailWhenGiftReferencesUnknownCatalogName() {
    var person = personsService.create(new NewPersonDTO("alice", (short) 10, (short) 1));

    assertThrows(
        RuntimeException.class,
        () ->
            giftsService.create(new NewGiftDTO("book", null, person.id(), "unknown-event", null)));
  }
}
