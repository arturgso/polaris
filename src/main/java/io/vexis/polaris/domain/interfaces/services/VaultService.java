package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist.ShoppingListDTO;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface VaultService {

  List<GiftDTO> listGifts();

  List<GiftListDTO> listGiftLists();

  List<ShoppingItemDTO> listShoppingItems();

  List<ShoppingListDTO> listShoppingLists();

  String unlock(String password, UserDetails userDetails);

  boolean validate(String token);
}
