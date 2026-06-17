package io.vexis.polaris.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.vexis.polaris.application.security.JwtService;
import io.vexis.polaris.domain.interfaces.services.GiftListService;
import io.vexis.polaris.domain.interfaces.services.GiftsService;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemService;
import io.vexis.polaris.domain.interfaces.services.ShoppingListService;
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
  private static final String VAULT_PASSWORD = "vault-secret";

  @Mock private ShoppingListService shoppingListService;

  @Mock private ShoppingItemService shoppingItemService;

  @Mock private GiftListService giftListService;

  @Mock private GiftsService giftsService;

  @Mock private JwtService jwtService;

  @InjectMocks private VaultServiceImpl vaultService;

  @BeforeEach
  void setUp() {
    vaultService.allowedUser = ALLOWED_USER;
    vaultService.vaultPasswordHash = VAULT_PASSWORD;
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
}
