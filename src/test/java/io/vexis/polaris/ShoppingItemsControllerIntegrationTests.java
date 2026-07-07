package io.vexis.polaris;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.vexis.polaris.domain.enums.ShoppingItemStatus;
import io.vexis.polaris.shared.TextUtils;
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
@WithMockUser(roles = "ADMIN")
class ShoppingItemsControllerIntegrationTests {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldListSeededShoppingItemDependencies() throws Exception {
    mockMvc
        .perform(get("/statuses"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.shoppingItemsStatus[*].value", hasItem("IDEA")))
        .andExpect(jsonPath("$.shoppingItemsStatus[*].value", hasItem("TO_BUY")))
        .andExpect(jsonPath("$.shoppingItemsStatus[*].value", hasItem("BOUGHT")));

    String categoryResponse = createCategory("e2e dep", "#111827");
    Long categoryId = readId(categoryResponse);

    mockMvc
        .perform(get("/shopping-item-categories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.tags", hasItem(TextUtils.normalizeTag("e2e dep"))));
  }

  @Test
  void shouldPerformCrudForShoppingItemsEndpoint() throws Exception {
    createCategory("e2e item cat", "#06B6D4");
    createCategory("e2e upd cat", "#10B981");
    String createResponse =
        mockMvc
            .perform(
                post("/shopping-items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                          "title":"USB-C Cable",
                          "link":"https://example.com/cable",
                          "category":"E2EITEMCAT",
                          "price":39.90,
                          "status":"PLANNED"
                        }
                        """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.title").value("usb-c cable"))
            .andExpect(jsonPath("$.link").value("https://example.com/cable"))
            .andExpect(jsonPath("$.price").value(39.90))
            .andExpect(jsonPath("$.category").value(TextUtils.normalizeTag("e2e item cat")))
            .andExpect(jsonPath("$.status.value").value("PLANNED"))
            .andExpect(jsonPath("$.status.name").value("planejado"))
            .andExpect(jsonPath("$.status.color").value("#3B82F6"))
            .andExpect(jsonPath("$.createdAt").exists())
            .andExpect(jsonPath("$.updatedAt").exists())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Long itemId = readId(createResponse);
    Long shoppingListId = readId(createShoppingList("e2e list"));

    mockMvc
        .perform(get("/shopping-items"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id", hasItem(itemId.intValue())));

    mockMvc
        .perform(get("/shopping-items").param("title", "USB-C"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id", hasItem(itemId.intValue())));

    mockMvc
        .perform(get("/shopping-items").param("tag", TextUtils.normalizeTag("e2e item cat")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id", hasItem(itemId.intValue())));

    mockMvc
        .perform(get("/shopping-items").param("status", "PLANNED"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id", hasItem(itemId.intValue())));

    mockMvc
        .perform(
            patch("/shopping-items/{id}", itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "title":"Wireless Mouse",
                      "category":"E2EUPDCAT",
                      "price":89.50,
                      "status":"BOUGHT",
                      "listId":%d
                    }
                    """
                        .formatted(shoppingListId)))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/shopping-items").param("title", "mouse"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id", hasItem(itemId.intValue())))
        .andExpect(jsonPath("$[?(@.id == %d)].title".formatted(itemId), hasItem("wireless mouse")))
        .andExpect(jsonPath("$[?(@.id == %d)].price".formatted(itemId), hasItem(89.50)))
        .andExpect(
            jsonPath(
                "$[?(@.id == %d)].category".formatted(itemId),
                hasItem(TextUtils.normalizeTag("e2e upd cat"))))
        .andExpect(jsonPath("$[?(@.id == %d)].status.value".formatted(itemId), hasItem("BOUGHT")))
        .andExpect(
            jsonPath(
                "$[?(@.id == %d)].shoppingList.id".formatted(itemId),
                hasItem(shoppingListId.intValue())))
        .andExpect(
            jsonPath("$[?(@.id == %d)].shoppingList.title".formatted(itemId), hasItem("e2e list")));

    mockMvc.perform(delete("/shopping-items/{id}", itemId)).andExpect(status().isOk());

    mockMvc
        .perform(get("/shopping-items").param("title", "mouse"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id").isEmpty());
  }

  @Test
  void shouldReturnDashboardShoppingItemMetricsAndRecentItems() throws Exception {
    createCategory("e2e dash", "#EC4899");
    ShoppingItemStatus status = ShoppingItemStatus.COMPRAR;

    for (int index = 1; index <= 6; index++) {
      createShoppingItem(
          "Dashboard Item " + index,
          "https://example.com/dashboard-" + index,
          "E2EDASH",
          index * 10,
          status);
    }

    mockMvc
        .perform(get("/dashboard/metrics"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.shoppingItemsCount").value(6))
        .andExpect(jsonPath("$.shoppingItemsTotal").value(210.0));

    mockMvc
        .perform(get("/dashboard/recent-shopping-items"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(5)))
        .andExpect(jsonPath("$[0].title").value("dashboard item 6"))
        .andExpect(jsonPath("$[1].title").value("dashboard item 5"))
        .andExpect(jsonPath("$[4].title").value("dashboard item 2"));
  }

  @Test
  void shouldRejectInvalidShoppingItemRequests() throws Exception {
    mockMvc
        .perform(
            post("/shopping-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"link":"https://example.com/item","price":10.00}
                    """))
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(
            post("/shopping-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"title":"Item without price","link":"https://example.com/item"}
                    """))
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(
            post("/shopping-item-categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"tag":"invalid color","name":"invalid color","color":"blue"}
                    """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnNotFoundForMissingShoppingItem() throws Exception {
    mockMvc
        .perform(
            patch("/shopping-items/{id}", 999999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"title":"missing item"}
                    """))
        .andExpect(status().isNotFound());

    mockMvc.perform(delete("/shopping-items/{id}", 999999L)).andExpect(status().isNotFound());
  }

  private String createCategory(String name, String color) throws Exception {
    return mockMvc
        .perform(
            post("/shopping-item-categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"tag":"%s","name":"%s","color":"%s"}
                    """
                        .formatted(name, name, color)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.tag").value(TextUtils.normalizeTag(name)))
        .andExpect(jsonPath("$.name").value(name))
        .andExpect(jsonPath("$.color").value(color))
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  private String createShoppingItem(
      String title, String link, String categoryTag, int price, ShoppingItemStatus status)
      throws Exception {
    return mockMvc
        .perform(
            post("/shopping-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "title":"%s",
                      "link":"%s",
                      "category":"%s",
                      "price":%d,
                      "status":"%s"
                    }
                    """
                        .formatted(title, link, categoryTag, price, status.name())))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  private String createShoppingList(String title) throws Exception {
    return mockMvc
        .perform(
            post("/shopping-lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"title":"%s"}
                    """
                        .formatted(title)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.title").value(title))
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  private Long readId(String json) {
    int start = json.indexOf("\"id\":");
    int end = json.indexOf(",", start);
    if (end < 0) {
      end = json.indexOf("}", start);
    }
    return Long.parseLong(json.substring(start + 5, end).trim());
  }
}
