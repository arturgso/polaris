package io.vexis.polaris;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vexis.polaris.domain.interfaces.repositories.EventsRepository;
import io.vexis.polaris.domain.interfaces.repositories.GiftListRepository;
import io.vexis.polaris.domain.interfaces.repositories.GiftsRepository;
import io.vexis.polaris.domain.interfaces.repositories.PersonsRepository;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemCategoriesRepository;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemRepository;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingListRepository;
import io.vexis.polaris.domain.models.entities.Event;
import io.vexis.polaris.domain.models.entities.Gift;
import io.vexis.polaris.domain.models.entities.GiftList;
import io.vexis.polaris.domain.models.entities.Person;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import io.vexis.polaris.domain.models.entities.ShoppingItemCategory;
import io.vexis.polaris.domain.models.entities.ShoppingList;
import io.vexis.polaris.domain.enums.GiftStatus;
import io.vexis.polaris.domain.enums.ShoppingItemStatus;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VaultIntegrationTests {

  @Autowired private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired private PersonsRepository personsRepository;

  @Autowired private GiftListRepository giftListRepository;

  @Autowired private GiftsRepository giftsRepository;

  @Autowired private EventsRepository eventRepository;

  @Autowired private ShoppingListRepository shoppingListRepository;

  @Autowired private ShoppingItemRepository shoppingItemRepository;

  @Autowired private ShoppingItemCategoriesRepository shoppingItemCategoriesRepository;

  @Test
  void shouldUnlockVaultAndListOnlySecretResources() throws Exception {
    seedVaultData();
    String authToken = loginAdmin();
    String vaultToken = unlockVault(authToken);

    mockMvc
        .perform(
            get("/vault/gifts")
                .header("Authorization", bearer(authToken))
                .header("X-Vault-Token", vaultToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[*].title", hasItem("secret gift")));

    mockMvc
        .perform(
            get("/vault/gift-lists")
                .header("Authorization", bearer(authToken))
                .header("X-Vault-Token", vaultToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[*].title", hasItem("secret gift list")))
        .andExpect(jsonPath("$[*].inVault", hasItem(true)));

    mockMvc
        .perform(
            get("/vault/shopping-items")
                .header("Authorization", bearer(authToken))
                .header("X-Vault-Token", vaultToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[*].title", hasItem("secret shopping item")));

    mockMvc
        .perform(
            get("/vault/shopping-lists")
                .header("Authorization", bearer(authToken))
                .header("X-Vault-Token", vaultToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[*].title", hasItem("secret shopping list")))
        .andExpect(jsonPath("$[*].inVault", hasItem(true)));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldRejectVaultRequestsWithoutVaultToken() throws Exception {
    seedVaultData();

    mockMvc.perform(get("/vault/gifts")).andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectVaultRequestsWithInvalidVaultToken() throws Exception {
    seedVaultData();
    String authToken = loginAdmin();

    mockMvc
        .perform(
            get("/vault/gifts")
                .header("Authorization", bearer(authToken))
                .header("X-Vault-Token", "invalid-token"))
        .andExpect(status().isForbidden());
  }

  @Test
  void shouldRequireVaultPasswordForSecretListChanges() throws Exception {
    seedVaultData();
    String authToken = loginAdmin();
    Long secretGiftListId =
        giftListRepository.findAll().stream()
            .filter(list -> Boolean.TRUE.equals(list.getInVault()))
            .filter(list -> "secret gift list".equals(list.getTitle()))
            .findFirst()
            .orElseThrow()
            .getId();

    mockMvc
        .perform(
            patch("/gift-lists/{id}", secretGiftListId)
                .header("Authorization", bearer(authToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"title":"updated secret gift list"}
                    """))
        .andExpect(status().isUnauthorized());

    mockMvc
        .perform(
            patch("/gift-lists/{id}", secretGiftListId)
                .header("Authorization", bearer(authToken))
                .header("X-Vault-Password", "teste")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"title":"updated secret gift list"}
                    """))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            delete("/gift-lists/{id}", secretGiftListId)
                .header("Authorization", bearer(authToken))
                .header("X-Vault-Password", "teste"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldRequireVaultPasswordForSecretShoppingListDeletion() throws Exception {
    seedVaultData();
    String authToken = loginAdmin();
    Long secretShoppingListId =
        shoppingListRepository.findAll().stream()
            .filter(list -> Boolean.TRUE.equals(list.getInVault()))
            .filter(list -> "secret shopping list".equals(list.getTitle()))
            .findFirst()
            .orElseThrow()
            .getId();

    mockMvc
        .perform(
            delete("/shopping-lists/{id}", secretShoppingListId)
                .header("Authorization", bearer(authToken)))
        .andExpect(status().isUnauthorized());

    mockMvc
        .perform(
            delete("/shopping-lists/{id}", secretShoppingListId)
                .header("Authorization", bearer(authToken))
                .header("X-Vault-Password", "teste"))
        .andExpect(status().isOk());
  }

  private void seedVaultData() {
    Person person =
        personsRepository.save(
            Person.builder()
                .name("Aline")
                .birthdayMonth((short) 4)
                .birthdayDay((short) 20)
                .build());

    Event event =
        eventRepository.save(
            Event.builder().tag("birthday").name("Birthday").color("#111827").build());
    GiftStatus giftStatus = GiftStatus.IDEA;
    GiftList publicGiftList = new GiftList();
    publicGiftList.setTitle("public gift list");
    publicGiftList.setInVault(false);
    publicGiftList = giftListRepository.save(publicGiftList);
    GiftList secretGiftList = new GiftList();
    secretGiftList.setTitle("secret gift list");
    secretGiftList.setInVault(true);
    secretGiftList = giftListRepository.save(secretGiftList);

    giftsRepository.save(
        Gift.builder()
            .title("public gift")
            .giftFor(person)
            .event(event)
            .status(giftStatus)
            .giftList(publicGiftList)
            .inVault(false)
            .build());
    giftsRepository.save(
        Gift.builder()
            .title("secret gift")
            .giftFor(person)
            .event(event)
            .status(giftStatus)
            .giftList(secretGiftList)
            .inVault(true)
            .build());

    ShoppingItemCategory category =
        shoppingItemCategoriesRepository.save(
            ShoppingItemCategory.builder().tag("tech").name("Tech").color("#0EA5E9").build());
    ShoppingList publicShoppingList = new ShoppingList();
    publicShoppingList.setTitle("public shopping list");
    publicShoppingList.setInVault(false);
    publicShoppingList = shoppingListRepository.save(publicShoppingList);
    ShoppingList secretShoppingList = new ShoppingList();
    secretShoppingList.setTitle("secret shopping list");
    secretShoppingList.setInVault(true);
    secretShoppingList = shoppingListRepository.save(secretShoppingList);

    shoppingItemRepository.save(
        ShoppingItem.builder()
            .title("public shopping item")
            .category(category)
            .price(new BigDecimal("10.00"))
            .status(ShoppingItemStatus.IDEA)
            .shoppingList(publicShoppingList)
            .inVault(false)
            .build());
    shoppingItemRepository.save(
        ShoppingItem.builder()
            .title("secret shopping item")
            .category(category)
            .price(new BigDecimal("20.00"))
            .status(ShoppingItemStatus.IDEA)
            .shoppingList(secretShoppingList)
            .inVault(true)
            .build());
  }

  private String loginAdmin() throws Exception {
    return tokenFrom(
        mockMvc
            .perform(
                post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {"username":"admin","password":"secret123"}
                        """))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString());
  }

  private String unlockVault(String authToken) throws Exception {
    return tokenFrom(
        mockMvc
            .perform(
                post("/vault/unlock")
                    .header("Authorization", bearer(authToken))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {"password":"teste"}
                        """))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString());
  }

  private String tokenFrom(String responseBody) throws Exception {
    JsonNode tokenNode = objectMapper.readTree(responseBody).get("token");
    return tokenNode.asText();
  }

  private String bearer(String token) {
    return "Bearer " + token;
  }
}
