package io.vexis.polaris.application.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.vexis.polaris.domain.interfaces.services.VaultService;
import io.vexis.polaris.domain.models.dtos.filters.GiftFiltersDTO;
import io.vexis.polaris.domain.models.dtos.filters.ListEntityFiltersDTO;
import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.vault.UnlockVaultRequestDTO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class VaultControllerTests {

  @Mock private VaultService vaultService;

  @Mock private Authentication authentication;

  @InjectMocks private VaultController controller;

  @Test
  void shouldForwardVaultGiftFiltersWithVaultFlag() {
    when(vaultService.listGifts(any())).thenReturn(List.of());

    ResponseEntity<List<GiftDTO>> response =
        controller.listGifts("idea", "birthday", "book", "link", 7L);

    assertThat(response.getBody()).isEmpty();

    ArgumentCaptor<GiftFiltersDTO> captor = ArgumentCaptor.forClass(GiftFiltersDTO.class);
    verify(vaultService).listGifts(captor.capture());
    assertThat(captor.getValue().personId()).isNull();
    assertThat(captor.getValue().status()).isEqualTo("idea");
    assertThat(captor.getValue().event()).isEqualTo("birthday");
    assertThat(captor.getValue().title()).isEqualTo("book");
    assertThat(captor.getValue().link()).isEqualTo("link");
    assertThat(captor.getValue().giftListId()).isEqualTo(7L);
    assertThat(captor.getValue().inVault()).isTrue();
  }

  @Test
  void shouldForwardVaultListFiltersWithVaultFlag() {
    when(vaultService.listGiftLists(any())).thenReturn(List.of());
    when(vaultService.listShoppingLists(any())).thenReturn(List.of());

    controller.listGiftLists("secret gifts");
    controller.listShoppingLists("secret shopping");

    ArgumentCaptor<ListEntityFiltersDTO> listCaptor =
        ArgumentCaptor.forClass(ListEntityFiltersDTO.class);
    verify(vaultService).listGiftLists(listCaptor.capture());
    assertThat(listCaptor.getValue().title()).isEqualTo("secret gifts");
    assertThat(listCaptor.getValue().inVault()).isTrue();

    ArgumentCaptor<ListEntityFiltersDTO> shoppingCaptor =
        ArgumentCaptor.forClass(ListEntityFiltersDTO.class);
    verify(vaultService).listShoppingLists(shoppingCaptor.capture());
    assertThat(shoppingCaptor.getValue().title()).isEqualTo("secret shopping");
    assertThat(shoppingCaptor.getValue().inVault()).isTrue();
  }

  @Test
  void shouldForwardVaultShoppingItemFiltersWithVaultFlag() {
    when(vaultService.listShoppingItems(any())).thenReturn(List.of());

    controller.listShoppingItems("planned", "tech", "mouse", 8L);

    ArgumentCaptor<ShoppingItemFiltersDTO> captor =
        ArgumentCaptor.forClass(ShoppingItemFiltersDTO.class);
    verify(vaultService).listShoppingItems(captor.capture());
    assertThat(captor.getValue().status()).isEqualTo("planned");
    assertThat(captor.getValue().tag()).isEqualTo("tech");
    assertThat(captor.getValue().title()).isEqualTo("mouse");
    assertThat(captor.getValue().listId()).isEqualTo(8L);
    assertThat(captor.getValue().inVault()).isTrue();
  }

  @Test
  void shouldUnlockVaultForAuthenticatedUser() {
    UserDetails userDetails =
        User.withUsername("admin").password("password").roles("ADMIN").build();
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(vaultService.unlock("vault-secret", userDetails)).thenReturn("vault-token");

    var response = controller.unlock(new UnlockVaultRequestDTO("vault-secret"), authentication);

    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().token()).isEqualTo("vault-token");
    verify(vaultService).unlock("vault-secret", userDetails);
  }
}
