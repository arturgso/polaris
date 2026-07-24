package io.vexis.polaris;

import static io.vexis.polaris.shared.ListConstants.NO_LIST_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.vexis.polaris.domain.enums.GiftStatus;
import io.vexis.polaris.domain.enums.ShoppingItemStatus;
import io.vexis.polaris.domain.exceptions.VaultAuthenticationException;
import io.vexis.polaris.domain.interfaces.repositories.GiftListRepository;
import io.vexis.polaris.domain.interfaces.repositories.GiftsRepository;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemRepository;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingListRepository;
import io.vexis.polaris.domain.interfaces.services.GiftListService;
import io.vexis.polaris.domain.interfaces.services.GiftsService;
import io.vexis.polaris.domain.interfaces.services.PersonsService;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemCategoriesService;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemService;
import io.vexis.polaris.domain.interfaces.services.ShoppingListService;
import io.vexis.polaris.domain.models.dtos.filters.GiftFiltersDTO;
import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.dtos.gifts.NewGiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.UpdateGiftDTO;
import io.vexis.polaris.domain.models.dtos.persons.NewPersonDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.categories.NewCategoryDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.NewShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.UpdateShoppingItemDTO;
import io.vexis.polaris.shared.dtos.NewListDTO;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ListAssociationIntegrationTests {

  private static final String VAULT_PASSWORD = "teste";

  @Autowired private GiftsService giftsService;
  @Autowired private GiftListService giftListService;
  @Autowired private GiftsRepository giftsRepository;
  @Autowired private GiftListRepository giftListRepository;
  @Autowired private PersonsService personsService;

  @Autowired private ShoppingItemService shoppingItemService;
  @Autowired private ShoppingListService shoppingListService;
  @Autowired private ShoppingItemRepository shoppingItemRepository;
  @Autowired private ShoppingListRepository shoppingListRepository;
  @Autowired private ShoppingItemCategoriesService categoriesService;

  @Test
  void shouldAssociateFilterAndRemoveGiftList() {
    var person = personsService.create(new NewPersonDTO("gift owner", (short) 1, (short) 2));
    var list = giftListService.create(new NewListDTO("gift list", null));

    var gift =
        giftsService.create(
            new NewGiftDTO(
                "book", null, person.id(), "ANIVERSARIO", GiftStatus.IDEIA, list.getId()));

    assertThat(gift.giftList().id()).isEqualTo(list.getId());
    assertThat(
            giftsService.list(
                new GiftFiltersDTO(null, null, null, null, null, list.getId(), false)))
        .extracting(result -> result.id())
        .containsExactly(gift.id());

    giftsService.update(new UpdateGiftDTO(null, null, null, NO_LIST_ID, null, null), gift.id());

    assertThat(giftsRepository.findById(gift.id()).orElseThrow().getGiftList()).isNull();
    assertThat(
            giftsService.list(new GiftFiltersDTO(null, null, null, null, null, NO_LIST_ID, false)))
        .extracting(result -> result.id())
        .contains(gift.id());
  }

  @Test
  void shouldAssociateFilterAndRemoveShoppingList() {
    categoriesService.create(new NewCategoryDTO("association category"));
    var list = shoppingListService.create(new NewListDTO("shopping list", null));

    var item =
        shoppingItemService.create(
            new NewShoppingItemDTO(
                "coffee",
                null,
                "ASSOCIATIONCATEGORY",
                BigDecimal.TEN,
                ShoppingItemStatus.PLANEJADO,
                list.getId()));

    assertThat(item.shoppingList().id()).isEqualTo(list.getId());
    assertThat(
            shoppingItemService.list(
                new ShoppingItemFiltersDTO(null, null, null, list.getId(), false)))
        .extracting(result -> result.id())
        .containsExactly(item.id());

    shoppingItemService.update(
        new UpdateShoppingItemDTO(null, null, null, null, null, NO_LIST_ID), item.id());

    assertThat(shoppingItemRepository.findById(item.id()).orElseThrow().getShoppingList()).isNull();
    assertThat(
            shoppingItemService.list(
                new ShoppingItemFiltersDTO(null, null, null, NO_LIST_ID, false)))
        .extracting(result -> result.id())
        .contains(item.id());
  }

  @Test
  void shouldRequireVaultPasswordAndInheritListVisibility() {
    var person = personsService.create(new NewPersonDTO("vault owner", (short) 3, (short) 4));
    var giftList = giftListService.create(new NewListDTO("vault gifts", null));
    giftListService.moveToVault(giftList.getId());

    var giftRequest =
        new NewGiftDTO(
            "secret gift", null, person.id(), "ANIVERSARIO", GiftStatus.IDEIA, giftList.getId());

    assertThatThrownBy(() -> giftsService.create(giftRequest))
        .isInstanceOf(VaultAuthenticationException.class);

    var gift = giftsService.create(giftRequest, VAULT_PASSWORD);
    assertThat(giftsRepository.findById(gift.id()).orElseThrow().getInVault()).isTrue();

    categoriesService.create(new NewCategoryDTO("vault association"));
    var shoppingList = shoppingListService.create(new NewListDTO("vault shopping", null));
    shoppingListService.moveToVault(shoppingList.getId());
    var itemRequest =
        new NewShoppingItemDTO(
            "secret item",
            null,
            "VAULTASSOCIATION",
            BigDecimal.ONE,
            ShoppingItemStatus.IDEIA,
            shoppingList.getId());

    assertThatThrownBy(() -> shoppingItemService.create(itemRequest))
        .isInstanceOf(VaultAuthenticationException.class);

    var item = shoppingItemService.create(itemRequest, VAULT_PASSWORD);
    assertThat(shoppingItemRepository.findById(item.id()).orElseThrow().getInVault()).isTrue();
  }

  @Test
  void shouldPreserveOrDeleteChildrenWhenDeletingLists() {
    var person = personsService.create(new NewPersonDTO("delete owner", (short) 5, (short) 6));
    var preservedGiftList = giftListService.create(new NewListDTO("preserved gifts", null));
    var preservedGift =
        giftsService.create(
            new NewGiftDTO(
                "preserved gift",
                null,
                person.id(),
                "ANIVERSARIO",
                GiftStatus.IDEIA,
                preservedGiftList.getId()));

    giftListService.delete(preservedGiftList.getId(), false, null);

    assertThat(giftListRepository.existsById(preservedGiftList.getId())).isFalse();
    assertThat(giftsRepository.findById(preservedGift.id()).orElseThrow().getGiftList()).isNull();

    var deletedGiftList = giftListService.create(new NewListDTO("deleted gifts", null));
    var deletedGift =
        giftsService.create(
            new NewGiftDTO(
                "deleted gift",
                null,
                person.id(),
                "ANIVERSARIO",
                GiftStatus.IDEIA,
                deletedGiftList.getId()));

    giftListService.delete(deletedGiftList.getId(), true, null);

    assertThat(giftListRepository.existsById(deletedGiftList.getId())).isFalse();
    assertThat(giftsRepository.existsById(deletedGift.id())).isFalse();

    categoriesService.create(new NewCategoryDTO("delete association"));
    var preservedShoppingList =
        shoppingListService.create(new NewListDTO("preserved shopping", null));
    var preservedItem =
        shoppingItemService.create(
            new NewShoppingItemDTO(
                "preserved item",
                null,
                "DELETEASSOCIATION",
                BigDecimal.ONE,
                ShoppingItemStatus.IDEIA,
                preservedShoppingList.getId()));

    shoppingListService.delete(preservedShoppingList.getId(), false, null);

    assertThat(shoppingListRepository.existsById(preservedShoppingList.getId())).isFalse();
    assertThat(shoppingItemRepository.findById(preservedItem.id()).orElseThrow().getShoppingList())
        .isNull();

    var deletedShoppingList = shoppingListService.create(new NewListDTO("deleted shopping", null));
    var deletedItem =
        shoppingItemService.create(
            new NewShoppingItemDTO(
                "deleted item",
                null,
                "DELETEASSOCIATION",
                BigDecimal.ONE,
                ShoppingItemStatus.IDEIA,
                deletedShoppingList.getId()));

    shoppingListService.delete(deletedShoppingList.getId(), true, null);

    assertThat(shoppingListRepository.existsById(deletedShoppingList.getId())).isFalse();
    assertThat(shoppingItemRepository.existsById(deletedItem.id())).isFalse();
  }
}
