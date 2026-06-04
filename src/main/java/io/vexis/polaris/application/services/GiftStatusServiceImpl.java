package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.GiftStatusFactory;
import io.vexis.polaris.domain.exceptions.GiftStatusNotFoundException;
import io.vexis.polaris.domain.interfaces.mappers.GiftStatusMapper;
import io.vexis.polaris.domain.interfaces.repositories.GiftStatusRepository;
import io.vexis.polaris.domain.interfaces.services.GiftStatusService;
import io.vexis.polaris.domain.models.dtos.giftstatus.GiftStatusDTO;
import io.vexis.polaris.domain.models.dtos.giftstatus.NewGiftStatusDTO;
import io.vexis.polaris.domain.models.dtos.giftstatus.UpdateGiftStatusDTO;
import io.vexis.polaris.domain.models.entities.GiftStatus;
import io.vexis.polaris.shared.ListMapper;
import io.vexis.polaris.shared.TextUtils;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GiftStatusServiceImpl implements GiftStatusService {

  private final GiftStatusRepository repository;
  private final GiftStatusFactory factory;
  private final GiftStatusMapper mapper;

  @Override
  public GiftStatusDTO create(NewGiftStatusDTO dto) {
    log.info("Creating gift status");
    var giftStatus = repository.save(factory.create(dto.name(), dto.color()));
    log.info("Gift status created with id={}", giftStatus.getId());
    return mapper.toDTO(giftStatus);
  }

  @Override
  public List<GiftStatusDTO> list() {
    log.debug("Listing gift statuses");
    List<GiftStatus> giftStatusList = repository.findAll();
    return ListMapper.createResponseList(giftStatusList, mapper::toDTO);
  }

  @Override
  public GiftStatus getEntity(Long id) {
    log.debug("Loading gift status id={}", id);
    return repository.findById(id).orElseThrow(GiftStatusNotFoundException::new);
  }

  @Override
  public GiftStatus getEntityByTag(String tag) {
    log.debug("Loading gift status by tag");
    return repository
        .findByTag(TextUtils.normalizeTag(tag))
        .orElseThrow(GiftStatusNotFoundException::new);
  }

  @Transactional
  @Override
  public void update(UpdateGiftStatusDTO dto, Long id) {
    log.info("Updating gift status id={}", id);
    var giftStatus = repository.findById(id).orElseThrow(GiftStatusNotFoundException::new);
    giftStatus = mapper.update(dto, giftStatus);

    if (dto.tag() != null) {
      giftStatus.setTag(TextUtils.normalizeTag(dto.tag()));
    }

    if (dto.color() != null) {
      giftStatus.setColor(TextUtils.normalizeColor(dto.color()));
    }

    repository.save(giftStatus);
    log.info("Gift status updated id={}", id);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    log.info("Deleting gift status id={}", id);
    repository.deleteById(id);
    log.info("Gift status deleted id={}", id);
  }
}