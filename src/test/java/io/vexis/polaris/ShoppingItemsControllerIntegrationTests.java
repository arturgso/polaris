package io.vexis.polaris;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemStatusesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ShoppingItemsControllerIntegrationTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ShoppingItemStatusesRepository statusesRepository;

  @Test
  void shouldListSeededShoppingItemDependencies() throws Exception {
    mockMvc
        .perform(get("/shopping-item-statuses"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].name", hasItem("IDEA")))
        .andExpect(jsonPath("$[*].name", hasItem("TO_BUY")))
        .andExpect(jsonPath("$[*].name", hasItem("BOUGHT")));

    String categoryResponse = createCategory("e2e dep", "#111827");
    Long categoryId = readId(categoryResponse);

    mockMvc
        .perform(get("/shopping-item-categories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id", hasItem(categoryId.intValue())))
        .andExpect(jsonPath("$[*].name", hasItem("E2E DEP")));
  }

  @Test
  void shouldPerformCrudForShoppingItemsEndpoint() throws Exception {
    Long categoryId = readId(createCategory("e2e item cat", "#06B6D4"));
    Long updatedCategoryId = readId(createCategory("e2e upd cat", "#10B981"));
    Long plannedStatusId = getStatusId("PLANNED");
    Long boughtStatusId = getStatusId("BOUGHT");

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
                          "categoryId":%d,
                          "price":39.90,
                          "statusId":%d
                        }
                        """
                            .formatted(categoryId, plannedStatusId)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.title").value("usb-c cable"))
            .andExpect(jsonPath("$.link").value("https://example.com/cable"))
            .andExpect(jsonPath("$.price").value(39.90))
            .andExpect(jsonPath("$.category.id").value(categoryId))
            .andExpect(jsonPath("$.category.name").value("E2E ITEM CAT"))
            .andExpect(jsonPath("$.category.color").value("#06B6D4"))
            .andExpect(jsonPath("$.status.id").value(plannedStatusId))
            .andExpect(jsonPath("$.status.name").value("PLANNED"))
            .andExpect(jsonPath("$.createdAt").exists())
            .andExpect(jsonPath("$.updatedAt").exists())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Long itemId = readId(createResponse);

    mockMvc
        .perform(get("/shopping-items"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id", hasItem(itemId.intValue())));

    mockMvc
        .perform(get("/shopping-items").param("title", "USB-C"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id", hasItem(itemId.intValue())));

    mockMvc
        .perform(get("/shopping-items").param("categoryId", categoryId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id", hasItem(itemId.intValue())));

    mockMvc
        .perform(get("/shopping-items").param("statusId", plannedStatusId.toString()))
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
                      "categoryId":%d,
                      "price":89.50,
                      "statusId":%d
                    }
                    """
                        .formatted(updatedCategoryId, boughtStatusId)))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/shopping-items").param("title", "mouse"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id", hasItem(itemId.intValue())))
        .andExpect(jsonPath("$[?(@.id == %d)].title".formatted(itemId), hasItem("wireless mouse")))
        .andExpect(jsonPath("$[?(@.id == %d)].price".formatted(itemId), hasItem(89.50)))
        .andExpect(
            jsonPath("$[?(@.id == %d)].category.name".formatted(itemId), hasItem("E2E UPD CAT")))
        .andExpect(jsonPath("$[?(@.id == %d)].status.name".formatted(itemId), hasItem("BOUGHT")));

    mockMvc.perform(delete("/shopping-items/{id}", itemId)).andExpect(status().isOk());

    mockMvc
        .perform(get("/shopping-items").param("title", "mouse"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id").isEmpty());
  }

  @Test
  void shouldReturnDashboardShoppingItemMetricsAndRecentItems() throws Exception {
    Long categoryId = readId(createCategory("e2e dash", "#EC4899"));
    Long statusId = getStatusId("TO_BUY");

    for (int index = 1; index <= 6; index++) {
      createShoppingItem(
          "Dashboard Item " + index,
          "https://example.com/dashboard-" + index,
          categoryId,
          index * 10,
          statusId);
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
                    {"title":"Item without link","price":10.00}
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
                    {"name":"invalid color","color":"blue"}
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
                    {"name":"%s","color":"%s"}
                    """
                        .formatted(name, color)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.name").value(name.toUpperCase()))
        .andExpect(jsonPath("$.color").value(color))
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  private String createShoppingItem(
      String title, String link, Long categoryId, int price, Long statusId) throws Exception {
    return mockMvc
        .perform(
            post("/shopping-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "title":"%s",
                      "link":"%s",
                      "categoryId":%d,
                      "price":%d,
                      "statusId":%d
                    }
                    """
                        .formatted(title, link, categoryId, price, statusId)))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  private Long getStatusId(String name) {
    return statusesRepository.findByName(name).orElseThrow().getId();
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
