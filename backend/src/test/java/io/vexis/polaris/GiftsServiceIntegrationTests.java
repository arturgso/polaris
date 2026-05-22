package io.vexis.polaris;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class GiftsServiceIntegrationTests {

    @Autowired
    private PersonsService personsService;

    @Autowired
    private GiftsService giftsService;

    @Autowired
    private GiftsRepository giftsRepository;

    @Test
    void shouldCreateGiftWithExplicitEventAndStatusNames() {
        var person = personsService.create(new NewPersonDTO("alice", (short) 10, (short) 1));

        var gift = giftsService.create(new NewGiftDTO(
                "book",
                "http://example.com",
                person.id().toString(),
                "birthday",
                "purchased"
        ));

        assertEquals("BIRTHDAY", gift.event());
        assertEquals("PURCHASED", gift.status());
        assertEquals("alice", gift.giftFor());
    }

    @Test
    void shouldCreateGiftUsingPersistedDefaultsWhenEventAndStatusAreNull() {
        var person = personsService.create(new NewPersonDTO("alice", (short) 10, (short) 1));

        var gift = giftsService.create(new NewGiftDTO(
                "book",
                null,
                person.id().toString(),
                null,
                null
        ));

        assertEquals("NONE", gift.event());
        assertEquals("IDEA", gift.status());
    }

    @Test
    void shouldUpdateGiftEventAndStatusByName() {
        var person = personsService.create(new NewPersonDTO("alice", (short) 10, (short) 1));
        var created = giftsService.create(new NewGiftDTO(
                "book",
                null,
                person.id().toString(),
                null,
                null
        ));

        giftsService.updateGift(new UpdateGiftDTO(null, null, null, "christmas", "delivered"), created.id());

        var updated = giftsRepository.findById(created.id()).orElseThrow();

        assertEquals("CHRISTMAS", updated.getEvent().getName());
        assertEquals("DELIVERED", updated.getStatus().getName());
        assertNotNull(updated.getEvent().getId());
        assertNotNull(updated.getStatus().getId());
    }

    @Test
    void shouldFailWhenGiftReferencesUnknownCatalogName() {
        var person = personsService.create(new NewPersonDTO("alice", (short) 10, (short) 1));

        assertThrows(RuntimeException.class, () -> giftsService.create(new NewGiftDTO(
                "book",
                null,
                person.id().toString(),
                "unknown-event",
                null
        )));
    }
}
