package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.GiftStatusFactory;
import io.vexis.polaris.domain.interfaces.mappers.GiftStatusMapper;
import io.vexis.polaris.domain.interfaces.repositories.GiftStatusRepository;
import io.vexis.polaris.domain.interfaces.services.GiftStatusService;
import io.vexis.polaris.domain.models.dtos.giftstatus.GiftStatusDTO;
import io.vexis.polaris.domain.models.dtos.giftstatus.NewGiftStatusDTO;
import io.vexis.polaris.domain.models.dtos.giftstatus.UpdateGiftStatusDTO;
import io.vexis.polaris.domain.models.entities.GiftStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GiftStatusServiceImpl implements GiftStatusService {

    private final GiftStatusRepository repository;
    private final GiftStatusFactory factory;
    private final GiftStatusMapper mapper;

    @Override
    public GiftStatusDTO create(NewGiftStatusDTO dto) {
        var giftStatus = repository.save(factory.create(dto.name()));
        return mapper.toDTO(giftStatus);
    }

    @Override
    public List<GiftStatusDTO> getAll() {
        List<GiftStatus> giftStatusList = repository.findAll();
        List<GiftStatusDTO> response = new ArrayList<>();

        for (GiftStatus giftStatus : giftStatusList) {
            response.add(mapper.toDTO(giftStatus));
        }

        return response;
    }

    @Override
    public GiftStatus getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Gift status not found"));
    }

    @Override
    public GiftStatus getEntityByName(String name) {
        return repository.findByName(factory.normalizeName(name))
                .orElseThrow(() -> new RuntimeException("Gift status not found"));
    }

    @Transactional
    @Override
    public void update(UpdateGiftStatusDTO dto, Long id) {
        var giftStatus = repository.findById(id).orElseThrow(() -> new RuntimeException("Gift status not found"));
        giftStatus = mapper.update(dto, giftStatus);

        if (dto.name() != null) {
            giftStatus.setName(factory.normalizeName(dto.name()));
        }

        repository.save(giftStatus);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
