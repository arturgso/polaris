package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.GiftsFactory;
import io.vexis.polaris.domain.interfaces.mappers.GiftsMapper;
import io.vexis.polaris.domain.interfaces.repositories.GiftsRepository;
import io.vexis.polaris.domain.interfaces.services.GiftsService;
import io.vexis.polaris.domain.interfaces.services.PersonsService;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.NewGiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.UpdateGiftDTO;
import io.vexis.polaris.domain.models.entities.Gift;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        var giftList = repository.findAllByGiftForId(personId);
        List<GiftDTO> response = new ArrayList<>();

        for (Gift gift : giftList) {
            response.add(mapper.toDTO(gift));
        }

        return response;
    }

    @Transactional
    @Override
    public void updateGift(UpdateGiftDTO dto, UUID giftId) {
        var gift = repository.findById(giftId).orElseThrow(() -> new RuntimeException("Not found"));
        gift = mapper.update(dto, gift);

        repository.save(gift);
    }

    @Transactional
    @Override
    public void deleteGift(UUID giftId) {
        repository.deleteById(giftId);
    }
}
