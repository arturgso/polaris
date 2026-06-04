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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(roles = "ADMIN")
class PersonsControllerIntegrationTests {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldPerformCrudForPersonsEndpoint() throws Exception {
    String createResponse =
        mockMvc
            .perform(
                post("/persons")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {"name":"Alice","birthdayMonth":1,"birthdayDay":10}
                        """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value("Alice"))
            .andExpect(jsonPath("$.birthdayMonth").value(1))
            .andExpect(jsonPath("$.birthdayDay").value(10))
            .andExpect(jsonPath("$.birthday").value("10/01"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    Long id = readId(createResponse);

    mockMvc
        .perform(get("/persons/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.name").value("Alice"));

    mockMvc
        .perform(get("/persons"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].name", hasItem("Alice")));

    mockMvc
        .perform(
            patch("/persons/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"name":"Alicia","birthdayMonth":2,"birthdayDay":29}
                    """))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/persons/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Alicia"))
        .andExpect(jsonPath("$.birthday").value("29/02"));

    mockMvc.perform(delete("/persons/{id}", id)).andExpect(status().isOk());
    mockMvc.perform(get("/persons/{id}", id)).andExpect(status().isNotFound());
  }

  @Test
  void shouldAcceptNullBirthdayPair() throws Exception {
    mockMvc
        .perform(
            post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"name":"Bob"}
                    """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.birthday").doesNotExist());
  }

  @Test
  void shouldRejectIncompleteOrInvalidBirthday() throws Exception {
    mockMvc
        .perform(
            post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"name":"Carol","birthdayMonth":5}
                    """))
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(
            post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"name":"Dan","birthdayMonth":2,"birthdayDay":30}
                    """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnNotFoundForMissingPerson() throws Exception {
    mockMvc.perform(get("/persons/{id}", 999999L)).andExpect(status().isNotFound());
    mockMvc.perform(delete("/persons/{id}", 999999L)).andExpect(status().isNotFound());
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
