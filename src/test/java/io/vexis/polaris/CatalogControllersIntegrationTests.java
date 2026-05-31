package io.vexis.polaris;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class CatalogControllersIntegrationTests {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldPerformCrudForEventsEndpoint() throws Exception {
    String createResponse =
        mockMvc
            .perform(
                post("/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                                {"name":"graduation","color":"#111827"}
                                """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("GRADUATION"))
            .andExpect(jsonPath("$.color").value("#111827"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    Long id = readId(createResponse);

    mockMvc
        .perform(get("/events"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].name", hasItem("GRADUATION")));

    mockMvc
        .perform(
            patch("/events/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                {"name":"retirement"}
                                """))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/events"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].name", hasItem("RETIREMENT")));

    mockMvc.perform(delete("/events/{id}", id)).andExpect(status().isOk());
  }

  @Test
  void shouldPerformCrudForGiftStatusEndpoint() throws Exception {
    String createResponse =
        mockMvc
            .perform(
                post("/gift-status")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                                {"name":"wrapped","color":"#F97316"}
                                """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("WRAPPED"))
            .andExpect(jsonPath("$.color").value("#F97316"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    Long id = readId(createResponse);

    mockMvc
        .perform(get("/gift-status"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].name", hasItem("WRAPPED")));

    mockMvc
        .perform(
            patch("/gift-status/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                {"name":"shipped"}
                                """))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/gift-status"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].name", hasItem("SHIPPED")));

    mockMvc.perform(delete("/gift-status/{id}", id)).andExpect(status().isOk());
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
