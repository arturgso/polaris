package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.GiftsFactory;
import io.vexis.polaris.domain.interfaces.mappers.GiftsMapper;
import io.vexis.polaris.domain.interfaces.repositories.GiftsRepository;
import io.vexis.polaris.domain.interfaces.services.GiftsService;
import io.vexis.polaris.domain.interfaces.services.PersonsService;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.NewGiftDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GiftsServiceImpl implements GiftsService {

    private final PersonsService personsService;

    private final GiftsRepository repository;
    private final GiftsFactory factory;
    private final GiftsMapper mapper;

    @Override
    public GiftDTO create(NewGiftDTO dto) {
        var person = personsService.getEntity(UUID.fromString(dto.personId()));
        var gift = factory.create(
            dto.title(), 
            dto.link(), 
            person, 
            dto.event(), 
            dto.status()
        );

        gift = repository.save(gift);
        return mapper.toDTO(gift);
    }

    @Override
    public List<GiftDTO> getAllFromPerson(UUID personId) {
        return List.of();
    }

    @Override
    public void updateGift(UUID giftId) {

    }

    @Override
    public void deleteGift(UUID giftId) {

    }
}
