package io.vexis.polaris.application.services;

import org.springframework.stereotype.Service;

import io.vexis.polaris.domain.interfaces.mappers.GiftListMapper;
import io.vexis.polaris.domain.interfaces.repositories.GiftListRepository;
import io.vexis.polaris.domain.interfaces.services.GiftListService;
import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.dtos.giftlist.NewGiftListDTO;
import io.vexis.polaris.domain.models.entities.GiftList;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GiftListServiceImpl implements GiftListService {

    private final GiftListMapper mapper;
    private final GiftListRepository repository;

    @Override
    public GiftListDTO create(NewGiftListDTO dto) {
        var giftList = mapper.toEntity(dto);
        giftList = repository.save(giftList);

        return mapper.toDTO(giftList);
    }

    @Override
    public GiftList getEntity(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(NewGiftListDTO dto, Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}