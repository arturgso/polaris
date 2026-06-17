package io.vexis.polaris.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.vexis.polaris.application.security.JwtService;
import io.vexis.polaris.application.security.VaultPasswordValidator;
import io.vexis.polaris.domain.exceptions.VaultAuthenticationException;
import io.vexis.polaris.domain.interfaces.services.GiftListService;
import io.vexis.polaris.domain.interfaces.services.GiftsService;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemService;
import io.vexis.polaris.domain.interfaces.services.ShoppingListService;
import io.vexis.polaris.domain.models.dtos.filters.ListEntityFiltersDTO;
import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist.ShoppingListDTO;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class VaultServiceImplTests {

  private static final String ALLOWED_USER = "admin";
  private static final String VAULT_PASSWORD = "teste";

  @Mock private ShoppingListService shoppingListService;

  @Mock private ShoppingItemService shoppingItemService;

  @Mock private GiftListService giftListService;

  @Mock private GiftsService giftsService;

  @Mock private JwtService jwtService;

  @Mock private VaultPasswordValidator vaultPasswordValidator;

  @InjectMocks private VaultServiceImpl vaultService;

  @BeforeEach
  void setUp() {
    vaultService.allowedUser = ALLOWED_USER;
  }

  @Test
  void shouldGenerateVaultTokenForAllowedUserWithCorrectPassword() {
    UserDetails userDetails = User.withUsername(ALLOWED_USER).password("password").roles("ADMIN").build();
    when(jwtService.generateVaultToken(userDetails)).thenReturn("vault-token");

    String token = vaultService.unlock(VAULT_PASSWORD, userDetails);

    assertThat(token).isEqualTo("vault-token");
    verify(jwtService).generateVaultToken(userDetails);
  }

  @Test
  void shouldValidateVaultTokenForAllowedUser() {
    when(jwtService.isVaultTokenValid("vault-token", ALLOWED_USER)).thenReturn(true);

    assertThat(vaultService.validate("vault-token")).isTrue();

    verify(jwtService).isVaultTokenValid("vault-token", ALLOWED_USER);
  }

  @Test
  void shouldRejectUnlockWhenPasswordIsWrong() {
    UserDetails userDetails = User.withUsername(ALLOWED_USER).password("password").roles("ADMIN").build();
    org.mockito.Mockito.doThrow(new VaultAuthenticationException("Incorrect Password"))
        .when(vaultPasswordValidator)
        .validate("wrong-password");

    org.assertj.core.api.Assertions.assertThatThrownBy(
            () -> vaultService.unlock("wrong-password", userDetails))
        .isInstanceOf(VaultAuthenticationException.class)
        .hasMessage("Incorrect Password");
  }

  @Test
  void shouldRejectUnlockWhenUserIsNotAllowed() {
    UserDetails userDetails = User.withUsername("other-admin").password("password").roles("ADMIN").build();

    org.assertj.core.api.Assertions.assertThatThrownBy(
            () -> vaultService.unlock(VAULT_PASSWORD, userDetails))
        .isInstanceOf(VaultAuthenticationException.class)
        .hasMessage("Not Allowed");
  }

  @Test
  void shouldDelegateVaultListOperationsToUnderlyingServices() {
    GiftListDTO giftListDTO = new GiftListDTO();
    ShoppingListDTO shoppingListDTO = new ShoppingListDTO();
    GiftDTO giftDTO = new GiftDTO(null, null, null, null, null, null, null, null);
    ShoppingItemDTO shoppingItemDTO =
        new ShoppingItemDTO(null, null, null, null, null, null, null, null);

    when(giftListService.list(new ListEntityFiltersDTO("title", true))).thenReturn(List.of(giftListDTO));
    when(shoppingListService.list(new ListEntityFiltersDTO("title", true))).thenReturn(List.of(shoppingListDTO));
    when(giftsService.list(new io.vexis.polaris.domain.models.dtos.filters.GiftFiltersDTO(null, "status", "event", "title", "link", true)))
        .thenReturn(List.of(giftDTO));
    when(shoppingItemService.list(new ShoppingItemFiltersDTO("status", "tag", "title", true)))
        .thenReturn(List.of(shoppingItemDTO));

    assertThat(vaultService.listGiftLists(new ListEntityFiltersDTO("title", true))).containsExactly(giftListDTO);
    assertThat(vaultService.listShoppingLists(new ListEntityFiltersDTO("title", true))).containsExactly(shoppingListDTO);
    assertThat(vaultService.listGifts(new io.vexis.polaris.domain.models.dtos.filters.GiftFiltersDTO(null, "status", "event", "title", "link", true)))
        .containsExactly(giftDTO);
    assertThat(vaultService.listShoppingItems(new ShoppingItemFiltersDTO("status", "tag", "title", true)))
        .containsExactly(shoppingItemDTO);
  }
}
