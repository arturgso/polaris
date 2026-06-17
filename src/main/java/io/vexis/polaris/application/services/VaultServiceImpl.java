package io.vexis.polaris.application.services;

import io.vexis.polaris.application.security.JwtService;
import io.vexis.polaris.domain.interfaces.services.GiftListService;
import io.vexis.polaris.domain.interfaces.services.GiftsService;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemService;
import io.vexis.polaris.domain.interfaces.services.ShoppingListService;
import io.vexis.polaris.domain.interfaces.services.VaultService;
import io.vexis.polaris.domain.models.dtos.filters.GiftFiltersDTO;
import io.vexis.polaris.domain.models.dtos.filters.ListEntityFiltersDTO;
import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist.ShoppingListDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VaultServiceImpl implements VaultService {

  private final ShoppingListService shoppingListService;
  private final ShoppingItemService shoppingItemService;

  private final GiftListService giftListService;
  private final GiftsService giftsService;

  private final JwtService jwtService;

  @Value("${app.bootstrap.admin.username}")
  public String allowedUser;

  @Value("${app.bootstrap.vault.password}")
  public String vaultPasswordHash;

  @Override
  public List<GiftDTO> listGifts(GiftFiltersDTO filters) {
    return giftsService.list(filters);
  }

  @Override
  public List<GiftListDTO> listGiftLists(ListEntityFiltersDTO filters) {
    return giftListService.list(filters);
  }

  @Override
  public List<ShoppingItemDTO> listShoppingItems(ShoppingItemFiltersDTO filters) {
    return shoppingItemService.list(filters);
  }

  @Override
  public List<ShoppingListDTO> listShoppingLists(ListEntityFiltersDTO filters) {
    return shoppingListService.list(filters);
  }

  @Override
  public String unlock(String password, UserDetails userDetails) {
    validateUser(userDetails);
    validateVaultPassword(password);

    return jwtService.generateVaultToken(userDetails);
  }

  @Override
  public boolean validate(String token) {
    return jwtService.isVaultTokenValid(token, allowedUser);
  }

  private void validateUser(UserDetails userDetails) {
    if (!allowedUser.equals(userDetails.getUsername())) {
      throw new RuntimeException("Not Allowed");
    }
  }

  private void validateVaultPassword(String password) {
    String hash = new BCryptPasswordEncoder().encode(vaultPasswordHash);

    if (!new BCryptPasswordEncoder().matches(password, hash)) {
      throw new RuntimeException("Incorrect Password");
    }
  }
}
