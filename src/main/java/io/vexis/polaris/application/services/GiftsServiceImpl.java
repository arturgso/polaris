package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.GiftsFactory;
import io.vexis.polaris.domain.exceptions.GiftNotFoundException;
import io.vexis.polaris.domain.interfaces.mappers.GiftsMapper;
import io.vexis.polaris.domain.interfaces.repositories.GiftsRepository;
import io.vexis.polaris.domain.interfaces.services.EventsService;
import io.vexis.polaris.domain.interfaces.services.GiftStatusService;
import io.vexis.polaris.domain.interfaces.services.GiftsService;
import io.vexis.polaris.domain.interfaces.services.PersonsService;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.NewGiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.UpdateGiftDTO;
import io.vexis.polaris.domain.models.entities.Gift;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GiftsServiceImpl implements GiftsService {

  private static final String DEFAULT_EVENT = "NONE";
  private static final String DEFAULT_STATUS = "IDEA";

  private final PersonsService personsService;
  private final EventsService eventsService;
  private final GiftStatusService giftStatusService;

  private final GiftsRepository repository;
  private final GiftsFactory factory;
  private final GiftsMapper mapper;

  @Override
  public GiftDTO create(NewGiftDTO dto) {
    log.info("Creating gift for personId={}", dto.personId());
    var person = personsService.getEntity(UUID.fromString(dto.personId()));
    var event =
        dto.event() == null
            ? eventsService.getEntityByName(DEFAULT_EVENT)
            : eventsService.getEntityByName(dto.event());
    var status =
        dto.status() == null
            ? giftStatusService.getEntityByName(DEFAULT_STATUS)
            : giftStatusService.getEntityByName(dto.status());

    var gift = factory.create(dto.title(), dto.link(), person, event, status);

    gift = repository.save(gift);
    log.info("Gift created with id={} for personId={}", gift.getId(), person.getId());
    return mapper.toDTO(gift);
  }

  @Override
  public List<GiftDTO> getAllFromPerson(UUID personId) {
    log.debug("Listing gifts for personId={}", personId);
    var giftList = repository.findAllByGiftForId(personId);
    List<GiftDTO> response = new ArrayList<>();

    for (Gift gift : giftList) {
      response.add(mapper.toDTO(gift));
    }

    log.debug("Found {} gifts for personId={}", response.size(), personId);
    return response;
  }

  @Transactional
  @Override
  public void updateGift(UpdateGiftDTO dto, UUID giftId) {
    log.info("Updating gift id={}", giftId);
    var gift = repository.findById(giftId).orElseThrow(GiftNotFoundException::new);
    gift = mapper.update(dto, gift);

    if (dto.event() != null) {
      gift.setEvent(eventsService.getEntityByName(dto.event()));
    }

    if (dto.status() != null) {
      gift.setStatus(giftStatusService.getEntityByName(dto.status()));
    }

    repository.save(gift);
    log.info("Gift updated id={}", giftId);
  }

  @Transactional
  @Override
  public void deleteGift(UUID giftId) {
    log.info("Deleting gift id={}", giftId);
    repository.deleteById(giftId);
    log.info("Gift deleted id={}", giftId);
  }
}
