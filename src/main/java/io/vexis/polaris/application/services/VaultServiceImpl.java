package io.vexis.polaris.application.services;

import io.vexis.polaris.domain.interfaces.services.GiftListService;
import io.vexis.polaris.domain.interfaces.services.ShoppingListService;
import io.vexis.polaris.domain.interfaces.services.VaultService;
import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist.ShoppingListDTO;
import io.vexis.polaris.domain.models.entities.ShoppingList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VaultServiceImpl implements VaultService {

    private final ShoppingListService shoppingListService;
    private final GiftListService giftListService;

    @Override
    public List<GiftDTO> listGifts() {
        return List.of();
    }

    @Override
    public List<GiftListDTO> listGiftLists() {
        return giftListService.listAllInVault();
    }

    @Override
    public List<ShoppingItemDTO> listShoppingItems() {
        return List.of();
    }

    @Override
    public List<ShoppingListDTO> listShoppingLists() {
        return List.of();
    }

    @Override
    public String unlock(String password) {
        return "";
    }

    @Override
    public boolean validate(String token) {
        return false;
    }
}
