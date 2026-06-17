package io.vexis.polaris.application.services;

import io.vexis.polaris.domain.exceptions.GiftListNotFoundException;
import io.vexis.polaris.domain.interfaces.mappers.GiftListMapper;
import io.vexis.polaris.domain.interfaces.repositories.GiftListRepository;
import io.vexis.polaris.domain.interfaces.repositories.GiftsRepository;
import io.vexis.polaris.domain.interfaces.services.GiftListService;
import io.vexis.polaris.domain.models.dtos.filters.ListEntityFiltersDTO;
import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.entities.GiftList;
import io.vexis.polaris.domain.specs.ListEntitySpec;
import io.vexis.polaris.shared.ListMapper;
import io.vexis.polaris.shared.TextUtils;
import io.vexis.polaris.shared.dtos.NewListDTO;
import io.vexis.polaris.shared.utils.EntityUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GiftListServiceImpl implements GiftListService {

  private final GiftListMapper mapper;
  private final GiftListRepository repository;
  private final GiftsRepository giftsRepository;

  @Override
  public GiftListDTO create(NewListDTO dto) {
    log.info("Creating gift list");
    var giftList = new GiftList();
    giftList.setTitle(TextUtils.normalizeText(dto.title()));
    giftList = repository.save(giftList);

    log.info("Gift list created with id={}", giftList.getId());
    return mapper.toDTO(giftList);
  }

  @Override
  public GiftList getEntity(Long id) {
    log.debug("Loading gift list id={}", id);
    return repository.findById(id).orElseThrow(GiftListNotFoundException::new);
  }

  @Override
  public GiftListDTO getById(Long id) {
    log.debug("Loading gift list DTO id={}", id);
    return mapper.toDTO(getEntity(id));
  }

  @Override
  public List<GiftListDTO> list(ListEntityFiltersDTO filters) {
    log.debug("Listing gift lists");
    List<GiftList> lists = repository.findAll(ListEntitySpec.byFilters(filters));
    return ListMapper.createResponseList(lists, mapper::toDTO);
  }

  @Override
  @Transactional
  public void moveToVault(Long id) {
    var list = getEntity(id);
    list.setInVault(true);
    repository.save(list);
    list.getGifts().forEach(gift -> gift.setInVault(true));
    giftsRepository.saveAll(list.getGifts());
  }

  @Transactional
  @Override
  public void update(NewListDTO dto, Long id) {
    log.info("Updating gift list id={}", id);
    var giftList = EntityUtils.findOrThrow(repository, id);
    if (dto.title() != null) {
      giftList.setTitle(TextUtils.normalizeText(dto.title()));
    }

    repository.save(giftList);
    log.info("Gift list updated id={}", id);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    log.info("Deleting gift list id={}", id);
    if (!repository.existsById(id)) {
      throw new GiftListNotFoundException();
    }
    repository.deleteById(id);
    log.info("Gift list deleted id={}", id);
  }
}
