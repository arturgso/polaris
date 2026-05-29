package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.ShoppingItemFactory;
import io.vexis.polaris.domain.interfaces.mappers.ShoppingItemMapper;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemRepository;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemService;
import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.NewShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.UpdateShoppingItemDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import io.vexis.polaris.domain.specs.ShoppingItemsSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingItemServiceImpl implements ShoppingItemService {

    private final ShoppingItemRepository repository;
    private final ShoppingItemMapper mapper;
    private final ShoppingItemFactory factory;

    @Override
    public ShoppingItemDTO create(NewShoppingItemDTO dto) {
        var item = factory.create(
                dto.title(),
                dto.link(),
                dto.categoryId(),
                dto.price(),
                dto.statusId()
        );

        item = repository.save(item);
        return mapper.toDTO(item);
    }

    @Override
    public List<ShoppingItemDTO> list(ShoppingItemFiltersDTO filtersDTO) {
        List<ShoppingItem> itemList = repository.findAll(ShoppingItemsSpec.byFilters(filtersDTO));
        List<ShoppingItemDTO> responseList = new ArrayList<>();

        for (ShoppingItem item : itemList) {
            responseList.add(mapper.toDTO(item));
        }

        return responseList;
    }

    @Override
    public void update(UpdateShoppingItemDTO dto, Long itemId) {

    }

    @Override
    public void delete(Long itemId) {

    }
}
