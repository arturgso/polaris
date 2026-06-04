package io.vexis.polaris;

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
class UsersControllerIntegrationTests {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldCreateAdminUserWithoutReturningPassword() throws Exception {
    mockMvc
        .perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"username":"Admin User","password":"secret123"}
                    """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isString())
        .andExpect(jsonPath("$.username").value("admin user"))
        .andExpect(jsonPath("$.password").doesNotExist())
        .andExpect(jsonPath("$.createdAt").exists())
        .andExpect(jsonPath("$.updatedAt").exists());
  }

  @Test
  void shouldRejectDuplicateUsername() throws Exception {
    String content =
        """
        {"username":"duplicate","password":"secret123"}
        """;

    mockMvc
        .perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().isCreated());

    mockMvc
        .perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().isConflict());
  }

  @Test
  void shouldCreateAdminUserThroughPrivateSignupEndpoint() throws Exception {
    mockMvc
        .perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"username":"Signup Admin","password":"secret123"}
                    """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isString())
        .andExpect(jsonPath("$.username").value("signup admin"))
        .andExpect(jsonPath("$.password").doesNotExist());
  }
}
