package io.vexis.polaris.application.services;

import java.util.List;

import io.vexis.polaris.shared.dtos.NewListDTO;
import org.springframework.stereotype.Service;

import io.vexis.polaris.domain.exceptions.GiftListNotFoundException;
import io.vexis.polaris.domain.interfaces.mappers.GiftListMapper;
import io.vexis.polaris.domain.interfaces.repositories.GiftListRepository;
import io.vexis.polaris.domain.interfaces.services.GiftListService;
import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.entities.GiftList;
import io.vexis.polaris.shared.ListMapper;
import io.vexis.polaris.shared.TextUtils;
import io.vexis.polaris.shared.utils.EntityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GiftListServiceImpl implements GiftListService {

  private final GiftListMapper mapper;
  private final GiftListRepository repository;

  @Override
  public GiftListDTO create(NewListDTO dto) {
    var giftList = new GiftList();
    giftList.setTitle(TextUtils.normalizeText(dto.title()));
    giftList = repository.save(giftList);

    return mapper.toDTO(giftList);
  }

  @Override
  public GiftList getEntity(Long id) {
    return repository.findById(id).orElseThrow(GiftListNotFoundException::new);
  }

  @Override
  public GiftListDTO getById(Long id) {
    return mapper.toDTO(getEntity(id));
  }

  @Transactional
  @Override
  public void update(NewListDTO dto, Long id) {
    var giftList = EntityUtils.findOrThrow(repository, id);
    if (dto.title() != null) {
      giftList.setTitle(TextUtils.normalizeText(dto.title()));
    }

    repository.save(giftList);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    if (!repository.existsById(id)) {
      throw new GiftListNotFoundException();
    }
    repository.deleteById(id);
  }

  @Override
  public List<GiftListDTO> list() {
    var lists = repository.findAll();
    return ListMapper.createResponseList(lists, mapper::toDTO);
  }

}
