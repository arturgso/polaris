package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.GiftsFactory;
import io.vexis.polaris.application.security.VaultPasswordValidator;
import io.vexis.polaris.domain.enums.GiftStatus;
import io.vexis.polaris.domain.exceptions.GiftNotFoundException;
import io.vexis.polaris.domain.interfaces.mappers.GiftsMapper;
import io.vexis.polaris.domain.interfaces.repositories.GiftsRepository;
import io.vexis.polaris.domain.interfaces.services.EventsService;
import io.vexis.polaris.domain.interfaces.services.GiftListService;
import io.vexis.polaris.domain.interfaces.services.GiftsService;
import io.vexis.polaris.domain.interfaces.services.PersonsService;
import io.vexis.polaris.domain.models.dtos.filters.GiftFiltersDTO;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.NewGiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.UpdateGiftDTO;
import io.vexis.polaris.domain.models.entities.Gift;
import io.vexis.polaris.domain.specs.GiftsSpec;
import io.vexis.polaris.shared.ListMapper;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GiftsServiceImpl implements GiftsService {

  private static final String DEFAULT_EVENT = "NONE";

  private final PersonsService personsService;
  private final EventsService eventsService;
  private final GiftListService giftListService;

  private final GiftsRepository repository;
  private final GiftsFactory factory;
  private final GiftsMapper mapper;
  private final VaultPasswordValidator vaultPasswordValidator;

  @Override
  public GiftDTO create(NewGiftDTO dto) {
    log.info("Creating gift for personId={}", dto.personId());
    var person = personsService.getEntity(dto.personId());
    var event =
        dto.event() == null
            ? eventsService.getEntity(DEFAULT_EVENT)
            : eventsService.getEntity(dto.event().toUpperCase());
    var status =
        dto.status() == null
            ? GiftStatus.IDEA  
            : dto.status();

    var gift = factory.create(dto.title(), dto.link(), person, event, status);

    gift = repository.save(gift);
    log.info("Gift created with id={} for personId={}", gift.getId(), person.getId());
    return mapper.toDTO(gift);
  }

  @Override
  public List<GiftDTO> list(GiftFiltersDTO filters) {
    List<Gift> giftList = repository.findAll(GiftsSpec.byFilters(filters));

    return ListMapper.createResponseList(giftList, mapper::toDTO);
  }

  @Override
  public List<GiftDTO> listByPerson(GiftFiltersDTO filtersDTO) {
    var giftList = repository.findAll(GiftsSpec.byFilters(filtersDTO));
    return ListMapper.createResponseList(giftList, mapper::toDTO);
  }

  @Override
  public Gift getEntity(Long id) {
    return repository.findById(id).orElseThrow(GiftNotFoundException::new);
  }

  @Transactional
  @Override
  public void update(UpdateGiftDTO dto, Long id) {
    update(dto, id, null);
  }

  @Transactional
  @Override
  public void update(UpdateGiftDTO dto, Long id, String vaultPassword) {
    log.info("Updating gift id={}", id);
    var gift = getEntity(id);
    if (Boolean.TRUE.equals(gift.getInVault())) {
      vaultPasswordValidator.validate(vaultPassword);
    }
    gift = mapper.update(dto, gift);

    if (dto.giftFor() != null) {
      gift.setGiftFor(personsService.getEntity(dto.giftFor()));
    }

    if (dto.event() != null) {
      gift.setEvent(eventsService.getEntity(dto.event().toUpperCase()));
    }

    if (dto.giftListId() != null) {
      gift.setGiftList(giftListService.getEntity(dto.giftListId()));
    }

    repository.save(gift);
    log.info("Gift updated id={}", id);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    delete(id, null);
  }

  @Transactional
  @Override
  public void delete(Long id, String vaultPassword) {
    log.info("Deleting gift id={}", id);
    var gift = getEntity(id);
    if (Boolean.TRUE.equals(gift.getInVault())) {
      vaultPasswordValidator.validate(vaultPassword);
    }
    if (!repository.existsById(id)) {
      throw new GiftNotFoundException();
    }
    repository.deleteById(id);
    log.info("Gift deleted id={}", id);
  }

  @Override
  public Long countAll() {
    return repository.count();
  }

  @Override
  public BigDecimal getTotalPrice() {
    return repository.getTotalPrice();
  }

  @Override
  @Transactional
  public void moveGiftToVault(Long id) {
    var gift = getEntity(id);
    gift.setInVault(true);
    repository.save(gift);
  }

  @Override
  public List<GiftDTO> listAllInVault() {
    return repository.findAllByInVaultTrue().stream().map(mapper::toDTO).toList();
  }
}
