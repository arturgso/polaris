package io.vexis.polaris;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                                {"tag":"graduation","name":"graduacao","color":"#111827"}
                                """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.tag").value(TextUtils.normalizeTag("graduacao")))
            .andExpect(jsonPath("$.name").value("graduacao"))
            .andExpect(jsonPath("$.color").value("#111827"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    String tag = readTag(createResponse);

    mockMvc
        .perform(get("/events"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].tag", hasItem(tag)));

    mockMvc
        .perform(
            patch("/events/{tag}", tag)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                {"tag":"retirement","name":"aposentadoria"}
                                """))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/events"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].tag", hasItem("RETIREMENT")))
        .andExpect(jsonPath("$[*].name", hasItem("aposentadoria")));

    mockMvc.perform(delete("/events/{tag}", "RETIREMENT")).andExpect(status().isOk());
  }

  private String readTag(String json) {
    int start = json.indexOf("\"tag\":\"");
    int end = json.indexOf("\"", start + 7);
    return json.substring(start + 7, end);
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
